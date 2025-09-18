package com.java.web.hello.controller;

import com.java.web.hello.aop.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @date 2023/3/13 22:17
 */
@Slf4j
@RestController
public class FreemarkerIndexController {

    @RequestMapping("/hello/freemarkerIndex")
    public String freemarkerIndex(Map<String,Object> result){
        //转发到页面展示name value
        result.put("name","kevin");
        result.put("sex", 0);
        List<String> listResult = new ArrayList<String>();
        listResult.add("zhangsan");
        listResult.add("lisi");
        listResult.add("mayikt");
        result.put("listResult", listResult);

        return "freemarkerIndex";
    }

    @RequestMapping("/hello/test1")
    public String getPerson(){
        throw new RuntimeException("xxxx");
    }

    @RequestMapping("/hello/test")
    public String test() throws InterruptedException {
//        Thread.sleep(3000);
        return new Person("kevin",18).toString();
    }

}
