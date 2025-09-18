package com.netty.c2;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @date 2024/10/7 20:55
 */
@Log4j2
public class TestEventLoop {

    public static void main(String[] args) throws InterruptedException {
        /*
         * private static final int DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
         * protected MultithreadEventLoopGroup(int nThreads, Executor executor, Object... args) {
         *         super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, executor, args);
         *     }
         */
        //创建事件循环组，可以处理I/O事件、普通任务、定时任务等，参数如果什么都不传，会采用默认的线程数DEFAULT_EVENT_LOOP_THREADS
        EventLoopGroup group = new NioEventLoopGroup(2);
        //只能处理普通任务、定时任务
        //EventLoopGroup group2 = new DefaultEventLoop();
        log.debug(NettyRuntime.availableProcessors());
        //获取下一个事件循环对象
        System.out.println(group.next());
        System.out.println(group.next());

        //执行普通任务
        group.next().execute(()->{
            try {
                Thread.sleep(1000);
                log.debug("ok");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        log.debug("main");

        //执行定时任务
        group.next().scheduleAtFixedRate(()->{

            log.debug("task");
        },0,1, TimeUnit.SECONDS);
        

    }
}
