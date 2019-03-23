package com.imooc.sell.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 *  商品(包含类目)
 *  用于给前端展示使用
 * @param <T>
 */
@Data
public class ProductVO {
    //类目名称
    @JsonProperty(value = "name")//该注解可以按照指定的名字返还给前端
    private String categoryName;
    //类目状态
    @JsonProperty(value = "type")
    private Integer categoryType;
    //商品详情
    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
