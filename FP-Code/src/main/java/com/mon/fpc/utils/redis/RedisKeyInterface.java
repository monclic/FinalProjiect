package com.mon.fpc.utils.redis;

/**
 * @author Created by mon
 * @description redisKey管理
 */
public interface RedisKeyInterface {
    enum RedisDeleteType {
        DELETE, NOT_DELETE
    }

    //分类名称
    String getFolder();

    //是否随意清空缓存
    RedisDeleteType getDeleteType();

    //key描述
    String getKeyDescription();

}
