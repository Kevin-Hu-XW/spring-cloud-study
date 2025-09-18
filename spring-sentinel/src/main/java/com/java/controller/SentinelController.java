package com.java.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.java.exception.CustomerHandler;
import com.java.web.base.common.Res;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kevin
 * @date 2025/9/7 10:25
 */

@RestController
public class SentinelController {

    @GetMapping("/sentinel")
    public String sentinel() {
        return "sentinel";
    }

    @GetMapping("/sentinel1")
    public String sentinel1() {
        return "sentinel1";
    }
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "handleHotKey")
    public Res testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2) {
        int a = 10/0;
        return Res.success("testHotKey");
    }
    // 热点Key的兜底方法
    public Res handleHotKey(String p1, String p2, BlockException exception) {
        return Res.success("handleHotKey:"+ exception.getClass().getCanonicalName());
    }

    @GetMapping("/rateLimit/byUrl")
    @SentinelResource(value = "byUrl")
    public Res byUrl(@RequestParam(value = "p1",required = false) String p1,
                          @RequestParam(value = "p2",required = false) String p2) {

        return Res.success("按Url限流");
    }


    @GetMapping("/customerHandler")
    @SentinelResource(value = "customerHandler",
            blockHandler = "handleException1",
            blockHandlerClass = CustomerHandler.class)
    public Res customerHandler(@RequestParam(value = "p1",required = false) String p1,
                               @RequestParam(value = "p2",required = false) String p2) {
        return Res.success("customerHandler");
    }
}
