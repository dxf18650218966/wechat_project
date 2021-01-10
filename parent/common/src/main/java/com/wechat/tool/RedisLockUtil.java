package com.wechat.tool;

import cn.hutool.core.lang.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 * @author dxf
 * @date 2021/1/9 13:57
 * @version 1.0
 */
@Component
public class RedisLockUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RedisLockUtil.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 加锁(自旋重试三次)
     * @param key 锁唯一标识
     * @param value 作为客户端锁的唯一标识
     * @param effectiveTime 锁的有效期 (单位：秒)
     * @return true 加锁成功
     */
    public boolean lock(String key, String value, long effectiveTime){
        boolean locked = false;
        int tryCount = 3;
        while ( !locked && tryCount > 0) {
            locked = stringRedisTemplate.opsForValue().setIfAbsent(key, value, effectiveTime, TimeUnit.SECONDS);
            if(locked){
                return true;
            }
            tryCount--;
            try {
                // 线程睡眠 0.3S
                Thread.sleep(300);
            } catch (InterruptedException e) {
                LOG.error("RedisLockUtil线程被中断" + Thread.currentThread().getId(), e);
            }
        }
        return locked;
    }

    /**
     * 使用lua脚本释放锁，不会解除别人锁
     * @param key 锁唯一标识
     * @param value 作为客户端锁的唯一标识
     * @return true 释放锁成功
     */
    public boolean unlockLua(String key, String value) {
        if (key == null || value == null){
            return false;
        }
        // lua脚本： redis中key对应的value要等于传递过来的value值，避免释放掉别人的锁
        String unlockLua = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript(unlockLua ,Long.class);
        // 执行lua脚本
        Object result = stringRedisTemplate.execute(redisScript, Arrays.asList(key), value);
        return result.equals(1L);
    }
}
