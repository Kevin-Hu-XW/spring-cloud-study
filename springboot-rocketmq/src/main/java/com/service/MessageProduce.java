package com.service;

import com.alibaba.fastjson.JSON;
import com.config.ExtRocketMQTemplate;
import com.dto.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

/**
 * @author Kevin
 * @date 2023/8/7 9:22
 */
public class MessageProduce {

    @Resource(name = "extRocketMQTemplate")
    private ExtRocketMQTemplate extRocketMQTemplate;


    public void sendOneWay(){
        Person person = new Person();
        person.setId("1");
        person.setName("kevin");

        String msg = JSON.toJSONString(person);

        String topic = "uds";


    }
}
