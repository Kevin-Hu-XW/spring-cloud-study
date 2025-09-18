package com.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.security.config.DBUserDetailsManager;
import com.security.entity.User;
import com.security.mapper.UserMapper;
import com.security.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Kevin
 * @date 2024/7/14 15:38
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private DBUserDetailsManager dbUserDetailsManager;



    @Override
    public void saveUserDetails(User user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withDefaultPasswordEncoder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
        dbUserDetailsManager.createUser(userDetails);

    }

}
