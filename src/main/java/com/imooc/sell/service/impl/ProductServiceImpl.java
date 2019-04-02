package com.imooc.sell.service.impl;

import com.imooc.sell.dao.ProductInfoDao;
import com.imooc.sell.dto.CartDto;
import com.imooc.sell.dto.OrderMasterDto;
import com.imooc.sell.entity.OrderMaster;
import com.imooc.sell.entity.ProductInfo;
import com.imooc.sell.enums.ProductStatusEnum;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exception.SellException;
import com.imooc.sell.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductInfoDao productInfoDao;
    @Override
    public ProductInfo findOne(String productId) {
        return productInfoDao.findById(productId).get();
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoDao.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoDao.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoDao.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDto> cartDtoList) {
        for(CartDto cartDto : cartDtoList){
            ProductInfo productInfo = productInfoDao.findById(cartDto.getProductId()).get();
            if(productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIT);
            }
            Integer stock=productInfo.getProductStock()+cartDto.getProductQuantity();
            productInfo.setProductStock(stock);
            productInfoDao.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDto> cartDtoList) {
        for (CartDto cartDto : cartDtoList) {
            ProductInfo productInfo = productInfoDao.findById(cartDto.getProductId()).get();
            if(productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIT);
            }
            Integer stock=productInfo.getProductStock()-cartDto.getProductQuantity();
            if(stock<0){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIT);
            }

            productInfo.setProductStock(stock);
            productInfoDao.save(productInfo);
        }
    }
}
