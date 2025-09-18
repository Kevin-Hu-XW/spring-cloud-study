package com.security.interceptor;


import com.security.util.JwtConfig;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Kevin
 * @date 2024/8/24 17:21
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {


    @Resource
    private JwtConfig jwtConfig;

    /**
     * 在请求处理之前执行的逻辑
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String url = request.getRequestURI();
        // 地址过滤，返回 true 表示继续处理请求，返回 false 则中断请求
        if (url.contains("/login")){
            return true;
        }
        //token 校验
        String token = request.getHeader(jwtConfig.getHeader());
        if (!StringUtils.hasText(token)){
            token = request.getParameter(jwtConfig.getHeader());
        }
        Claims claims = null;
        try{
            claims = jwtConfig.getTokenClaim(token);
            if (claims == null || jwtConfig.isTokenExpired(claims.getExpiration())){
                throw new RuntimeException("token已过期，请重新登录！");
            }
        }
        catch (Exception e){
            throw new RuntimeException("token已过期，请重新登录！");
        }
        //将用户信息放入 request
        String userName = jwtConfig.getUserNameFromToken(token);
        return true;
    }
}
