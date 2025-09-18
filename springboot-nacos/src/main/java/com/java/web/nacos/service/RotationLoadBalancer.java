package com.java.web.nacos.service;

import com.java.web.nacos.LoadBalance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kevin
 * @date 2023/3/30 14:32
 */
@Component
public class RotationLoadBalancer implements LoadBalance {
    //原子类记录访问次数
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    /**
     * 轮询
     * @param serviceInstances
     * @return
     */
    @Override
    public ServiceInstance getSingleAddres(List<ServiceInstance> serviceInstances) {
        int index= atomicInteger.incrementAndGet()%2;
        return serviceInstances.get(index);
    }
}
