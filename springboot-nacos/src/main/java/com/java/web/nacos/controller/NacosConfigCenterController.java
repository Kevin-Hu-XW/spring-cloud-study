package com.java.web.nacos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author Kevin
 * @date 2023/4/5 16:55
 */
/**
 * 配置发布后，动态刷新配置
 * @author Admin
 */
@RefreshScope
@RestController
@RequestMapping("/api/user")
public class NacosConfigCenterController {

    private static final Random RANDOM = new Random();
    /**
     * 使用原生注解@Value()导入配置
     */
    @Value("${props.desSecret}")
    private String id;

    @GetMapping("/get")
    public String get() {
        return "获取的config配置下的值为" + id;
    }


    @GetMapping("/info")
    public String getUserInfo() {
        // 模拟随机失败
//        if (RANDOM.nextBoolean()) {
//            throw new RuntimeException("服务异常");
//        }
//        return "用户信息：张三";
        throw new RuntimeException("服务异常");
    }
}
