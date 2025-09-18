package com.java.web.mybatis.service;

import com.java.web.base.pojo.User;
import com.java.web.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kevin
 * @date 2023/3/16 22:38
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public User queryUserById(Long id){
        return this.userMapper.queryUserById(id);
    }
}
