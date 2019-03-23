package com.imooc.sell.service.impl;

import com.imooc.sell.entity.ProductInfo;
import com.imooc.sell.service.IProductService;
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
public class ProductServiceImplTest {
    @Autowired
    private IProductService productService;

    @Test
    public void findOne() {
        ProductInfo productInfo = productService.findOne("123456");
        System.out.println(productInfo);
    }

    @Test
    public void findUpAll() {
        List<ProductInfo> list = productService.findUpAll();
        for (ProductInfo productInfo : list) {
            System.out.println(productInfo);
        }

    }

    @Test
    public void findAll() {
        PageRequest pageRequest=PageRequest.of(0,2);
        Page<ProductInfo> page = productService.findAll(pageRequest);
        System.out.println(page.getContent().get(0).getProductName());
    }

    @Test
    public void save() {
        ProductInfo productInfo=new ProductInfo();
        productInfo.setProductId("123457");
        productInfo.setProductName("炒米粉");
        productInfo.setProductPrice(new BigDecimal(7.2));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("好吃的米粉");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(3);
        productService.save(productInfo);
    }
}