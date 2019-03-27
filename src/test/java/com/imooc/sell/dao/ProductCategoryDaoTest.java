package com.imooc.sell.dao;

import com.imooc.sell.entity.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryDaoTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Test
    public void test1(){
        ProductCategory productCategory = productCategoryDao.findById(1).get();
        System.out.println(productCategory.getCategoryName());
    }
    @Test
    public void inserTest(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(2);
        productCategoryDao.save(productCategory);
    }

    @Test
    public void findByCategoryTypeInTest(){
        List<Integer> list= Arrays.asList(1,10);
        List<ProductCategory> productCategoryList = productCategoryDao.findByCategoryTypeIn(list);
        for (ProductCategory productCategory : productCategoryList) {
            System.out.println(productCategory);
        }
    }

}