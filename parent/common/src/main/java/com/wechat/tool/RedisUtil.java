package com.wechat.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    // -----------------------------------  通用操作  -----------------------------------------------
    /**
     * 删除指定key
     */
    public boolean del(String key){
        return redisTemplate.delete(key);
    }
    /**
     * 设置有效期
     * @param key
     * @param timeout
     * @param unit
     */
    public Boolean expire(String key, long timeout, TimeUnit unit){
        return redisTemplate.expire(key, timeout, unit);
    }
    /**
     * 获取有效期
     * @param key
     * @return
     */
    public long getExpire(String key){
        return redisTemplate.opsForValue().getOperations().getExpire(key);
    }


    // -----------------------------------  String 操作类型  ----------------------------------------
    /**
     * 添加键值对
     */
    public void set(String key, String value){  stringRedisTemplate.opsForValue().set(key, value);  }
    /**
     * 添加键值对并设置有效期
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


    // -------------------------------------  Hash 操作类型  ----------------------------------
    public void set(String key, String hashKey, Object value){
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public void set(String key, Map<String, Object> map){
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Object getObject(String key, String hashKey){
       return redisTemplate.opsForHash().get(key, hashKey);
    }
    public String get(String key, String hashKey){
        return (String) redisTemplate.opsForHash().get(key, hashKey);
    }


}
