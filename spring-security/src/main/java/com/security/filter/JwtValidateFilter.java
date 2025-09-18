package com.security.filter;

import com.security.config.DBUserDetailsManager;
import com.security.config.MyAuthenticationEntryPoint;
import com.security.exception.JwtAuthenticationException;
import com.security.util.JwtConfig;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Kevin
 * @date 2024/9/12 15:29
 * OncePerRequestFilter 是 Spring 框架中用于过滤器实现的一个抽象类。
 * 它确保每个请求只通过该过滤器一次，不管该请求在应用程序的处理过程中被转发了多少次。
 */
@Component
public class JwtValidateFilter extends OncePerRequestFilter {


    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Resource
    private DBUserDetailsManager dbUserDetailsManager;


    /**
     * 校验token的合法性
     * @param request 请求
     * @param response 响应
     * @param filterChain 过滤器
     * @throws ServletException 异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String url = request.getRequestURI();
            // 放行登录请求
            if ("/login".equals(url)) {
                filterChain.doFilter(request, response);
                return;
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
                    throw new RuntimeException("token已过期");
                }
            }
            catch (Exception e){
                throw new RuntimeException("token已过期");
            }
            //将用户信息放入 request
            String userName = jwtConfig.getUserNameFromToken(token);
            //在 JWT 的日常使用场景中，认证逻辑是在用户第一次登录时发生的，之后的请求只需验证令牌，因此大多数情况下不需要调用 AuthenticationManager
            UserDetails userDetails = this.dbUserDetailsManager.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request,response);
        }
        catch (Exception e){
            // 捕获异常并调用 AuthenticationEntryPoint
            myAuthenticationEntryPoint.commence(request,response, new JwtAuthenticationException(e.getMessage()));
        }
    }
}
