package com.nio.c2;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Kevin
 * @date 2024/10/5 20:24
 */
@Log4j2
public class NIOServer {

    public static void main(String[] args) {
        try (ServerSocketChannel channel = ServerSocketChannel.open()) {
            channel.bind(new InetSocketAddress(8080));
            Selector selector = Selector.open();
            channel.configureBlocking(false);
            SelectionKey register = channel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                /**
                 * select方法，没有事件发生线程阻塞，有事件发生线程才会运行
                 * select 在事件为处理时，不会阻塞，事件发生后要么处理，要么取消，不能置之不理
                 *
                 */
               selector.select();
               //获取所有注册事件，当有事件发生时，就会把selector.keys()中的事件添加到selector.selectedKeys()集合中，
                // 但是不会删除，需要我们自己自动删除
                //Set<SelectionKey> keys1 = selector.keys();
                // 获取所有就绪事件
                Set<SelectionKey> keys = selector.selectedKeys();
                // 遍历所有事件，逐一处理
                Iterator<SelectionKey> iter = keys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    // 处理完毕，必须将事件移除
                    iter.remove();
                    log.debug("key: {}", key);
                    // 判断事件类型
                    if (key.isAcceptable()) {
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        log.debug("ssc:{}", ssc);
                        // 必须处理，且处理完需要删除，如果不删除，下次select还是可以拿到，因为selector.selectedKeys()中还在，
                        // 但是注册的channel不存在，c.accept()获取的值会为null
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);

                        sc.register(selector, SelectionKey.OP_READ);
                        //key.cancel();
                    }
                    else if (key.isReadable()) {
                        try {
                            //拿到触发事件的channel
                            SocketChannel sc = (SocketChannel) key.channel();
                            log.debug("sc:{}", sc);
                            ByteBuffer buffer = ByteBuffer.allocate(4);
                            //客户端调用close()方法正常断开，返回-1
                            int read= sc.read(buffer);
                            if (read == -1){
                                key.cancel();
                                key.channel().close();
                            }else{
                                buffer.flip();
                                log.debug("{}", Charset.defaultCharset().decode(buffer));
                            }
                        }
                        catch (IOException e){
                            e.printStackTrace();
                            log.error("read error:客户端已关闭" );
                            // 客户端断开连接，因此需要将key取消（从 selector中keys()集合中正真删除key）
                            // key.cancel() 用于取消 SelectionKey 的注册，释放资源，通常与 channel.close() 一起使用，避免继续监听已经失效的通道
                            /**
                             * 具体原因是：当客户端关闭连接时，服务器端的 SocketChannel 会接收到一个 可读事件（READ 事件），
                             * 这个可读事件通常表示对方关闭连接，也就是操作系统通知的 EOF 信号。如果服务器端的应用程序没有取消该 SelectionKey，
                             * Selector 会继续将这个 EOF 状态 当作可读事件，加入到 selectedKeys 集合中。
                             * 这就意味着在每次 select() 调用时，Selector 都会认为有事件发生，并返回这个 可读事件。
                             */
                            key.cancel();
                            key.channel().close();
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
