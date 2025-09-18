package com.bio.testBSocket;

import java.nio.Buffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Kevin
 * @date 2024/9/21 11:29
 */
public class Test {

//    public static void main(String[] args) throws InterruptedException {
//        //CountDownLatch 一等多
//        CountDownLatch countDownLatch = new CountDownLatch(5);
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        for (int i = 0; i < 5; i++){
//            int num = i+1;
//            Thread t= new Thread(() -> {
//                try {
//                    Thread.sleep((long)(Math.random()*1000));
//                    System.out.println("线程 No."+num+"完成了检查工作");
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }finally {
//                    countDownLatch.countDown();
//                }
//            });
//            executorService.submit(t);
//        }
//        System.out.println("等待5个子线程完成检查工作...");
//        countDownLatch.await();
//        System.out.println("所有子线程完成检查工作，开始进入下一阶段...");
//        executorService.shutdown();
//    }

    public static void main(String[] args) throws InterruptedException {
        //多等一，即多个线程等待一个线程完成工作,然后多个线程同时开始工作，常用来进行压测
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(100);
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++){
            Thread t= new Thread(() -> {
                try {
                    begin.await();
                    Thread.sleep((long)(Math.random()*2000));
                    System.out.println(Thread.currentThread().getName()+"发起了请求...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    end.countDown();
                }
            });
            executorService.submit(t);
        }
        Thread.sleep(3000);
        System.out.println("所有线程已准备完毕, 压测开始!");
        begin.countDown();
        try {
            end.await();
            System.out.println("所有线程都请求完毕, 压测结束!");
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
