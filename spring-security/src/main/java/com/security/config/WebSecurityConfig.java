package com.security.config;

import com.alibaba.fastjson2.JSON;
import com.security.filter.JwtValidateFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;
import java.util.HashMap;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author Admin
 * @date 2024/7/8 20:34
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {



    @Autowired
    private JwtValidateFilter jwtValidateFilter;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //authorizeRequests()：开启授权保护
        //anyRequest()：对所有请求开启授权保护
        //authenticated()：已认证请求会自动被授权

        //开启授权保护
        http.authorizeRequests(
                authorize -> authorize
                        //具有USER_LIST权限的用户可以访问/user/list
                        //.requestMatchers(new AntPathRequestMatcher("/user/list")).hasAuthority("USER_LIST")
                        //具有USER_ADD权限的用户可以访问/user/add
                        //.requestMatchers(new AntPathRequestMatcher("/user/add")).hasAuthority("USER_ADD")
                        .requestMatchers(new AntPathRequestMatcher("/user/**")).hasRole("ADMIN")
                        //设置那些路径可以直接访问，不需要认证
                        //.requestMatchers("/user/login").permitAll()
                        //其他路径请求都需要认证
                        .anyRequest().authenticated())
                //表单授权方式
                //.formLogin(withDefaults());
                //自定义登录页
                /*
                    JWT 过滤器执行：首先检查是否有合法的 JWT token。
                    如果有，进行 JWT token 验证，并将认证信息存入 SecurityContextHolder。
                    如果没有，继续执行后续过滤器链。
                    表单登录处理：如果请求到达 UsernamePasswordAuthenticationFilter，并且用户尚未认证，则处理表单登录（用户名和密码认证）。
                 */
                .addFilterBefore(jwtValidateFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form ->{
                    // 指定登录页面路径
                    form.loginPage("/login").permitAll()
                            // spring-security默认的登录表单提交地址是/login，自定义登录路径
                            .loginProcessingUrl("/login")
                            .usernameParameter("username") //自定义表单用户名参数，默认是username
                            .passwordParameter("password") //自定义表单密码参数，默认是password
                            //.failureUrl("/user/login?error")
                            //.defaultSuccessUrl("/index")
                            .successHandler(myAuthenticationSuccessHandler)
                            .failureHandler(myAuthenticationFailureHandler); //登录失败的返回地址
                });
                //基本授权方式
                //.httpBasic(withDefaults());

        http.logout(logout ->{
            logout.logoutSuccessHandler(new MyLogoutSuccessHandler()); //注销成功时的处理
        });

        //错误处理
        http.exceptionHandling(exception  -> {
            //请求未认证的接口
            exception.authenticationEntryPoint(myAuthenticationEntryPoint);
            //请求未授权的接口
            exception.accessDeniedHandler((request, response, e)->{

                //创建结果对象
                HashMap<String,Object> result = new HashMap(16);
                result.put("code", -1);
                result.put("message", "没有权限");

                //转换成json字符串
                String json = JSON.toJSONString(result);

                //返回响应
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(json);
            });
        });

        //会话管理
        http.sessionManagement(session -> {
            session.maximumSessions(1)
                    //.expiredUrl("/login")
                    .expiredSessionStrategy(new MySessionInformationExpiredStrategy());
        });

        //跨域
        http.cors(withDefaults());
        //关闭csrf
        http.csrf().disable();
        return http.build();
    }




//    @Bean
//    public UserDetailsService userDetailsService() {
//        //创建基于内存用户信息管理器
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        //用manager管理userDetails对象
//        manager.createUser(
//                //创建userDetails对象，用于管理用户名、密码、用户角色、用户权限
//                User.withDefaultPasswordEncoder()
//                    .username("kevin")
//                    .password("1234")
//                    .roles("USER").build());
//        return manager;
//    }

}
