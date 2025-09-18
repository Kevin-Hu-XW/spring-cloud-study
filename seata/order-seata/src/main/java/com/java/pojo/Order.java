package com.java.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kevin
 * @date 2025/9/18 21:13
 */
@Data
public class Order implements Serializable {

    private Long id;
    private Long userId;
    private String commodityCode;
    private Integer count;
    private Long money;
    private int status;
}
