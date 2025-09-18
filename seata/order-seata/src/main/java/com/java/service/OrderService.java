package com.java.service;

/**
 * @author Kevin
 * @date 2025/9/18 21:18
 */
public interface OrderService {

    void create(Long userId, String commodityCode, Integer count);
}
