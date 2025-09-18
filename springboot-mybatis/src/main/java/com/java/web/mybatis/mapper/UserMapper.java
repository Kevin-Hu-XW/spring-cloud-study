package com.java.web.mybatis.mapper;

import com.java.web.base.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kevin
 * @date 2023/3/16 22:31
 */
@Mapper
public interface UserMapper {
    User queryUserById(Long id);
}
