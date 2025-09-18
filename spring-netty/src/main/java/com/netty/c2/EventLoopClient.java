package com.netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author Kevin
 * @date 2024/10/7 17:17
 */
@Log4j2
public class EventLoopClient {

    public static void main(String[] args) throws InterruptedException {
        // 2 带有Future、Promise的类型都是和异步方法配套使用，用来处理结果
        ChannelFuture channelFuture = new Bootstrap()

                .group(new NioEventLoopGroup())

                .channel(NioSocketChannel.class)

                .handler(new ChannelInitializer<Channel>() {
                    // 在建立连接后被调用
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 1、连接服务器
                //异步非阻塞，main线程发起了调用，真正执行connect 是 nio线程
                //如果不调用 channelFuture.sync()，则main线程会继续向下执行，不会等待连接建立完成，无法获取建立连接的Channel通道
                .connect("127.0.0.1", 8080); // 1s后才会执行

        //channelFuture.sync();
        //2.1 使用sync()方法同步处理结果（主线程去主动等待结果），阻塞当前线程，直到nio线程连接建立完成
        /*channelFuture.sync();
        Channel channel = channelFuture.channel();
        log.debug("客户端启动成功:{}", channel);
        channel.writeAndFlush("client3");*/

        // 2.2 使用addListener(回调对象)方法异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            // 在nio线程连接建立好之后，会调用operationComplete方法
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                log.debug("客户端启动成功:{}", channel);
                ByteBuf buf = channel.alloc().buffer();
                buf.writeBytes("client3".getBytes(CharsetUtil.UTF_8));
                channel.writeAndFlush(buf);
                ByteBuf buf2 = channel.alloc().buffer();
                buf2.writeBytes("client4".getBytes(CharsetUtil.UTF_8));
                //一旦调用writeAndFlush，Netty会自动管理ByteBuf的释放，即调用release()，将引用计数键位0
                channel.writeAndFlush(buf2);
            }
        });



    }
}
