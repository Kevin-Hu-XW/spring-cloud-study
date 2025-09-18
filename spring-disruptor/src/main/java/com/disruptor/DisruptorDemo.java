package com.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.*;

// 定义事件类
class LongEvent {
    private long value;

    public void set(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongEvent{" +
                "value=" + value +
                '}';
    }
}

// 定义事件工厂，负责创建事件实例
class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}

// 定义事件生产者
class LongEventProducer {
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(long value) {
        // 获取下一个序号
        long sequence = ringBuffer.next();
        try {
            // 填充事件数据
            LongEvent event = ringBuffer.get(sequence);
            event.set(value);
        } finally {
            // 发布事件
            ringBuffer.publish(sequence);
        }
    }
}


// 定义事件消费者
class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("Event consumed: " + event);
    }
}
/**
 * @author Admin
 */
public class DisruptorDemo {
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService executor = newCachedThreadPool();

        // 创建事件工厂
        LongEventFactory eventFactory = new LongEventFactory();

        // 定义环形缓冲区大小，必须是 2 的幂
        int bufferSize = 1024;

        // 创建 Disruptor 实例
        Disruptor<LongEvent> disruptor = new Disruptor<>(eventFactory, bufferSize, executor);

        // 设置消费者
        disruptor.handleEventsWith(new LongEventHandler());

        // 启动 Disruptor
        disruptor.start();

        // 获取 RingBuffer
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        // 创建生产者
        LongEventProducer producer = new LongEventProducer(ringBuffer);

        // 模拟发布事件
        for (long i = 0; i < 10; i++) {
            producer.onData(i);
        }

        // 关闭 Disruptor 和线程池
        disruptor.shutdown();
        executor.shutdown();
    }
}
