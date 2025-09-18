package com.netty.c4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @author Kevin
 * @date 2024/10/7 16:44
 */
@Log4j2
public class HelloServer {

    public static void main(String[] args) {
        //1、启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
                // 2、BoosEventLoopGroup，处理连接请求，WorkerEventLoopGroup(Selector,Thread)，处理读写事件,group组
                .group(new NioEventLoopGroup())
                // 3、选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                //设置服务端的接收缓冲区大小
                .option(ChannelOption.SO_RCVBUF,2)
                // 4、boss处理连接，worker(child)处理读写，决定了worker(child)能执行那些操作（handler）
                .childHandler(
                        //5、channel代表和客户端进行数据读写的通道 Initializer初始化，负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 6、添加具体handler
                        ch.pipeline().addLast(new LoggingHandler());

                        //ch.pipeline().addLast(new StringDecoder()); // 将ByteBuf 转成字符串
                        //7、添加自定义handler
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            //读事件
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // 打印上一步转换好的字符串
                                ByteBuf byteBuf = (ByteBuf) msg;
                                byte[] bytes = new byte[byteBuf.readableBytes()];
                                byteBuf.readBytes(bytes);
                                log.debug(bytes.length);
                                log.debug("byte:{}", Arrays.toString(bytes));
                            }
                        });
                    }
                })
                // 8、绑定监听端口
                .bind(8080);
    }
}
