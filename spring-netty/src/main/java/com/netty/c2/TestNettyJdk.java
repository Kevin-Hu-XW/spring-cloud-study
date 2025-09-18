package com.netty.c2;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author Kevin
 * @date 2024/10/11 11:29
 */
@Log4j2
public class TestNettyJdk {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        EventLoop executors = group.next();
        Future<Integer> future = executors.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("执行计算");
                Thread.sleep(1000);
                return 50;
            }
        });

        //3、主线程通过future来获取结果
        log.debug("等待结果");
        log.debug("结果是：{}", future.get());
    }
}
