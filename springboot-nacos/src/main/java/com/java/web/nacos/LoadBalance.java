package com.java.web.nacos;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author Kevin
 * @date 2023/3/30 14:31
 */
public interface LoadBalance {
    /**
     * 根据多个不同的地址 返回单个调用rpc地址
     *
     * @param serviceInstances
     * @return
     */
    ServiceInstance getSingleAddres(List<ServiceInstance> serviceInstances);

}
