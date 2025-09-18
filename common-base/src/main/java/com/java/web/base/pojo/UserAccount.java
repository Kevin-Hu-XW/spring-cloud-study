package com.java.web.base.pojo;


import com.java.web.base.common.CommonFields;
import lombok.Data;

@Data
public class UserAccount extends CommonFields {

    private Boolean verified;

    private String password;

    private String passwordExt;

    private String verifyCode;
    private Integer regType;
    private Integer passwordChanged;

}
