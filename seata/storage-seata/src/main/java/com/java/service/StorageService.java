package com.java.service;

/**
 * @author Kevin
 * @date 2025/9/18 21:17
 */
public interface StorageService {

    /**
     * 扣减库存
     *
     * @param commodityCode 商品编号
     * @param count         扣减数量
     */
    void deduct(String commodityCode, Integer count);
}
