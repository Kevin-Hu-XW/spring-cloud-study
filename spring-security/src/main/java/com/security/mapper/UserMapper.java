package com.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.security.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kevin
 * @date 2024/7/14 15:33
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {


}
