package com.mon.fpc.utils;


/**
 * 参数工具类
 *
 * @author mon
 */
public class ParamUtil {

    /**
     * 判断一个集合对象是否为null或者为空集合
     *
     * @return
     */
    public static boolean isBlank(java.util.Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * 判断一个字符串是否为null或者为空
     *
     * @return
     */
    public static boolean isBlank(String string) {
        return string == null || string.trim().equals("");
    }

    /**
     * 判断数字是否为null或者为0
     *
     * @return
     */
    public static boolean isBlank(Number num) {
        return num == null || num.equals(0);
    }

    /**
     * 判断一个long[]是否为null或者长度为0
     *
     * @return
     */
    public static boolean isBlank(Long[] ids) {
        return ids == null || ids.length == 0;
    }

    /**
     * 判断一个String[]是否为null或者长度为0
     *
     * @return
     */
    public static boolean isBlank(String[] strings) {
        return strings == null || strings.length == 0;
    }

    /**
     * 判断一个Object是否为null
     *
     * @return
     */
    public static boolean isBlank(Object obj) {
        return obj == null;
    }

}

