package com.kevin.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author Admin
 */
@Configuration
public class RateLimiterConfig {

    @Bean("ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        System.out.println(">>> 注册 ipKeyResolver Bean");
        return exchange -> {
            String ip = Objects.requireNonNull(exchange.getRequest().getRemoteAddress())
                    .getAddress()
                    .getHostAddress();
            System.out.println("限流 KeyResolver 提取 IP：" + ip);
            return Mono.just(ip);
        };
    }
}
