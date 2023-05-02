package com.mon.fpc.utils;

import cn.hutool.core.bean.BeanUtil;
import com.mon.fpc.core.Resp;
import com.mon.fpc.core.exception.BaseException;


/**
 * @author Created by mon
 * @description
 */
public class LoginUserHolder {
    private static final ThreadLocal<Object> userContextThreadLocal = new InheritableThreadLocal<>();

    public static void set(Object user) {
        userContextThreadLocal.set(user);
    }

    public static <T> T get(Class<T> cls) {
        Object userInfo = userContextThreadLocal.get();
        if (userInfo == null || !userInfo.getClass().equals(cls)) {
            throw new BaseException(Resp.RespEnum.NO_LOGIN, "凭证错误，请重新登录");
        }
        T t;
        try {
            t = BeanUtil.toBean(userInfo, cls);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(Resp.RespEnum.NO_LOGIN, "用户信息解析错误，请重新登录");
        }
        if (t == null) {
            throw new BaseException(Resp.RespEnum.NO_LOGIN, "用户信息解析错误，请重新登录");
        }
        return t;
    }

    //仅用于Product/Detail、Issuer/Detail,Home/Info 区分是否登录的情况
    public static <T> T get(Class<T> cls,int type) {
        if (type!=0) {
            throw new BaseException("请重新登录");
        }
        Object userInfo = userContextThreadLocal.get();
        if (userInfo == null ){
            return null;
        }
        if (!userInfo.getClass().equals(cls)) {
            throw new BaseException(Resp.RespEnum.NO_LOGIN, "凭证错误，请重新登录");
        }
        T t;
        try {
            t = BeanUtil.toBean(userInfo, cls);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(Resp.RespEnum.NO_LOGIN, "用户信息解析错误，请重新登录");
        }
        if (t == null) {
            throw new BaseException(Resp.RespEnum.NO_LOGIN, "用户信息解析错误，请重新登录");
        }
        return t;
    }

    public static void remove() {
        userContextThreadLocal.remove();
    }
}
