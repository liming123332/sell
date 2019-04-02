package com.imooc.sell.service;

import com.imooc.sell.dto.OrderMasterDto;
import com.imooc.sell.entity.OrderMaster;

public interface IBuyerService {

    //查询一个订单
    OrderMasterDto findOrderOne(String openid,String orderId);

    //取消一个订单
    OrderMasterDto cancelOrder(String openid,String orderId);
}
