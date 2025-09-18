package com.socket.service;

import java.io.IOException;

/**
 * @author Kevin
 * @date 2023/12/18 21:08
 */
public class HttpServer {


    public static void main(String[] args) throws IOException {

        new ListenerThread(9090).start();
    }
}
