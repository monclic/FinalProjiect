package com.mon.fpc.utils.redis;



import com.mon.fpc.core.exception.BaseException;
import com.mon.fpc.utils.ParamUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author Created by mon
 * @description
 */
@SuppressWarnings("unchecked")
@Component
public class RedisUtil implements ApplicationContextAware {

    private static RedisTemplate redisTemplate;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);
    }

    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static <T> Optional<T> get(RedisKeyInterface keyEnum, String key) {
        Optional<T> optional = Optional.ofNullable((T) redisTemplate.opsForValue().get(buildKey(keyEnum, key)));
        return optional;
    }

    public static <T> T get(RedisKeyInterface keyEnum, String key, Supplier<T> supplier) {
        Optional<T> v = get(keyEnum, key);
        if (!v.isPresent()) {
            T t = supplier.get();
            if (t != null) {
                //若为null，则不保存
                if (t instanceof Collection) {
                    if (!ParamUtil.isBlank((Collection) t)) {
                        //若为空集合，则不保存
                        set(keyEnum, key, t);
                    }
                } else {
                    //若不为集合，则保存
                    set(keyEnum, key, t);
                }
            }
            return t;
        } else {
            return v.get();
        }
    }

    public static <T> T get(RedisKeyInterface keyEnum, String key, Supplier<T> supplier, long time, TimeUnit timeUnit) {
        Optional<T> v = get(keyEnum, key);
        if (!v.isPresent()) {
            T t = supplier.get();
            if (t != null) {
                //若为null，则不保存
                if (t instanceof Collection) {
                    if (!ParamUtil.isBlank((Collection) t)) {
                        //若为空集合，则不保存
                        set(keyEnum, key, t, time, timeUnit);
                    }
                } else {
                    //若不为集合，则保存
                    set(keyEnum, key, t, time, timeUnit);
                }
            }
            return t;
        } else {
            return v.get();
        }
    }

    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static void set(RedisKeyInterface keyEnum, String key, Object value) {
        redisTemplate.opsForValue().set(buildKey(keyEnum, key), value);
    }

    public static void set(String key, Object value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public static void set(RedisKeyInterface keyEnum, String key, Object value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(buildKey(keyEnum, key), value, time, timeUnit);
    }

    public static void delete(RedisKeyInterface keyEnum, String key) {
        redisTemplate.delete(buildKey(keyEnum, key));
    }

    public static void vagueDelete(RedisKeyInterface keyEnum, String key) {
        Set keys = redisTemplate.keys(buildKey(keyEnum, key) + "*");
        redisTemplate.delete(keys);
    }
    //若当前key的剩余时间小于界限值，则更新value
    public static void setEachTimes(RedisKeyInterface keyEnum, String key, Object value, long time, TimeUnit timeUnit) {
        Long expire = redisTemplate.getExpire(buildKey(keyEnum, key), timeUnit);//单位 timeUnit
        Long hours=12L;//12小时
        if (expire!=null && expire<=hours){
            set(keyEnum,key,value,time,timeUnit);
        }
    }
    public static void delete(String key) {
        redisTemplate.delete(key);
    }

    public static void delete(RedisKeyInterface keyEnum) {
        Set<String> keys = redisTemplate.keys(buildPre(keyEnum) + ":*");
        redisTemplate.delete(keys);
    }

    public static boolean getLock(RedisKeyInterface keyEnum, String key, long millisecond) {
        String finalKey = buildKey(keyEnum, key);
        return redisTemplate.opsForValue().setIfAbsent(finalKey, "lock", millisecond, TimeUnit.MILLISECONDS);
    }

    public static void releaseLock(RedisKeyInterface keyEnum, String key) {
        String finalKey = buildKey(keyEnum, key);
        redisTemplate.delete(finalKey);
    }

    public static StringBuilder buildPre(RedisKeyInterface keyEnum) {
        StringBuilder sb = new StringBuilder();
        if (RedisKeyInterface.RedisDeleteType.NOT_DELETE.equals(keyEnum.getDeleteType())) {
            sb.append(RedisKeyInterface.RedisDeleteType.NOT_DELETE.toString()).append(":");
        } else {
            sb.append(RedisKeyInterface.RedisDeleteType.DELETE.toString()).append(":");
        }
        if (!ParamUtil.isBlank(keyEnum.getFolder())) {
            sb.append(keyEnum.getFolder());
        }
        return sb;
    }

    private static String buildKey(RedisKeyInterface keyEnum, String key) {
        StringBuilder sb = buildPre(keyEnum);
        return sb.append(":").append(key).toString();
    }

    public static <T> List<T> getAllValues(RedisKeyInterface keyEnum) {

        Set<String> keys = redisTemplate.keys(buildPre(keyEnum) + ":*");
        if (!ParamUtil.isBlank(keys)) {
            List list = new ArrayList(keys.size());
            for (String key : keys) {
                T t = (T) redisTemplate.opsForValue().get(key);
                list.add(t);
            }
            return list;
        }
        return new ArrayList();

    }

    public static <T> List<T> getKeysByGroup(RedisKeyInterface keyEnum) {
        Set<T> keys = redisTemplate.keys(buildPre(keyEnum) + ":*");
        if (!ParamUtil.isBlank(keys)) {
            return new ArrayList<>(keys);
        }
        return new ArrayList();
    }

    //chen

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public static boolean exists(RedisKeyInterface keyEnum,String key) {
        Boolean exists = redisTemplate.hasKey(buildKey(keyEnum,key));
        if (exists == null){
            return false;
        }
        return exists;
    }

    //---------------------------------------------集合 Set-------------------------------------------------------------
    /**
     * 集合添加元素
     */
    public static void setAdd(RedisKeyInterface keyEnum,String key) {
        redisTemplate.opsForSet().add(keyEnum.getFolder(),key);
    }


    //---------------------------------------------有序集合 ZSet---------------------------------------------------------
    /**
     * 有序集合添加元素
     * 若当前key不存在对应有序集合，则创建该集合
     * 若当前key存在但对应类型不是有序集合则添加失败并报错
     * 若当前value存在则会只更新score
     * @param key
     * @param value
     * @param score ：有序集合的分数，通过分数来为集合中的成员进行从小到大的排序 ，可以重复
     */
    public static void addZset(RedisKeyInterface keyEnum,String key, Object value, double score) {
        try {
             redisTemplate.opsForZSet().add(buildKey(keyEnum,key), value, score);
        } catch (Exception e) {
            throw new BaseException("该key:{0} 已存在且不为有序集合类型",key);
        }
    }


    /**
     * 获取有序集合数量 key不存在，返回0；存在，返回集合的个数
     */
    public static Long countZset(RedisKeyInterface keyEnum,String key) {
        try {
            return redisTemplate.opsForZSet().size(buildKey(keyEnum,key));
        } catch (Exception e) {
            throw new BaseException("[RedisUtils.countZset] [error] [key is {}]", buildKey(keyEnum,key), e);
        }
    }

    /**
     * 获取指定范围内的有序集合
     * key存在，返回集合的对应位置的元素，区间左开右闭。start从0开始，end传-1表示查询所有
     * 常用于分页查询
     */
    public static Set<Object> rangeZset(RedisKeyInterface keyEnum,String key, Long start, Long end) {
        try {
            return redisTemplate.opsForZSet().range(buildKey(keyEnum,key), start, end);
        } catch (Exception e) {
            throw new BaseException("[RedisUtils.rangeZset] [error] [key is {},start is {},end is {}]", buildKey(keyEnum,key), start, end, e);
        }
    }

    /**
     * 移除有序集合内的指定元素,未查询到对应的key和value，返回0，否则返回1
     */
    public static Long removeZset(RedisKeyInterface keyEnum,String key, Object value) {
        return redisTemplate.opsForZSet().remove(buildKey(keyEnum,key), value);
    }

    /**
     * 获取有序集合内的指定元素的score
     */
    public static Double score(RedisKeyInterface keyEnum,String key, Object value) {
        return redisTemplate.opsForZSet().score(buildKey(keyEnum,key), value);
    }
}
