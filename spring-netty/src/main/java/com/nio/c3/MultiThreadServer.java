package com.nio.c3;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kevin
 * @date 2024/10/6 13:42
 */
@Log4j2
public class MultiThreadServer {

    public static void main(String[] args) {
        Thread.currentThread().setName("Boss");
        try(ServerSocketChannel ssc = ServerSocketChannel.open()){
            ssc.bind(new InetSocketAddress(8080));
            Selector selector = Selector.open();
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            //创建固定数量的worker
            Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
            for (int i = 0; i < workers.length; i++){
                workers[i] = new Worker("worker-" + i);
            }
            AtomicInteger index = new AtomicInteger();
            while(true){
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()){
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        log.debug("connected......{}",sc.getRemoteAddress());
                        //关联worker中的selector，而不是boss的selector
                        log.debug("before register......{}", sc.getRemoteAddress());
                        /**
                         *
                         * 如果这样写会有问题：selector.select()会阻塞，不会释放publicKeys锁，导致register()方法中拿不到锁阻塞
                         */
                        //sc.register(worker.selector, SelectionKey.OP_READ);
                        //worker.register(sc);
                        //轮询
                        workers[index.getAndIncrement() % workers.length].register(sc);
                        log.debug("after register......{}", sc.getRemoteAddress());
                    }
                    else if (key.isReadable()){
                        //
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    static class Worker implements Runnable{
        private Thread thread;
        private Selector selector;
        private String name;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
        //保证值初始化一次
        private volatile boolean start;

        public Worker(String name){
            this.name = name;
        }

        private void register(SocketChannel sc) throws IOException {
            if (!start){
                thread = new Thread(this,name);
                selector = Selector.open();
                thread.start();
                start = true;
            }
            //向队列中添加任务，任务并没有立刻执行
            log.info("向queue添加任务");
            queue.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            //唤醒selector，让worker线程处理队列中的任务
            //主动唤醒，集合中可能没有就绪事件
            selector.wakeup();
        }

        @Override
        public void run() {
            while(true){
                try {
                    //执行队列中的任务
                    //log.debug("select() is blocking......");
                    selector.select();
                    //log.debug("select() is wakeup......");
                    Runnable task = queue.poll();
                    if (Objects.nonNull(task)){
                        log.debug("task is running......{}",task);
                        task.run();
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()){
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel sc = (SocketChannel) key.channel();
                            log.debug("read......{}",sc.getRemoteAddress());
                            sc.read(buffer);
                            buffer.flip();
                            log.debug(Charset.defaultCharset().decode(buffer));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
