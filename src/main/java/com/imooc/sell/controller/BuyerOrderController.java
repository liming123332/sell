package com.imooc.sell.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imooc.sell.VO.ResultVO;
import com.imooc.sell.dto.OrderMasterDto;
import com.imooc.sell.entity.OrderDetail;
import com.imooc.sell.entity.OrderMaster;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.from.OrderForm;
import com.imooc.sell.service.IBuyerService;
import com.imooc.sell.service.IOrderService;
import com.imooc.sell.service.impl.BuyerServiceImpl;
import com.imooc.sell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IBuyerService buyerService;

    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid OrderForm orderForm, BindingResult bindingResult){
        Gson gson=new Gson();
        if(bindingResult.hasErrors()){
            log.error("【创建订单】 参数不正确，orderFrom={}" , orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        OrderMasterDto orderMasterDto=new OrderMasterDto();

        orderMasterDto.setBuyerName(orderForm.getName());
        orderMasterDto.setBuyerOpenid(orderForm.getOpenid());
        orderMasterDto.setBuyerPhone(orderForm.getPhone());
        orderMasterDto.setBuyerAddress(orderForm.getAddress());
        List<OrderDetail> orderDetailList=new ArrayList<>();
        try {
            orderDetailList=gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>(){}.getType());
        }catch (Exception e){
            e.printStackTrace();
            log.error("【对象转换】失败,string={}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderMasterDto.setOrderDetails(orderDetailList);
        if(orderMasterDto.getOrderDetails().size()==0){
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderMasterDto createResult = orderService.create(orderMasterDto);
        Map<String,String> map=new HashMap<>();
        map.put("orderId",createResult.getOrderId());
        return ResultVOUtil.success(map);
    }


    //订单列表
    @GetMapping("/list")
    public ResultVO<List<OrderMasterDto>> list(@RequestParam("openid") String openid,
                                               @RequestParam(value = "page",defaultValue = "0") Integer page,
                                               @RequestParam(value = "size",defaultValue = "10") Integer size){

        if(StringUtils.isEmpty(openid)){
            log.error("【订单查询列表】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest pageRequest=PageRequest.of(page,size);
        Page<OrderMasterDto> orderMasterDtoPage = orderService.findList(openid, pageRequest);
        return ResultVOUtil.success(orderMasterDtoPage);
    }

    //订单详情
    @GetMapping("/detail")
    public ResultVO<OrderMasterDto> detail(@RequestParam String openid,
                                           @RequestParam String orderId){
        //TODO 不安全的做法
        OrderMasterDto orderMasterDto = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(orderMasterDto);
    }
    //订单取消
    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam String openid,
                           @RequestParam String orderId){
        buyerService.cancelOrder(openid, orderId);
        return ResultVOUtil.success();
    }

}
