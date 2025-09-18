package com.socket.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Kevin
 * @date 2023/12/18 20:39
 */

/**
 * 处理业务逻辑
 */
public class WorkThread extends Thread{

    private Socket socket;

    public WorkThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        /**
         * getInputStream() 方法返回一个 InputStream 对象，
         * 你可以使用它来从连接的另一端接收数据。常见的使用包括从网络上读取文本、二进制数据等。
         */
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
