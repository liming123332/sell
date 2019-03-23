package com.imooc.sell.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * 类目表
 */
@Entity
//动态更新时间 对应mysql里面的更新时间自动更新时间
@DynamicUpdate
@Data
public class ProductCategory {
    //类目id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    //类目名字
    private String categoryName;
    //类目编号
    private Integer categoryType;
/*    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;*/

    public ProductCategory() {
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryType=" + categoryType +
                '}';
    }
}
