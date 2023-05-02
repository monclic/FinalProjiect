package com.mon.fpc.core;

import com.mon.fpc.core.enums.RedisKeyEnum;
import com.mon.fpc.utils.redis.RedisUtil;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.Resource;

/**
 * redis 过期key监听器
 * @author mon
 * @date 2022-11-01 17:44
 * @description:
 */

public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }



}
