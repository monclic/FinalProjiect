package com.mon.fpc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author clic
 * @date 2023-05-12 15:17
 * @description:
 */

public class VerifyUtil {
    //验证邮箱地址是否正确
    public static boolean checkEmail(String email) {
        final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

        return EMAIL_PATTERN.matcher(email).matches();

    }

}
