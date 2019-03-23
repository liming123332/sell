package com.imooc.sell.controller;

import com.imooc.sell.VO.ProductInfoVO;
import com.imooc.sell.VO.ProductVO;
import com.imooc.sell.VO.ResultVO;
import com.imooc.sell.entity.ProductCategory;
import com.imooc.sell.entity.ProductInfo;
import com.imooc.sell.service.ICategroyService;
import com.imooc.sell.service.IProductService;
import com.imooc.sell.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {
 *     "code": 0,
 *     "msg": "成功",
 *     "data": [
 *         {
 *             "name": "热榜",
 *             "type": 1,
 *             "foods": [
 *                 {
 *                     "id": "123456",
 *                     "name": "皮蛋粥",
 *                     "price": 1.2,
 *                     "description": "好吃的皮蛋粥",
 *                     "icon": "http://xxx.com",
 *                 }
 *             ]
 *         },
 *     ]
 * }
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategroyService categroyService;

    @RequestMapping(value="/list",method=RequestMethod.GET)
    public ResultVO list(){
        //查询所有上架的商品
        List<ProductInfo> productInfoList = productService.findUpAll();
        //查询类目 一次性
        List<Integer> categoryTypeList=new ArrayList<>();
        for (ProductInfo productInfo : productInfoList) {
            categoryTypeList.add(productInfo.getCategoryType());
        }
        List<ProductCategory> productCategoryList = categroyService.findByCategoryTypeIn(categoryTypeList);
        //数据拼装
        List<ProductVO> productVOList=new ArrayList<>();
        //将商品类目信息存到需要展示到前台的VO对象当中
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO=new ProductVO();
            BeanUtils.copyProperties(productCategory, productVO);

            List<ProductInfoVO> productInfoVOList =new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO=new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }

}
