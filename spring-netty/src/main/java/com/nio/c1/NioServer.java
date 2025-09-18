package com.nio.c1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author Kevin
 * @date 2024/9/23 15:34
 */
public class NioServer {



    public static void main(String[] args) throws IOException {
        //创建 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9080));

        /*
         * 所有注册到Selector上的Channel都应该设置成为非阻塞模式，这是因为Selector的工作机制依赖于非阻塞I/O，
         * Selector需要依赖非阻塞通道，在没有事件发生时立即返回，从而可以继续轮询其他通道的事件(底层是poll模型时)
         */
        //设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //创建Selector
        Selector selector = Selector.open();

        //将ServerSocketChannel注册到Selector，关注OP_ACCEPT事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            //阻塞直到有事件发生
            selector.select();
            //获取发送事件的SelectionKey
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //从集合中移除当前SelectionKey，避免重复处理
                iterator.remove();

                //判断事件类型
                if (key.isAcceptable()){
                    //有新的连接请求
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    //接收连接,通信channel
                    SocketChannel client = server.accept();
                    //设置为非阻塞模式
                    client.configureBlocking(false);
                    //将新的SocketChannel注册到Selector,关注OP_READ事件
                    client.register(selector,SelectionKey.OP_READ);
                }
                else if (key.isReadable()){
                    //有数据可读
                    SocketChannel client = (SocketChannel) key.channel();
                    //创建ByteBuffer缓冲区
                    ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

                    //从SocketChannel中读取数据并写入byteBuffer
                    client.read(buffer);
                    //翻转ByteBuffer,准备读取数据，切换为读模式
                    buffer.flip();
                    // 将数据从 ByteBuffer 写回到 SocketChannel
                    client.write(buffer);
                    //关闭连接
                    client.close();
                }
            }

        }
    }
}
