package com.java.mapper;

import com.java.pojo.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kevin
 * @date 2025/9/18 22:25
 */
@Mapper
public interface AccountMapper {

    int update(Account account);
}
