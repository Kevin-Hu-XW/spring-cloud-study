package com.service.impl;



import com.mapper.TransactionMapper;
import com.pojo.Test;
import com.pojo.Test2;
import com.service.TransactionInterface;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Random;

/**
 * @author Kevin
 * @date 2023/7/3 11:27
 */
@Service
@Log4j2
public class TransactionServiceImpl implements TransactionInterface {

    @Autowired
    private DataSourceTransactionManager txManager;

    @Autowired
    private TransactionMapper transactionMapper;







    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void test() throws Exception {
        // 手动开启事务  start
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//        TransactionStatus status = txManager.getTransaction(def);
        addTest();
        addTest2();
        int i = 1/0;
        // 手动提交事务
        //txManager.commit(status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public int addTest() throws Exception {
        Test test = Test.builder()
                .name("test"+new Random().nextInt())
                .build();
        log.info("name:"+test.getName());
        int count = transactionMapper.addTest(test);
        if (count<1){
            throw new Exception("add test error");
        }
        //int i = 1/0;
        return count;
    }

    @Override
    public int addTest2() throws Exception {
        Test2 test2 = Test2.builder()
                .salary(new Random().nextInt()*1000)
                .build();
        int count = transactionMapper.addTest2(test2);
        if (count<1){
            throw new Exception("add test2 error");
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void methodA() {
        Test2 test2 = Test2.builder()
                .salary(new Random().nextInt()*1000)
                .build();
        int count = transactionMapper.addTest2(test2);
        log.info("####Salary:"+test2.getSalary());
        try {
            methdoB();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        int i =1/0;

    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.NESTED)
    public void methdoB() {
        Test test = Test.builder()
                .name("test"+new Random().nextInt())
                .build();
        log.info("name:"+test.getName());
        int count = transactionMapper.addTest(test);

        //int i =1/0;
    }
}
