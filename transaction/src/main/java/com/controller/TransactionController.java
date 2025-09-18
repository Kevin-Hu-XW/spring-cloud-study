package com.controller;

import com.service.TransactionInterface;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kevin
 * @date 2023/7/3 15:41
 */
@RestController
@Log4j2
public class TransactionController {

    @Autowired
    private TransactionInterface transactionInterface;

    @RequestMapping(value = "/transaction",method = RequestMethod.GET)
    public Integer test(){
        try {
//            int count = transactionInterface.addTest();
            //transactionInterface.test();
            transactionInterface.methodA();
            return 0;
        } catch (Exception e) {
            log.info("####"+e.getMessage());
        }
        return null;
    }
}
