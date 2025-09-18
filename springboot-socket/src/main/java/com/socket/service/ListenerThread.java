package com.socket.service;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Kevin
 * @date 2023/12/18 20:38
 */

/**
 * 父线程负责维持socket打开并接入client，子线程可以负责处理client的请求。
 * 监听线程
 */
@Log4j2
public class ListenerThread extends Thread{

    private final ServerSocket serverSocket;

    public ListenerThread(Integer port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        log.info("监听端口：{}", port);
    }

    @Override
    public void run() {
        while (serverSocket.isBound()&&!serverSocket.isClosed()){
            try {
                Socket socket = serverSocket.accept();
                WorkThread workThread = new WorkThread(socket);
                log.info(workThread.getName()+":{}",socket.getPort());
                workThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
