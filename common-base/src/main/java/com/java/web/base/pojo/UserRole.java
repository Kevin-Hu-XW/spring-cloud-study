package com.java.web.base.pojo;



import com.java.web.base.common.CommonFields;
import lombok.Data;

import java.util.Date;

@Data
public class UserRole extends CommonFields {

    private Long id;

    private String roleName;

    private Integer roleType;

    private Date createdAt;

    private Date updatedAt;

    private Long userId;

    private String roleDesc;
}
