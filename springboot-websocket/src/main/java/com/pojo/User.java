package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Kevin
 * @date 2023/8/4 14:25
 */
@Data
@AllArgsConstructor
public class User implements Serializable {

    private String name;
}
