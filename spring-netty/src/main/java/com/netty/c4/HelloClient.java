package com.netty.c4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
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
public class HelloClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        // 1、启动类
        Channel channel = new Bootstrap()
                // 2、添加EventLoopGroup
                .group(worker)
                // 3、选择客户端的SocketChannel实现
                .channel(NioSocketChannel.class)
                // 4、添加处理器
                .handler(new ChannelInitializer<Channel>() {
                    // 在建立连接后被调用
                    @Override
                    protected void initChannel(Channel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ChannelInboundHandlerAdapter(){
                            // 会在连接channel建立成功后，会触发active事件
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                log.debug("channel active");
                                // 一次发送160个字节
                                for (int i = 1; i <= 10; i++){
                                    log.info("正在向服务端发送第"+
                                            i +"次数据......");
//                                    ByteBuf buf = ctx.alloc().buffer(1);
//                                    buf.writeBytes(new byte[]{(byte) i});
                                    ByteBuf buf = ctx.alloc().buffer(10);
                                    buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
                                    ctx.writeAndFlush(buf);
                                }
                            }
                        });
                    }
                })
                // 5、连接服务器
                .connect("127.0.0.1", 8080)
                .sync()
                .channel();
        ChannelFuture channelFuture = channel.closeFuture();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.debug("channel close");
                worker.shutdownGracefully();
            }
        });
    }
}
