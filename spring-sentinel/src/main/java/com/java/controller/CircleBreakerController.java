package com.java.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.java.exception.CustomerHandler;
import com.java.web.base.common.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kevin
 * @date 2025/9/9 22:36
 */

@RestController
public class CircleBreakerController {

    public static final String SERVICE_URL = "http://springboot-nacos";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consumeService/{username}")
//    @SentinelResource(value = "fallback")
//    @SentinelResource(value = "fallback",fallback = "handlerFallback") // fallback只负责业务异常
//    @SentinelResource(value = "fallback",blockHandler = "blockHandler")  // blockHandler只负责sentinel控制台配置违规
    // 若blockHandler和fallback都配置，则blockHandler优先级更高
    @SentinelResource(value = "fallback",fallback = "handlerFallback",
            blockHandlerClass = CustomerHandler.class,blockHandler = "handleException1")
    public Res consumeService(@PathVariable String  username) {

        String result =restTemplate.getForObject(SERVICE_URL + "/load-balancer?username="+username, String.class);
        if ("kevin".equals(username)) {
           throw new NullPointerException("用户不存在");
        }
        return Res.success(result);
    }
    public Res handlerFallback(String username,Throwable e) {
        return Res.success("兜底异常fallback-handlerFallback异常"+e.getMessage());
    }
}
