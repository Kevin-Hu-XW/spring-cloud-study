package com.java.service;

/**
 * @author Kevin
 * @date 2025/9/18 21:08
 */
public interface AccountService {

    /**
     * 扣减账户余额
     * @param userId
     * @param money
     */
    void deductMoney(Long userId, Long money);
}
