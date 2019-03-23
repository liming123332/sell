package com.imooc.sell.enums;

import lombok.Getter;

/**
 * 商品状态的枚举
 */
@Getter
public enum ProductStatusEnum {
    UP(0,"在架"),//0表示上架
    DOWN(1,"下架");//1表示已经下架了
    private Integer code;

    private String message;

    ProductStatusEnum(Integer code,String message){
        this.code=code;
        this.message=message;
    }
}
