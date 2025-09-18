package com.java.web.hello.service;

import com.java.web.hello.aop.Person;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kevin
 * @date 2023/3/16 17:25
 */
@RestController
public class Hello {

    @RequestMapping("/test")
    public String test(){
        return new Person("kevin",18).toString();
    }
}
