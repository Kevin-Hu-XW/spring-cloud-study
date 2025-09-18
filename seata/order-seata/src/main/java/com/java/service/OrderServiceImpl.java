package com.java.service;

import com.java.mapper.OrderMapper;
import com.java.pojo.Order;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kevin
 * @date 2025/9/18 21:42
 */
@Log4j2
@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void create(Long userId, String commodityCode, Integer count) {

        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        Long money = (long) (count * 100);
        order.setMoney(money);
        order.setStatus(0);
        int affect = orderMapper.create(order);
        if (affect <= 0) {
            log.error("创建订单失败");
            throw new RuntimeException("创建订单失败");
        }

        log.info("创建订单成功");

    }
}
