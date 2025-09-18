package com.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Kevin
 * @date 2024/8/24 16:13
 */
@Data
@Log4j2
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {



    private String secret ;
    private Long expire ;
    private String header ;

    public  String createToken(String subject) {
        // 获取当前日期
        Date currentDate = new Date();
        // 计算令牌过期日期，通过给定的expire时间（秒）转换为毫秒
        Date expireDate = new Date(currentDate.getTime() + expire * 1000);

        // 使用Jwts.builder()构建JWT令牌
        return Jwts.builder()
                // 设置令牌类型为JWT
                .setHeaderParam("typ","JWT")
                // 设置令牌的主题
                .setSubject(subject)
                // 设置令牌的签发时间
                .setIssuedAt(currentDate)
                // 设置令牌的过期时间
                .setExpiration(expireDate)
                // 使用HS512算法和密钥签名令牌
                .signWith(SignatureAlgorithm.HS512,secret)
                // 将令牌压缩为紧凑的字符串形式
                .compact();
    }

    /**
     * 获取token注册信息
     * @param token
     * @return
     */
    public Claims getTokenClaim(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.error("token解析失败");
            return null;
        }
    }


    /**
     * 验证token是否过期失效
     * @param expirationTime
     * @return
     */
    public boolean isTokenExpired (Date expirationTime) {
        return expirationTime.before(new Date());
    }

    /**
     * 获取token失效时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getTokenClaim(token).getExpiration();
    }

    /**
     * 从token中获取用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        return getTokenClaim(token).getSubject();
    }

    /**
     * 获取token创建时间
     * @param token
     * @return
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getTokenClaim(token).getIssuedAt();
    }

}
