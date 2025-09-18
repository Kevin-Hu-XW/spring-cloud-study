package com.java.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kevin
 * @date 2025/9/18 21:07
 */
@Data
public class Account implements Serializable {
    private Long id;

    private Long userId;

    private Long money;
}
