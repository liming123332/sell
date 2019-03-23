package com.imooc.sell.VO;

import lombok.Data;

import java.util.List;

/**
 * http请求返回的最外层对象
 *
 */
@Data
public class ResultVO<T> {

    /**错误码**/
    private Integer code;
    /**提示信息**/
    private String msg;
    /**返回数据**/
    private T data;
}
