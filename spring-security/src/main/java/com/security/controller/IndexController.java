package com.security.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Kevin
 * @date 2024/8/10 15:59
 */

@RestController
public class IndexController {

    @GetMapping("/login")
    @ApiOperation(value = "登录", notes = "登录")
    public ModelAndView login(){
        return new ModelAndView("login");
    }

    @GetMapping("/index")
    @ApiOperation(value = "首页", notes = "首页")
    public String index(){
        return "欢迎登录！";
    }
}
