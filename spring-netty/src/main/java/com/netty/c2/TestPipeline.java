package com.netty.c2;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.Charset;

/**
 * @author Kevin
 * @date 2024/10/11 14:17
 */

@Log4j2
public class TestPipeline {
    static EventLoopGroup group = new DefaultEventLoopGroup();
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    //建立连接后就调用initChannel方法，只会被调用一次
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //1、通过channel拿到Pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        //2、添加处理器 head -> handler1 -> handler2 -> handler3 ->handler4->  handler5 ->handler6->tail
                        //入站顺序：重前往后 出站顺序：重后往前
                        pipeline.addLast("handler1", new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug(1);
                                //调用下一个处理器
                                super.channelRead(ctx, msg);
                            }
                        });

                        pipeline.addLast("handler2", new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug(2);
                                super.channelRead(ctx, msg);
                            }
                        });
                        pipeline.addLast("handler3", new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug(3);
                                //super.channelRead(ctx, msg); 因为后面没有入站处理器，所以不需要调用
                                ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes()));
                            }
                        });

                        pipeline.addLast("handler4", new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug(4);
                                super.write(ctx, msg, promise);
                            }
                        });

                        pipeline.addLast("handler5", new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug(5);
                                super.write(ctx, msg, promise);
                            }
                        });
                        pipeline.addLast("handler6", new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug(6);
                                super.write(ctx, msg, promise);
                            }
                        });

                    }
                })
                .bind(8080);
    }
}
