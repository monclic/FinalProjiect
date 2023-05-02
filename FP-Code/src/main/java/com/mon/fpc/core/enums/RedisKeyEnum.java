package com.mon.fpc.core.enums;


import com.mon.fpc.utils.redis.RedisKeyInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisKeyEnum implements RedisKeyInterface {
    USER_INFO("user_info", "缓存用户信息，key为id"),
    USER_TOKEN_INDEX("user_token_index", "key为用户id，value为用户token"),
    USER_TOKEN("user_token", "key为用户id，value为用户token"),
    USER_LOGIN_LAST_TIME("user_login_last_time",RedisDeleteType.NOT_DELETE,"用户最近一次登录时间"),
    ;
    private String folder;
    private RedisDeleteType deleteType;
    private String keyDescription;

    RedisKeyEnum(String folder, String keyDescription) {
        this.folder = folder;
        this.deleteType = RedisDeleteType.DELETE;
        this.keyDescription = keyDescription;
    }
}

