package com.netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;

/**
 * @author Kevin
 * @date 2024/10/9 22:29
 */
@Log4j2
public class CloseFutureClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()

                .group(nioEventLoopGroup)

                .channel(NioSocketChannel.class)

                .handler(new ChannelInitializer<Channel>() {
                    // 在建立连接后被调用
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("127.0.0.1", 8080);
        Channel channel = channelFuture.sync().channel();
        log.debug("channel:{}",channel);
        new Thread(() -> {
            log.debug("channel:{}",channel.isActive());
            Scanner scanner = new Scanner(System.in);
            while(true){
                String line = scanner.next();
                if ("q".equals(line)){
                    /**
                     * channel.close();操作是在nio线程中执行的
                     * log.debug("处理关闭后的操作....");是在input线程中执行的
                     * 这里无法控制两个线程执行的先后顺序，所以无法直接在channel.close();后面执行log.debug("处理关闭后的操作....");
                     */
                    // 关闭连接,和connect一样也是异步操作
                    channel.close();
                    //不能在这里处理关闭后的操作
                    //log.debug("处理关闭后的操作....");

                }
                channel.writeAndFlush(line);
            }
        },"input").start();

        //获取closeFuture对象
        //同步关闭
        /*ChannelFuture closeFuture = channel.closeFuture();
        log.debug("waiting close ....");
        closeFuture.sync();
        log.debug("关闭后处理....");*/
        // 异步关闭
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture closeFuture) throws Exception {

                log.debug("关闭后处理....");
                /***
                 * 优雅关闭线程组：
                 * 1、拒绝接收新任务
                 * 2、等待所有任务完成
                 */
                nioEventLoopGroup.shutdownGracefully();
            }
        });
    }
}
