package com.wechat;

import cn.hutool.core.lang.Console;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechat.minio.service.MinioService;
import com.wechat.tool.DateUtil;
import com.wechat.tool.RandomUtil;
import com.wechat.tool.RedisLockUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.wx.entity.AutoReplyBean;
import com.wechat.wx.mapper.AutoReplyMapper;
import com.wechat.wx.mapper.UserInfoMapper;
import com.wechat.wx.service.WxInterfaceCallService;
import com.wechat.wx.util.MaterialImgUtil;
import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * @Author dai
 * @Date 2020/10/31 
 */
@RunWith(SpringRunner.class)
// 因为business依赖了有主启动类的thirdparty，junit不知道加载哪个启动类，需要在junit的@SpringBootTest注解上指定启动类。
// 否则会报：junit报错Found multiple @SpringBootConfiguration annotated classes
@SpringBootTest(classes = {MallApplication.class})
public class redisTest {
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    MaterialImgUtil mediaUploadUtil;
    @Autowired
    AutoReplyMapper autoReplyMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    MinioService minioService;
    private static final Logger LOGGER = LoggerFactory.getLogger(WxInterfaceCallService.class);
     @Autowired
     private RedisTemplate<String,Object> redisTemplate;
     @Autowired
     private StringRedisTemplate stringRedisTemplate;
     @Autowired
     private RedisLockUtil redisLockUtil;

     @Test
    public void aa(){
         String ggg = redisUtil.get("ggg");
         Console.log(ggg);

     }
}
