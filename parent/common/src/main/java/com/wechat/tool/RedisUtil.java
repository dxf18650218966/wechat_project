package com.wechat.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

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

    public void set(String key , String value){
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置有效期
     * @param key
     * @param value
     * @param timeout 有效时间
     * @param timeUnit 单位
     */
    public void set(String key , String value, long timeout, TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }


    /**
     * 通过 key 获取 value
     */
    public String get(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 通过 key 删除键值对
     * @return successful 返回 true
     */
    public boolean del(String key){
        return stringRedisTemplate.delete(key);
    }


}
