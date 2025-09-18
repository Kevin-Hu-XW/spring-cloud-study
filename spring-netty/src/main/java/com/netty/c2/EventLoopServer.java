package com.netty.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Kevin
 * @date 2024/10/8 10:42
 */
@Log4j2
public class EventLoopServer {


    //细分2：单独添加处理耗时任务的线程组
    static EventLoopGroup group = new DefaultEventLoopGroup();

    public static void main(String[] args) {
        new ServerBootstrap()
                //如果只设置一个线程组，那么这实际上将boss group和worker group都设置为同一个NioEventLoopGroup
                //.group(new NioEventLoopGroup())
                //细分1： boss 只负责ServerSocketChannel的accept事件，worker只负责SocketChannel的读写事件
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    //建立连接后就调用initChannel方法，只会被调用一次
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder());
                        //关心客户端发送的数据
                        ch.pipeline().addLast("handler1",new ChannelInboundHandlerAdapter(){
                            //如果没有经过StringDecoder处理，数据就是ByteBuf类型
                            //ByteBuf是原本的ByteBuffer的增强
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //ByteBuf buf = (ByteBuf) msg;
                                log.debug("byteBuf:{}",msg);
//                                log.debug("byteBuf:{}",buf.toString(Charset.defaultCharset()));
//                                byte[] bytes = new byte[buf.readableBytes()];
//                                buf.readBytes(bytes);
//                                String msgStr =  new String(bytes, Charset.defaultCharset());
//                                log.debug("msgStr:{}",msgStr);

                            }
                        });
                    }
                })
                .bind(8080);
    }

}
