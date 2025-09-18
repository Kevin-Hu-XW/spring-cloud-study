package com.bio.testBSocket;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Kevin
 * @date 2024/9/21 15:53
 */
@Log4j2
public class SocketServer2 {

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(8080);
        while(true){
            Socket socket = serverSocket.accept();
            //当然业务处理过程可以交给一个线程(这里可以使用线程池),并且线程的创建是很耗资源的。
            //最终改变不了.accept()只能一个一个接受socket的情况,并且被阻塞的情况
            SocketServerThread socketServerThread = new SocketServerThread(socket);
            new Thread(socketServerThread).start();
        }

    }

}
