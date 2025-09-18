package com.security.controller;

import com.security.entity.User;
import com.security.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Admin
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    public UserService userService;


    @GetMapping("/list")
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    public List<User> getList(){
        return userService.list();
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加用户", notes = "添加用户")
    public void add(@RequestBody User user){
        userService.saveUserDetails(user);
    }
}