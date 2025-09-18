package com.netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.log4j.Log4j2;

import java.util.Date;

/**
 * @author Kevin
 * @date 2024/10/7 17:17
 */
@Log4j2
public class HelloClient {

    public static void main(String[] args) throws InterruptedException {
        // 1、启动类
        new Bootstrap()
                // 2、添加EventLoopGroup
                .group(new NioEventLoopGroup())
                // 3、选择客户端的SocketChannel实现
                .channel(NioSocketChannel.class)
                // 4、添加处理器
                .handler(new ChannelInitializer<Channel>() {
                    // 在建立连接后被调用
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 5、连接服务器
                .connect("127.0.0.1", 8080)
                .sync()
                .channel()
                // 6、发送消息
                .writeAndFlush(new Date() + ": hello world!");
    }
}
