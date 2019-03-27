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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

@Service
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
    public OrderMasterDto cancel(OrderMasterDto orderMasterDto) {
        
        return null;
    }

    @Override
    public OrderMasterDto finish(OrderMasterDto orderMasterDto) {
        //判断订单状态
        if(!orderMasterDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            System.out.println("完结订单不正确");
        }
        return null;
    }

    @Override
    public OrderMasterDto paid(OrderMasterDto orderMasterDto) {
        return null;
    }
}
