package com.java.web.base.common;


import com.java.web.base.enums.EnumUdsError;
import lombok.Data;

import java.io.Serializable;

@Data
public class Res<T> implements Serializable {
    private int code = 0;
    private String msg = "成功";
    private T data;

    public static Res failed(String msg){
        Res<String> res = new Res<>();
        res.setMsg(msg);
        res.setCode(Constant.Error);
        return res;
    }

    public static Res failed(EnumUdsError udsError){
        Res<String> res = new Res<>();
        res.setMsg(udsError.getMsg());
        res.setCode(udsError.getCode());
        return res;
    }

    public static Res failed(EnumUdsError udsError,String msg){
        Res<String> res = new Res<>();
        res.setMsg(msg);
        res.setCode(udsError.getCode());
        return res;
    }

    public static Res success(Object data){
        Res<Object> res = new Res<>();
        res.setData(data);
        return res;
    }
}
