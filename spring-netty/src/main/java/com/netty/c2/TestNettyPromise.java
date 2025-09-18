package com.netty.c2;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutionException;

/**
 * @author Kevin
 * @date 2024/10/11 13:35
 */
@Log4j2
public class TestNettyPromise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1、准备EventLoop对象
        EventLoop eventLoop = new NioEventLoopGroup().next();
        // 2、可以主动创建promise，结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(()->{
            log.debug("执行计算");
            try {
                Thread.sleep(1000);
                int i = 1/0;
                promise.setSuccess(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);
            }

        }).start();


        // 3、接收结果线程
        log.debug("等待结果....");
        log.debug("结果是：" + promise.get());
    }
}
