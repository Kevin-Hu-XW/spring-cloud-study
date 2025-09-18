package com.java.web.base.common;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CommonFields implements Serializable {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Integer status;
}
