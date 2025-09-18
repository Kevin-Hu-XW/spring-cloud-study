package com.bio.testBSocket;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 单线程
 * @author Kevin
 * @date 2024/9/21 15:19
 */
@Log4j2
public class SocketServer1 {


    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8080);
        try{
            while(true){
                Socket socket = serverSocket.accept();
                //下面收取客户端的请求信息
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                int sourcePort = socket.getPort();
                int maxLen = 2048;
                byte[] contextBytes = new byte[maxLen];
                //这里会被阻塞，直到有数据准备好
                int realLen = in.read(contextBytes, 0, maxLen);
                String message= new String(contextBytes, 0, realLen);
                log.info("服务器收到来自于端口："+sourcePort+"的信息："+message);

                out.write(("回发响应信息!").getBytes());
                out.close();
                in.close();
                socket.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            serverSocket.close();
        }
    }
}
