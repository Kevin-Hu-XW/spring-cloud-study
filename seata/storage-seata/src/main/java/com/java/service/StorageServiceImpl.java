package com.java.service;

import com.java.mapper.StorageMapper;
import com.java.pojo.Storage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kevin
 * @date 2025/9/18 22:32
 */
@Service
@Log4j2
public class StorageServiceImpl implements StorageService{

    @Autowired
    private StorageMapper storageMapper;

    /**
     * 扣减库存
     *
     * @param commodityCode 商品编号
     * @param count         扣减数量
     */
    @Override
    public void deduct(String commodityCode, Integer count) {
        Storage storage = new Storage();
        storage.setCommodityCode(commodityCode);
        storage.setCount(count);
        int affect = storageMapper.update(storage);
        if (affect <= 0) {
            log.error("扣减库存失败");
            throw new RuntimeException("扣减库存失败");
        }
        log.info("扣减库存成功");
    }
}
