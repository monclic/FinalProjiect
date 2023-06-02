package com.mon.fpc.core.enums;


import com.mon.fpc.utils.redis.RedisKeyInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisKeyEnum implements RedisKeyInterface {
    USER_INFO("user_info", "缓存用户信息，key为id"),
    USER_TOKEN("user_token", "key为用户id，value为用户token"),
    USER_LOGIN_LAST_TIME("user_login_last_time",RedisDeleteType.NOT_DELETE,"用户最近一次登录时间"),
    USER_REGISTER_CODE("user_register_code","注册验证码"),
    USER_RE_SET_CODE("user_re_set_code","找回密码 验证码"),
    USER_CHANGE_EMAIL_OLD_CODE("user_change_email_old_code","更换邮箱 验证码"),
    USER_CHANGE_EMAIL_NEW_CODE("user_change_email_new_code","更换邮箱 验证码"),
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

