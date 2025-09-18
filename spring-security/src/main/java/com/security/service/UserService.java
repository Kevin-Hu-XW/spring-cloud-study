package com.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.security.entity.User;

/**
 * @author Kevin
 * @date 2024/7/14 15:37
 */
public interface UserService extends IService<User> {

    void saveUserDetails(User user);
}
