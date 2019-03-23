package com.imooc.sell.service.impl;

import com.imooc.sell.entity.ProductCategory;
import com.imooc.sell.service.ICategroyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {
    @Autowired
    private ICategroyService categroyService;
    @Test
    public void findOne() {
        ProductCategory category = categroyService.findOne(1);
        System.out.println(category);
    }

    @Test
    public void findAll() {
        List<ProductCategory> list = categroyService.findAll();
        for (ProductCategory productCategory : list) {
            System.out.println(productCategory);
        }
    }

    @Test
    public void findByCategoryTypeIn() {
        List<ProductCategory> list = categroyService.findByCategoryTypeIn(Arrays.asList(1, 10));
        for (ProductCategory productCategory : list) {
            System.out.println(productCategory);
        }
    }

    @Test
    public void save() {
        ProductCategory productCategory=new ProductCategory();
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(3);
        categroyService.save(productCategory);
    }
}