package com.service;


import com.config.WebClientConfig;
import com.java.web.base.common.Res;
import com.java.web.base.pojo.User;
import com.java.web.base.req.ReqUser;
import com.pojo.Person;
import com.pojo.ReqTest;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author Kevin
 * @date 2023/8/22 14:44
 */
@Service
@Log4j2
public class TestService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RestTemplate restTemplate;

    public void testService(ReqTest reqTest){
        reqTest.setName("kevin");
    }

    public void demo(){
        ReqUser reqUser = new ReqUser();
        reqUser.setUserId(1L);
        /**
         * 第 1步到第 4 步是异步执行的，即发送请求和处理响应都是非阻塞的。
         * 因此，代码会依次执行，但不会等待请求的完成，而是在收到响应时执行相应的操作。
         *
         * .subscribe()：
         * .subscribe() 用于订阅 Mono 或者 Flux 对象，触发数据流的执行，并在数据可用时执行订阅者提供的操作。
         * 你可以在 .subscribe() 中定义一个消费者（Consumer），一旦数据可用，就会执行这个消费者，即执行日志打印操作
         * .subscribe() 方法触发了 Mono 对象的订阅，这会开始发送 HTTP 请求并等待响应。但是，这个过程是异步的，
         * subscribe() 方法会立即返回，并不会等待请求发送完成或者响应接收完成。
         * 这意味着，.subscribe() 方法执行完并不代表 HTTP 请求已经发送完成或者响应已经接收到。
         */
        webClientBuilder.build()
                //1、使用 WebClient 发送一个 POST 请求，请求体为上面创建的 ReqUser 对象。
                .post()
                .uri("http://localhost:8888/basic-info")
                .bodyValue(reqUser)
                //2、当请求完成并收到响应时，使用 .retrieve() 方法获取响应体
                .retrieve()
                //3、并使用 .bodyToMono() 方法将响应体映射为一个 Mono 对象。
                .bodyToMono(new ParameterizedTypeReference<Res<User>>(){})
                .timeout(Duration.ofSeconds( 2 ))
                //当请求过程中发生错误时，.doOnError() 会捕获这个错误，并执行定义在其中的操作
                .doOnError(e -> log.error( "######"+e.getMessage()))
                //.onErrorResume() 会返回一个新的 Mono 对象，其值为 "异常值"。
                // 这意味着，如果请求过程中发生了错误，响应体将不再是之前定义的 Res<User> 类型，而是一个包含了 "异常值" 的 Mono 对象
                .onErrorResume(e->Mono.just(new Res<User>()))
                //4、使用 .subscribe() 方法订阅 Mono 对象，触发请求发送
                .subscribe(res -> {
                    log.info("#########"+res.getData());
                });

    }

    public void resttemplateDemo(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        ReqUser reqUser = new ReqUser();
        reqUser.setUserId(1L);
        HttpEntity<ReqUser> request = new HttpEntity<>(reqUser, httpHeaders);
        ResponseEntity<Res> response = this.restTemplate.postForEntity("http://localhost:8888/basic-info", request,Res.class);
        log.info(response.getBody().getData().toString());
    }
}
