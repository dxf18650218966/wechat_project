package com.wechat;

import cn.hutool.core.lang.Console;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechat.tool.DateUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.wx.entity.AutoReplyBean;
import com.wechat.wx.mapper.AutoReplyMapper;
import com.wechat.wx.model.WxUserInfo;
import com.wechat.wx.util.MaterialImgUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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
    private  RedisTemplate<String,String> redisTemplate;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    MaterialImgUtil mediaUploadUtil;
    @Autowired
    AutoReplyMapper autoReplyMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Test
    public void test() throws Exception{


        Console.log( redisUtil.del("43534"));
    }
}
