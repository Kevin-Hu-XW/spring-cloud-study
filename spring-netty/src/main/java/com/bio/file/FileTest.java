package com.bio.file;


import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Kevin
 * @date 2024/9/20 11:32
 */
@Log4j2
public class FileTest {

    /**
     * BIO
     * @param src 源文件路径
     * @param dest 目标文件路径
     * @throws FileNotFoundException 源文件不存在
     */
    public static long readAndWriteFile(String src,String dest) throws FileNotFoundException {
        try(FileInputStream in = new FileInputStream(src);FileOutputStream out = new FileOutputStream(dest)){
            long startTime = System.currentTimeMillis();
            int byteData;
            while((byteData = in.read())!=-1){
                //逐个字节处理数据
                out.write(byteData);

            }
            long endTime = System.currentTimeMillis();
            return endTime - startTime;
        }catch (IOException e){
            log.error("####"+e.getMessage());
        }
        return 0;
    }

    /**
     * IO byteArray
     * @param src 源文件路径
     * @param dest 目标文件路径
     */
    public static long readAndWriteFileWithByteArray(String src,String dest){
        try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest))){
            long startTime = System.currentTimeMillis();
            //使用字节数组缓冲区来读取
            byte[] buffer = new byte[16*1024];
            //读取字节到字节数组缓冲区
            while(in.read(buffer)!=-1){
                out.write(buffer);
            }
            long endTime = System.currentTimeMillis();
            return endTime - startTime;
        }catch (IOException e){
            log.error("####"+e.getMessage());
        }
        return 0;
    }

    /**
     * NIO Buffer Channel
     * @param src 源文件路径
     * @param dest 目标文件路径
     */
    public static long readAndWriteFileWithByteBuffer(String src,String dest){
        try(FileInputStream in = new FileInputStream(src); FileChannel srcFileChannel = in.getChannel();
        FileOutputStream out = new FileOutputStream(dest); FileChannel destFileChannel = out.getChannel()){

            long startTime = System.currentTimeMillis();
            //直接缓冲区 ByteBuffer directBuffer = ByteBuffer.allocateDirect(8*1024);
            //堆缓冲区
            ByteBuffer buffer = ByteBuffer.allocateDirect(16*1024);
            srcFileChannel.read(buffer);
            /* 切换读写 */
            buffer.flip();
            /* 把缓冲区的内容写入输出文件中 */
            while(buffer.hasRemaining()){
                destFileChannel.write(buffer);
            }
            /* 清空缓冲区 */
            buffer.clear();
            long endTime = System.currentTimeMillis();
            return endTime - startTime;
        }catch (IOException e){
            log.error("####"+e.getMessage());
        }
        return 0;
    }

    /**
     * NIO ZeroCopy
     * @param src 源文件路径
     * @param dest 目标文件路径
     */
    public static long zeroCopy(String src,String dest){
        long startTime = System.currentTimeMillis();
        try(FileChannel srcFileChannel = new FileInputStream(src).getChannel();
        FileChannel destFileChannel = new FileOutputStream(dest).getChannel()){
        long position = 0;
        long size = srcFileChannel.size();
        srcFileChannel.transferTo(position,size,destFileChannel);
        }catch (IOException e){
            log.error("####"+e.getMessage());
        }
        return System.currentTimeMillis() - startTime;
    }


    public static void main(String[] args) throws FileNotFoundException {
        String src = "C:\\tmp\\test\\child1.bin.xml";
        String dest = "C:\\tmp\\1.xml";
        //System.out.println("BIO 逐字节处理数据:"+readAndWriteFile(src,dest));
        System.out.println("BIO 通过缓冲区处理数据:"+readAndWriteFileWithByteBuffer(src,dest));
        System.out.println("NIO 通过缓冲区处理数据"+readAndWriteFileWithByteArray(src,dest));
        System.out.println("NIO 零拷贝处理数据："+zeroCopy(src,dest));
    }
}
