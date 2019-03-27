package com.imooc.sell.dao;

import com.imooc.sell.entity.OrderDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailDaoTest {

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Test
    public void test1(){
        OrderDetail orderDetail=new OrderDetail();
        orderDetail.setDetailId("12345678");
        orderDetail.setOrderId("123456");
        orderDetail.setProductIcon("http://xxx.jpg");
        orderDetail.setProductId("123456");
        orderDetail.setProductName("皮蛋粥");
        orderDetail.setProductPrice(new BigDecimal(3.2));
        orderDetail.setProductQuantity(2);
        orderDetailDao.save(orderDetail);
    }
    @Test
    public void test2(){
        List<OrderDetail> orderDetailList = orderDetailDao.findByOrderId("123456");
        for (OrderDetail orderDetail : orderDetailList) {
            System.out.println(orderDetail.getProductName());
        }

    }
}