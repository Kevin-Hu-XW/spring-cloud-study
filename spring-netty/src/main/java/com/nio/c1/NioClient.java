package com.nio.c1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @date 2024/10/4 12:12
 */
public class NioClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        Runnable nioClient = () -> {
            try {
                SocketChannel socketChannel = SocketChannel.open();
                socketChannel.connect(new InetSocketAddress("localhost", 9080));
                socketChannel.configureBlocking(false);
                ByteBuffer buffer = ByteBuffer.wrap("Hello, 沉默王二 NIO!".getBytes());
                socketChannel.write(buffer);
                buffer.clear();
                socketChannel.read(buffer);
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        // 分别测试 NIO 和传统 IO 的服务器性能
        long startTime, endTime;
        int clientCount = 10000;
        startTime = System.currentTimeMillis();
        startTime = System.currentTimeMillis();
        ExecutorService executorServiceNIO = Executors.newFixedThreadPool(10);
        for (int i = 0; i < clientCount; i++) {
            executorServiceNIO.execute(nioClient);
        }
        executorServiceNIO.shutdown();
        executorServiceNIO.awaitTermination(1, TimeUnit.MINUTES);
        endTime = System.currentTimeMillis();
        System.out.println("NIO 服务器处理 " + clientCount + " 个客户端耗时: " + (endTime - startTime) + "ms");
    }
}
