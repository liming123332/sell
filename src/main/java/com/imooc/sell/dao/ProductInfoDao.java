package com.imooc.sell.dao;

import com.imooc.sell.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoDao extends JpaRepository<ProductInfo, String> {
    //通过商品的状态来查询商品信息
    List<ProductInfo> findByProductStatus(Integer productStatus);

}
