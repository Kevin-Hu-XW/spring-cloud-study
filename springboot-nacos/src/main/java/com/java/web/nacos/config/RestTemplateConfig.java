package com.java.web.nacos.config;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate工具类，主要用来提供RestTemplate对象
 */
//加上这个注解作用，可以被Spring扫描
@Configuration
public class RestTemplateConfig {
    /**
     * 创建RestTemplate对象，将RestTemplate对象的生命周期的管理交给Spring
     * 加上@LoadBalanced注解实现本地负载均衡
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
       // RestTemplate restTemplate = new RestTemplate();
       //设置中文乱码问题方式一
       // restTemplate.getMessageConverters().add(1,new StringHttpMessageConverter(Charset.forName("UTF-8")));
       // 设置中文乱码问题方式二
       // restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文编码
        return new RestTemplate();
    }
}
