package com.imooc.sell.service;

import com.imooc.sell.entity.ProductCategory;

import java.util.List;

public interface ICategroyService {
    ProductCategory findOne(Integer categoryId);
    List<ProductCategory> findAll();
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
    ProductCategory save(ProductCategory productCategory);
}
