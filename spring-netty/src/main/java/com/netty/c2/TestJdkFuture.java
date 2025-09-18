package com.netty.c2;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.*;

/**
 * @author Kevin
 * @date 2024/10/11 11:14
 */
@Log4j2
public class TestJdkFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1、线程池
        ExecutorService service = Executors.newFixedThreadPool(2);
        // 2、提交任务
        Future<Integer> future = service.submit(new Callable<Integer>() {
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
