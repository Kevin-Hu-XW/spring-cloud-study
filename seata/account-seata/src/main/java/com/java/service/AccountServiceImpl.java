package com.java.service;

import com.java.mapper.AccountMapper;
import com.java.pojo.Account;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kevin
 * @date 2025/9/18 21:55
 */
@Log4j2
@Service
public class AccountServiceImpl implements AccountService {


    @Autowired
    private AccountMapper accountMapper;
    /**
     * 扣减账户余额
     *
     * @param userId
     * @param money
     */
    @Override
    public void deductMoney(Long userId, Long money) {
        Account account = new Account();
        account.setUserId(userId);
        account.setMoney(money);
        int affect = accountMapper.update(account);
        if (affect <= 0) {
            log.error("扣减账户余额失败");
            throw new RuntimeException("扣减账户余额失败");
        }
        log.info("扣减账户余额成功");
    }
}
