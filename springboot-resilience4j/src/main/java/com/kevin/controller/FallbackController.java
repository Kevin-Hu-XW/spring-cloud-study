package com.kevin.controller;

import com.java.web.base.common.Res;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class FallbackController {

    @RequestMapping("/fallback")
    public Res<String> fallback() {

        return Res.failed("服务已降级，请稍后再试");
    }

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @GetMapping("/gateway-cbs")
    public List<String> getCircuitBreakers() {
        return circuitBreakerRegistry.getAllCircuitBreakers()
                .map(CircuitBreaker::getName)
                .map(name -> name + ": " + circuitBreakerRegistry.circuitBreaker(name).getState())
                .collect(Collectors.toList());
    }

}
