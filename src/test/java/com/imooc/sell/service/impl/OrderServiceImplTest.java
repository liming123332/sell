package com.imooc.sell.service.impl;

import com.imooc.sell.dao.OrderMasterDao;
import com.imooc.sell.dto.OrderMasterDto;
import com.imooc.sell.entity.OrderDetail;
import com.imooc.sell.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {
    @Autowired
    private IOrderService orderService;

    private final String buyerOpenId="ew3euwhd7sjw9diwkq";
    @Test
    public void create() {
        OrderMasterDto orderMasterDto=new OrderMasterDto();
        orderMasterDto.setBuyerName("沐桓");
        orderMasterDto.setBuyerAddress("宝安");
        orderMasterDto.setBuyerPhone("1231231233");
        orderMasterDto.setBuyerOpenid(buyerOpenId);

        List<OrderDetail> orderDetailList=new ArrayList<>();
        OrderDetail orderDetail=new OrderDetail();
        orderDetail.setProductId("123456");
        orderDetail.setProductQuantity(2);
        orderDetailList.add(orderDetail);
        
        orderMasterDto.setOrderDetails(orderDetailList);

        OrderMasterDto result = orderService.create(orderMasterDto);
        log.info("创建订单 resul={}",result);
    }

    @Test
    public void findOne(){
        OrderMasterDto dto = orderService.findOne("1553604473052891456");
        log.info("订单详情 dto={}",dto);
    }

    @Test
    public void findList(){
        PageRequest pageRequest=PageRequest.of(0,3);
        Page<OrderMasterDto> page = orderService.findList(buyerOpenId, pageRequest);
        List<OrderMasterDto> masterDtoList = page.getContent();
        masterDtoList.get(0).getBuyerName();
        log.info("内容 masterDtoList{}", masterDtoList);

    }

    @Test
    public void cancelOrder(){
        OrderMasterDto dto = orderService.findOne("1553604473052891456");
        OrderMasterDto result = orderService.cancel(dto);
        log.info("result={}",result);
    }

    @Test
    public void finish(){
        OrderMasterDto dto = orderService.findOne("1553604473052891456");
        OrderMasterDto result = orderService.finish(dto);
        log.info("result={}", result);
    }

    @Test
    public void paid(){
        OrderMasterDto dto = orderService.findOne("1553604473052891456");
        OrderMasterDto result = orderService.paid(dto);
        log.info("result={}", result);
    }

}