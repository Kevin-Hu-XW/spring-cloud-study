package com.java.web.base.pojo;


import com.java.web.base.common.CommonFields;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class User extends CommonFields {

    private String email;
    private String userName;
    private String tel;
    private String company;
    private String signature;
    private String job;
    private Boolean gender;
    private Date birthDay;
    private Integer avatar;
    private String showName;
    private String firstName;
    private String lastName;
    private String address;




    //用户角色
    private List<UserRole> userRoles;
    //用户账号
    private UserAccount account;
}
