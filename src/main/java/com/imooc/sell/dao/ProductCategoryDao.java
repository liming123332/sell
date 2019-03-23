package com.imooc.sell.dao;

import com.imooc.sell.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryDao extends JpaRepository<ProductCategory,Integer> {
    //通过多个类目编号查询商品类目
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
