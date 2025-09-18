package com.bio.testBSocket;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.concurrent.CountDownLatch;

/**
 * 一个SocketClientRequestThread线程模拟一个客户端请求
 * @author Kevin
 * @date 2024/9/20 16:04
 */
public class SocketClientRequestThread implements Runnable{


    private CountDownLatch countDownLatch;

    /**
     * 线程编号
     */
    private Integer clientIndex;

    /**
     * countDownLatch是java提供的同步计数器
     * 当计数器数值减为0时，所有受其影响的而等待的线程将会被激活，这样保证模拟并发请求的真实性
     * @param clientIndex
     * @param countDownLatch
     */
    public SocketClientRequestThread(Integer clientIndex, CountDownLatch countDownLatch) {
        this.clientIndex = clientIndex;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        Socket socket = null;
        OutputStream clientRequest = null;
        InputStream clientResponse = null;

        try {
            socket = new Socket("localhost", 8080);
            clientRequest = socket.getOutputStream();
            clientResponse = socket.getInputStream();
            // 等待主线程完成所有线程的启动，然后所有线程一起发送请求
            this.countDownLatch.await();

            clientRequest.write(("这是第"+this.clientIndex+"客户端的请求").getBytes());
            clientRequest.flush();

            System.out.println("第"+this.clientIndex+"客户端的请求发送完毕，等待服务器返回信息");

            int maxLength = 1024;
            byte[] contextBytes = new byte[maxLength];
            int realLean;
            StringBuilder messages = new StringBuilder();
            while((realLean = clientResponse.read(contextBytes,0,maxLength))!=-1){
                messages.append(new String(contextBytes, 0, realLean));
            }
            System.out.println("第"+this.clientIndex+"客户端收到服务器返回信息："+messages);

        } catch (Exception e) {
            System.out.println("#####"+e.getMessage());
        }
        finally {
            try {
                assert clientRequest != null;
                clientRequest.close();
                assert clientResponse != null;
                clientResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
