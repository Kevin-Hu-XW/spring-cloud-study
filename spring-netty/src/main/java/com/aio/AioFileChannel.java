package com.aio;

import lombok.extern.log4j.Log4j2;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author Kevin
 * @date 2024/10/7 10:50
 */
@Log4j2
public class AioFileChannel {

    public static void main(String[] args) {

        try(AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("c:/tmp/1.txt"), StandardOpenOption.READ)){

            /**
             * 参数1 ByteBuffer
             * 参数2 读取的起始位置
             * 参数3 附件
             * 参数4 回调对象 CompletionHandler<Integer, ByteBuffer>
             */
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            log.debug("开始读取");
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    log.debug("读取成功");
                    attachment.flip();
                    log.debug("读取内容：" + Charset.defaultCharset().decode(attachment));
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {

                }
            });
            log.debug("结束读取");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
