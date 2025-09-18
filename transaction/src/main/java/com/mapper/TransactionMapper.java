package com.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.pojo.Test;
import com.pojo.Test2;

/**
 * @author Kevin
 * @date 2023/7/3 14:08
 */
@Mapper
public interface TransactionMapper {

    int addTest(Test test);
    int addTest2(Test2 test2);
}
