package com.imooc.sell.service;

import com.imooc.sell.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {

    ProductInfo findOne(String productId);

    //查询所有在架的商品列表
    List<ProductInfo> findUpAll();

    //查询所有商品信息 传入分页信息
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //加库存
    //减库存



}
