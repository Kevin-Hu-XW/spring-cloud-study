package com.bio;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Kevin
 * @date 2024/9/24 10:16
 */
public class Client {


    public static void main(String[] args) throws IOException, InterruptedException {

        Socket socket = new Socket("localhost", 8080);
        Boolean isConnected =socket.isConnected();
        System.out.println(isConnected);
        synchronized (Client.class){
            Client.class.wait();
        }
    }
}
