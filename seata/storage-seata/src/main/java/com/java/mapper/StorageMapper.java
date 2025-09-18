package com.java.mapper;

import com.java.pojo.Storage;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kevin
 * @date 2025/9/18 22:29
 */
@Mapper
public interface StorageMapper {

    int update(Storage storage);
}
