package com.wechat;

import cn.hutool.core.lang.Console;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechat.constant.SystemConst;
import com.wechat.crypto.Base64Util;
import com.wechat.crypto.MD5Util;
import com.wechat.file.minio.service.MinioService;
import com.wechat.model.BusinessCode;
import com.wechat.tool.DateUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.wx.entity.AutoReplyBean;
import com.wechat.wx.mapper.AutoReplyMapper;
import com.wechat.wx.mapper.UserInfoMapper;
import com.wechat.wx.model.WxUserInfo;
import com.wechat.wx.util.AESUtils;
import com.wechat.wx.util.MaterialImgUtil;
import io.minio.MinioClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    MinioService minioService;

    @Test
    public void test() throws Exception{
        //String cardId = userInfoMapper.selectCardIdByPhone("18650218966");
         minioService.putObject("https://web-cdn.agora.io/website-files/images/homepage-solution-xiaotiancai.png");


    }

}
