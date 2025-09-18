package com.java.web.nacos.controller;

import com.java.web.nacos.LoadBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Kevin
 * @date 2023/3/30 10:38
 */
@RestController
public class ConsumerController {

    @Autowired
    private DiscoveryClient discoveryClient;

    //RestTemplate不是SpringCloud写的，本身Spring支持HTTP协议调用
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalance loadBalance;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @RequestMapping("/consumer")
    public Object consumeService(){
        //1、根据服务名称，从政策中心获取集群列表
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("springboot-mybatis");
        //2、通过负载均衡算法，从列表中任意选择一个服务进行rpc远程调用
        ServiceInstance serviceInstance = this.loadBalance.getSingleAddres(serviceInstances);
        String result = this.restTemplate.getForObject(serviceInstance.getUri()+"/test/6",String.class);
        return "调用springboot-mybatis服务的结果:"+result;

    }


    /**
     * Spring Cloud Nacos 2021 放弃Ribbon后 使用LoadBalancer + Nacos做负载均衡 ，
     * @return
     */
    @RequestMapping("/loadBalancer")
    public Object consumeRibbonService(){
        //1、根据服务名称，从注册中心获取集群列表
        //List<ServiceInstance> serviceInstances = discoveryClient.getInstances("springboot-mybatis");
        //2、列表中任意选择一个就行rpc远程调用
        //ServiceInstance serviceInstance = this.loadBalance.getSingleAddres(serviceInstances);
        String result = this.restTemplate.getForObject("http://springboot-mybatis/test/6",String.class);
        return "调用springboot-mybatis服务的结果:"+result;

    }

    @RequestMapping("/loadbalanceClient")
    public Object loadbalanceClient(){

        //String result = this.restTemplate.getForObject("http://springboot-mybatis/test/6",String.class);
        ServiceInstance result = this.loadBalancerClient.choose("springboot-mybatis");
        return "调用springboot-mybatis服务的结果:"+result.getUri();

    }
    @Autowired
    private Environment environment;
    @RequestMapping("/load-balancer")
    public String loadBalancer(){
        // 真实端口
        String port = environment.getProperty("local.server.port");
        return "load-balancer"+port;
    }
}
