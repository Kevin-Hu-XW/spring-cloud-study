package com.pojo;

import com.java.web.base.enums.EnumUdsError;

import java.io.Serializable;


public class Res<T> implements Serializable {

    private int code = 0;

    private String msg = "成功";

    private T data;

    private String traceId;

    public static Res failed(String msg) {
        Res<String> res = new Res();
        res.setMsg(msg);
        res.setCode(-1);
        return res;
    }

    public static Res failed(EnumUdsError udsError) {
        Res<String> res = new Res();
        res.setMsg(udsError.getMsg());
        res.setCode(udsError.getCode());
        return res;
    }

    public static Res failed(EnumUdsError udsError, String msg) {
        Res<String> res = new Res();
        res.setMsg(msg);
        res.setCode(udsError.getCode());
        return res;
    }

    public static Res success(Object data) {
        Res<Object> res = new Res();
        res.setData(data);
        return res;
    }

    public Res() {
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Res)) {
            return false;
        } else {
            Res<?> other = (Res)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getCode() != other.getCode()) {
                return false;
            } else {
                label49: {
                    Object this$msg = this.getMsg();
                    Object other$msg = other.getMsg();
                    if (this$msg == null) {
                        if (other$msg == null) {
                            break label49;
                        }
                    } else if (this$msg.equals(other$msg)) {
                        break label49;
                    }

                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                Object this$traceId = this.getTraceId();
                Object other$traceId = other.getTraceId();
                if (this$traceId == null) {
                    if (other$traceId != null) {
                        return false;
                    }
                } else if (!this$traceId.equals(other$traceId)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof Res;
    }

    @Override
    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + this.getCode();
        Object $msg = this.getMsg();
        result = result * 59 + ($msg == null ? 43 : $msg.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        Object $traceId = this.getTraceId();
        result = result * 59 + ($traceId == null ? 43 : $traceId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Res(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ", traceId=" + this.getTraceId() + ")";
    }
}