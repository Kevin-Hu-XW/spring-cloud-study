package com.security.config;

import com.alibaba.fastjson2.JSON;
import com.java.web.base.common.Constant;
import com.java.web.base.common.Res;
import com.security.util.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Admin
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //获取用户身份信息
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String jwt = jwtConfig.createToken(userDetails.getUsername());
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Authorization",jwt);
        Res<String> res = new Res<>();
        res.setCode(Constant.Success);
        res.setData(jwt);
        res.setMsg("登录成功");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(JSON.toJSONBytes(res));

    }
}