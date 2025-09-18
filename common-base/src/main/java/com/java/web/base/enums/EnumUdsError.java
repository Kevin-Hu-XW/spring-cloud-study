package com.java.web.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumUdsError {
    //内置错误 10000开始
    SYSTEM_INTERNAL_ERROR(10001,"系统内部错误"),
    DB_ERROR(10002,"数据库错误"),
    PARAM_ERROR(10003,"参数错误"),
    //自定义错误20000开始
    USER_ACCOUNT_ERROR(20000,"用户名密码错误"),
    USER_ROLE_ERROR(20001,"用户没有权限"),


    //其它40000开始
    UN_KNOW_ERROR(40004,"未知错误");
    private final int code;
    private final String msg;
}
