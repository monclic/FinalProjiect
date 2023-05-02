package com.mon.fpc.utils;


import com.mon.fpc.core.exception.BaseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mon
 * @date 2022-09-19 10:05
 * @description:
 */

public class VerifyUtils {
    //手机号码验证
    public static boolean isMobileNO(String mobiles) {
        try {
            Pattern p = Pattern
                    .compile("(1[0-9][0-9])\\d{8}");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        } catch (Exception e) {
            return false;
        }
    }

    //验证邮箱地址是否正确
    public static boolean checkEmail(String email) {
        try {
            String check = "([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    //判断用户名类型 返回true为邮箱，返回false为手机号，其他的报错
    public static boolean getTypeOfUserName(String username){
        if (isMobileNO(username)){
            return false;
        }else if (checkEmail(username)){
            return true;
        }else {
            throw new BaseException("请正确输入用户名");
        }
    }
}
