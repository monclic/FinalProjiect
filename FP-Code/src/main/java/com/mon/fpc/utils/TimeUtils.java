package com.mon.fpc.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author chen
 * @date 2022-10-28 11:23
 * @description:
 */

public class TimeUtils {

    /**
     * 按照 yyyy-MM-dd HH:mm:ss 格式返回当前时间
     */
    public static String getLocalDateTimeNow(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(now);
    }

    /**
     * 按照 yyyy-MM-dd HH:mm:ss 格式返回时间
     */
    public static String formatTime(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time);
    }

    public static LocalDateTime parseTime( Optional<Object> o){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return o.map(value -> LocalDateTime.parse(value.toString(), formatter)).orElse(null);
    }
}
