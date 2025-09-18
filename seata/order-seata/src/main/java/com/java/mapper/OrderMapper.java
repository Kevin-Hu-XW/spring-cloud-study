package com.java.mapper;

import com.java.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kevin
 * @date 2025/9/18 21:27
 */
@Mapper
public interface OrderMapper {

    int create(Order order);
}
