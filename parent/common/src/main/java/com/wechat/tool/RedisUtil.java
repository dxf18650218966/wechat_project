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
     * 添加键值对 ( 不管key是否存在，都会添加，覆盖掉value )
     */
    public void set(String key, String value){
        stringRedisTemplate.opsForValue().set(key, value);
    }
    /**
     * 添加键值对 (key 不存在时才添加)
     */
    public Boolean setIfAbsent(String key, String value){
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
    }
    /**
     * 添加键值对并设置有效期 ( 不管key是否存在，都会添加，覆盖掉value )
     * @param key
     * @param value
     * @param timeout 有效时间
     * @param timeUnit 单位
     */
    public void set(String key, String value, long timeout, TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 添加键值对并设置有效期 ( 只有当key不存在时，才会添加 )
     * @param key
     * @param value
     * @param timeout 有效时间
     * @param timeUnit 单位
     */
    public Boolean setIfAbsent(String key, String value, long timeout, TimeUnit timeUnit){
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    public String get(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 自增 (默认自增值：+1)
     * @param key
     * @return 自增后的value值
     * 自增不影响有效期
     */
    public Long increment(String key){
        return stringRedisTemplate.opsForValue().increment(key);
    }

    /**
     * 自增
     * @param key
     * @param delta 自增值
     * @return 自增后的value值
     * 自增不影响有效期
     */
    public Long increment(String key, long delta){
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }
    public Double increment(String key, double delta){
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 自减 (默认自增值：-1)
     * @param key
     * @return 自减后的value值
     * 自减不影响有效期
     */
    public Long decrement(String key){
        return stringRedisTemplate.opsForValue().decrement(key);
    }
    /**
     * 自减
     * @param key
     * @param delta 自减值
     * @return 自减后的value值
     * 自减不影响有效期
     */
    public Long decrement(String key, long delta){
        return stringRedisTemplate.opsForValue().decrement(key,delta);
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

    /**
     * 自增
     * @param key
     * @param hashKey 哈希键
     * @param delta 自增值
     * @return 自增后的value值
     * 自增不影响有效期
     */
    public Long increment(String key, Object hashKey, long delta){
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }
    public Double increment(String key, Object hashKey, double delta){
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }
}
