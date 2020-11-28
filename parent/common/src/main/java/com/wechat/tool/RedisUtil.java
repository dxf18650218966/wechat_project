package com.wechat.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 * @Author dai
 * @Date 2020/11/21 
 */
@Component
public class RedisUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // -----------------------------------  String 操作类型  ----------------------------------------

    public void set(String key, String value){
        stringRedisTemplate.opsForValue().set(key, value);
    }


    /**
     * 设置有效期
     * @param key
     * @param value
     * @param timeout 有效时间
     * @param timeUnit 单位
     */
    public void set(String key, String value, long timeout, TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public String get(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    public boolean del(String key){
        return stringRedisTemplate.delete(key);
    }

    // -------------------------------------  Hash 操作类型  ----------------------------------

    public void set(String key, Object hashKey, Object value){
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    public void set(String key, Map<Object, Object> map){
        stringRedisTemplate.opsForHash().putAll(key, map);
    }

    public Object get(String key, Object hashKey){
       return stringRedisTemplate.opsForHash().get(key, hashKey);
    }
    public String get(String key, String hashKey){
        return (String) stringRedisTemplate.opsForHash().get(key, hashKey);
    }


}
