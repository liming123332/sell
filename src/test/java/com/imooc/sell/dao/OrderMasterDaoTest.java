package com.imooc.sell.dao;

import com.imooc.sell.entity.OrderMaster;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterDaoTest {
    @Autowired
    private OrderMasterDao orderMasterDao;
    @Test
    public void test1(){
        OrderMaster orderMaster=new OrderMaster();
        orderMaster.setOrderId("123457");
        orderMaster.setBuyerName("卢涛");
        orderMaster.setBuyerPhone("12345678911");
        orderMaster.setBuyerOpenid("110110");
        orderMaster.setBuyerAddress("黄泉");
        orderMaster.setOrderAmount(new BigDecimal(2.5));
        orderMasterDao.save(orderMaster);
    }

    @Test
    public void test2(){
        PageRequest pageRequest=PageRequest.of(0,2);
        Page<OrderMaster> page = orderMasterDao.findByBuyerOpenid("110110", pageRequest);
        List<OrderMaster> orderMasterList = page.getContent();
        for (OrderMaster orderMaster : orderMasterList) {
            System.out.println(orderMaster.getBuyerName());
        }
    }

}