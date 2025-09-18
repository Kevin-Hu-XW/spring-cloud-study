//package com.java.web.hello.service;
//
//import jdk.nashorn.internal.runtime.logging.Logger;
//import lombok.extern.log4j.Log4j;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author Kevin
// * @date 2023/3/12 22:22
// */
//@RestController
//@EnableAutoConfiguration
//@ComponentScan("com.java.web.hello")
//public class HelloService {
//    @RequestMapping("/hello")
//    public String index() {
//        /*
//            @RestController注解改为@Controller会转发到"Hello World"页面
//         */
//        //log.info("###hello word");
//        return "Hello World";
//    }
////    public static void main(String[] args) {
////        //启动类会默认整合Tomcat 8080端口
////        SpringApplication.run(HelloService.class, args);
////    }
//
//}
