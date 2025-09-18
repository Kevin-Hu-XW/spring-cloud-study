package com.security.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.security.entity.User;
import com.security.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Kevin
 * @date 2024/7/14 16:21
 */
@Component
public class DBUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {


    @Resource
    private UserMapper userMapper;
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }


    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*
            根据用户名从数据库中查询用户信息
         */
        QueryWrapper<User> userqueryWrapper = new QueryWrapper<User>();
        userqueryWrapper.eq("username",username);
        User user = this.userMapper.selectOne(userqueryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles("ADMIN")
                    .build();
            //            Collection<GrantedAuthority> authorities = new ArrayList<>();
//            // 模拟角色->给当前登录的用户，赋予默认角色
//            //authorities.add(()->"USER_LIST");
//            authorities.add(()->"USER_ADD");
//            return new org.springframework.security.core.userdetails.User(user.getUsername(),
//                    user.getPassword(),
//                    user.getEnabled(),
//                    true,
//                    true,
//                    true,
//                    authorities);
//        }
        }
    }
    /**
     * 创建用户
     * @param userDetails
     */
    @Override
    public void createUser(UserDetails userDetails) {
        User user = new User();
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setEnabled(true);
        userMapper.insert(user);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }


}
