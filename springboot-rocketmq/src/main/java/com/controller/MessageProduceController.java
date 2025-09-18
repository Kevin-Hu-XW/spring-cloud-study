package com.controller;

import com.RocketApp;
import com.alibaba.fastjson.JSON;
import com.config.ExtRocketMQTemplate;
import com.dto.Person;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Kevin
 * @date 2023/8/7 15:28
 */
@RestController
@Log4j2
public class MessageProduceController {

//    @Resource(name = "extRocketMQTemplate")
//    private ExtRocketMQTemplate extRocketMQTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/send")
    public void sendOneWay(){
        Person person = new Person();
        person.setId("1");
        person.setName("kevin");

        String msg = JSON.toJSONString(person);
        //通过MessageBuilder构建消息
        Message<String> message = MessageBuilder.withPayload( msg ).build();
        String topic = "uds-test";
        //log.info(extRocketMQTemplate);
        rocketMQTemplate.convertAndSend(topic+":"+"test",message);
    }
}
