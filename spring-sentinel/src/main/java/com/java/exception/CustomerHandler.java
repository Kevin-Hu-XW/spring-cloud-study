package com.java.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.java.web.base.common.Res;

/**
 * @author Kevin
 * @date 2025/9/9 21:59
 */
public class CustomerHandler {
    public static Res handleException1(String username, BlockException exception) {
        return Res.success("handleException1:"+ exception.getClass().getCanonicalName());
    }
    public static Res handleException2(BlockException exception) {
        return Res.success("handleException2:"+ exception.getClass().getCanonicalName());
    }
    public static Res handleException3(BlockException exception) {
        return Res.success("handleException3:"+ exception.getClass().getCanonicalName());
    }
}
