package com.imooc.sell.service.impl;

import com.imooc.sell.dao.OrderDetailDao;
import com.imooc.sell.dao.OrderMasterDao;
import com.imooc.sell.dto.CartDto;
import com.imooc.sell.dto.OrderMasterDto;
import com.imooc.sell.entity.OrderDetail;
import com.imooc.sell.entity.OrderMaster;
import com.imooc.sell.entity.ProductInfo;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.service.IOrderService;
import com.imooc.sell.service.IProductService;
import com.imooc.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {
    //总价
    BigDecimal orderAmount=new BigDecimal(0);

    @Autowired
    private OrderMasterDao orderMasterDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private IProductService productService;

    @Override
    @Transactional
    public OrderMasterDto create(OrderMasterDto orderMasterDto) {
        //传给的购物车信息
        List<CartDto> cartDtoList=new ArrayList<>();
        String orderId=KeyUtil.genUniqueKey();
            //1.创建订单之前要先查询商品
        for (OrderDetail orderDetail : orderMasterDto.getOrderDetails()) {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if(productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIT);
            }
            //2.计算订单总价
            orderAmount=
                     productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            //3.订单详情入库
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetailDao.save(orderDetail);
            CartDto cartDto=new CartDto(orderDetail.getProductId(), orderDetail.getProductQuantity());
            cartDtoList.add(cartDto);
        }
        //4.写入数据库
        OrderMaster orderMaster=new OrderMaster();
        orderMasterDto.setOrderId(orderId);
        BeanUtils.copyProperties(orderMasterDto, orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterDao.save(orderMaster);

        //5.写入数据库
        productService.decreaseStock(cartDtoList);
        return orderMasterDto;
    }

    @Override
    public OrderMasterDto findOne(String orderId) {
        OrderMaster orderMaster = orderMasterDao.findById(orderId).get();
        if(orderMaster==null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIT);
        }
        List<OrderDetail> orderDetails = orderDetailDao.findByOrderId(orderMaster.getOrderId());
        if(orderDetails.size()==0){
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIT);
        }
        OrderMasterDto orderMasterDto=new OrderMasterDto();
        BeanUtils.copyProperties(orderMaster, orderMasterDto);
        orderMasterDto.setOrderDetails(orderDetails);
        return orderMasterDto;
    }

    @Override
    public Page<OrderMasterDto> findList(String openId, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterDao.findByBuyerOpenid(openId, pageable);
        List<OrderMaster> orderMasterList = orderMasterPage.getContent();
        List<OrderMasterDto> orderMasterDtoList=new ArrayList<>();
        for (OrderMaster orderMaster : orderMasterList) {
            OrderMasterDto orderMasterDto=new OrderMasterDto();
            BeanUtils.copyProperties(orderMaster,orderMasterDto);
            orderMasterDtoList.add(orderMasterDto);
        }
        Page<OrderMasterDto> orderMasterDtoPage=
                new PageImpl<OrderMasterDto>(orderMasterDtoList,pageable,orderMasterPage.getTotalElements());
        return orderMasterDtoPage;
    }

    @Override
    @Transactional
    public OrderMasterDto cancel(OrderMasterDto orderMasterDto) {
        OrderMaster orderMaster=new OrderMaster();
        //判断订单状态
        if(!orderMasterDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【取消订单】订单状态不正确，orderId={},orderStatus={}"
                    ,orderMasterDto.getOrderId(),orderMasterDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderMasterDto.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderMasterDto, orderMaster);
        OrderMaster updateResult = orderMasterDao.save(orderMaster);
        if(updateResult==null){
            log.error("【取消订单】更新失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //返回库存
        if(orderMasterDto.getOrderDetails().size()==0){
            log.error("【取消订单】订单中无商品,orderMasterDto={}",orderMasterDto);
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIT);
        }
        List<CartDto> cartDtoList=new ArrayList<>();
        for (OrderDetail orderDetail : orderMasterDto.getOrderDetails()) {
            CartDto cartDto=new CartDto(orderDetail.getProductId(), orderDetail.getProductQuantity());
            cartDtoList.add(cartDto);
        }
        productService.increaseStock(cartDtoList);
        //如果已支付需要退款
        if(orderMasterDto.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            //TODO
        }
        return orderMasterDto;
    }

    @Override
    @Transactional
    public OrderMasterDto finish(OrderMasterDto orderMasterDto) {
        //判断订单状态
        if(!orderMasterDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【完结订单】 订单状态不正确,orderId={},orderStatus={}",
                    orderMasterDto.getOrderId(),orderMasterDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        OrderMaster orderMaster=new OrderMaster();
        orderMasterDto.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        BeanUtils.copyProperties(orderMasterDto,orderMaster);
        OrderMaster updateResult = orderMasterDao.save(orderMaster);
        if(updateResult==null){
            log.error("【完结订单】更新失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderMasterDto;
    }

    @Override
    @Transactional
    public OrderMasterDto paid(OrderMasterDto orderMasterDto) {
        //判断订单状态 订单不处于新订单 说明状态不对
        if(!orderMasterDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单支付完成】 订单状态不正确,orderId={},orderStatus={}",
                    orderMasterDto.getOrderId(),orderMasterDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if(!orderMasterDto.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("【订单支付完成】订单支付状态不正确,orderMasterDto={}",orderMasterDto);
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改支付状态
        orderMasterDto.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderMasterDto,orderMaster);
        OrderMaster updateResult = orderMasterDao.save(orderMaster);
        if(updateResult==null){
            log.error("【订单支付完成】更新失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderMasterDto;
    }
}
