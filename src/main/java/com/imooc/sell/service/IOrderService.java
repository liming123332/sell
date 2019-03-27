package com.imooc.sell.service;

import com.imooc.sell.dto.OrderMasterDto;
import com.imooc.sell.entity.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {

    //创建订单
    OrderMasterDto create(OrderMasterDto orderMasterDto);

    //查询单个订单
    OrderMasterDto findOne(String orderId);

    //查询订单列表
    Page<OrderMasterDto> findList(String openId, Pageable pageable);

    //取消订单
    OrderMasterDto cancel(OrderMasterDto orderMasterDto);

    //完结订单
    OrderMasterDto finish(OrderMasterDto orderMasterDto);

    //支付订单
    OrderMasterDto paid(OrderMasterDto orderMasterDto);




}
