package com.java.web.mybatis.controller;

import com.java.web.base.common.Res;
import com.java.web.base.pojo.User;
import com.java.web.base.req.ReqUser;
import com.java.web.mybatis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kevin
 * @date 2023/3/16 22:39
 */
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Value("${server.port}")
    private String port;
    /**
     * 生产者    消费者在Nacos服务
     * @param id
     * @return
     */
    @RequestMapping("/test/{id}")
    public String queryUser(@PathVariable("id") Long id){
        log.info("id:{}",id);

        return this.userService.queryUserById(id).getUserName()+":"+port;
    }


    @RequestMapping("/basic-info")
    public Res<User> queryUser(@RequestBody ReqUser reqUser){
        Res<User> res=  new Res<>();
        log.info("userId:{}",reqUser.getUserId());
         res.setData(this.userService.queryUserById(reqUser.getUserId()));
        return res;
    }
}
