package com.socket.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Kevin
 * @date 2023/12/16 17:40
 */

@Log4j2
public class SocketController {


    private final static Integer PORT = 9099;

    public static void main(String[] args) {
        try{
            log.info("Server starting......");
            ServerSocket serverSocket = new ServerSocket(PORT);
            while(true){
                //当client接入后，创建套接字
                Socket socket = serverSocket.accept();
                System.out.println("socket.getPort():"+socket.getPort());
                /**
                 * getInputStream() 方法返回一个 InputStream 对象，
                 * 你可以使用它来从连接的另一端接收数据。常见的使用包括从网络上读取文本、二进制数据等。
                 */
                InputStream inputStream = socket.getInputStream();
                /**
                 * getOutputStream() 方法返回一个 OutputStream 对象，
                 * 你可以使用它来向连接的另一端发送数据。常见的使用包括通过网络发送文本、二进制数据等。
                 */
                OutputStream outputStream = socket.getOutputStream();
                String html = "<html>" +
                        "<head><title>Hello,My name is Kevin</title></head>" +
                        "<body><h1>My ID is 2019114</h1></body>" +
                        "</html>";
                final String CRLF = "\n\r";
                String response =
                        "HTTP/1.1 200 OK"+CRLF+
                                "Content-Length: "+html.getBytes(StandardCharsets.UTF_8).length+CRLF+
                                CRLF+
                                html+
                                CRLF+CRLF;
                outputStream.write(response.getBytes(StandardCharsets.UTF_8));
            }

//            inputStream.close();
//            outputStream.close();
//            socket.close();
//            serverSocket.close();
        }
        catch(Exception e){
            log.error("#####"+e.getMessage());
        }
    }
}
