package com.java.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kevin
 * @date 2025/9/18 21:16
 */
@Data
public class Storage implements Serializable {

    private Long id;
    /**
     * 商品编号
     */
    private String commodityCode;
    /**
     * 库存数量
     */
    private Integer count;
}
