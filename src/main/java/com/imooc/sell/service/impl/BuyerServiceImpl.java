package com.imooc.sell.service.impl;

import com.imooc.sell.dto.OrderMasterDto;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.service.IBuyerService;
import com.imooc.sell.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements IBuyerService {

    @Autowired
    private IOrderService orderService;

    @Override
    public OrderMasterDto findOrderOne(String openid, String orderId) {
        checkOrderDto(openid, orderId);
        OrderMasterDto orderMasterDto = orderService.findOne(orderId);
        return orderMasterDto;
    }

    @Override
    public OrderMasterDto cancelOrder(String openid, String orderId) {
        checkOrderDto(openid, orderId);
        OrderMasterDto orderMasterDto = orderService.findOne(orderId);
        orderService.cancel(orderMasterDto);
        return null;
    }

    public OrderMasterDto checkOrderDto(String openid, String orderId){
        OrderMasterDto orderMasterDto = orderService.findOne(orderId);
        if(orderMasterDto==null){
            return null;
        }
        if(!orderMasterDto.getBuyerOpenid().equals(openid)){
            log.error("【查询订单】订单的openid不一致,openid={},orderMasterDto={}", openid,orderMasterDto);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderMasterDto;
    }

}
