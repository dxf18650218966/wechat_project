package com.wechat.wx.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wechat.model.HttpClientResult;
import com.wechat.constant.SystemConst;
import com.wechat.tool.HttpUtil;
import com.wechat.wx.entity.GzhInfoBean;
import com.wechat.wx.entity.XcxInfoBean;
import com.wechat.wx.mapper.GzhInfoMapper;
import com.wechat.wx.mapper.XcxInfoMapper;
import com.wechat.wx.model.RedisKeyConst;
import com.wechat.wx.model.WechatUrlConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * 获取 access_token
 * @Author dai
 * @Date 2020/11/3 
 */
@Component
public class AccessTokenService implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(AccessTokenService.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private GzhInfoMapper gzhInfoMapper;
    @Autowired
    private XcxInfoMapper xcxInfoMapper;

    /**
     * 项目启动时获取
     * @param args
     */
    @Override
    public void run(ApplicationArguments args){
        getAccessToken();
    }

    /**
     * 定时获取
     */
    @Scheduled(cron = "0 30 * * * ?")
    public void timingTask() {
        getAccessToken();
    }


    public void getAccessToken(){
        HashMap<String, Object> requestMap = new HashMap<>(16);
        requestMap.put("grant_type","client_credential");

        // 获取公众号 access_token
        List<GzhInfoBean> gzhInfoList = gzhInfoMapper.selectList(null);
        if(gzhInfoList != null){
            gzhInfoList.forEach(gzhInfo ->{
                requestMap.put("appid", gzhInfo.getAppId());
                requestMap.put("secret", gzhInfo.getAppSecret());
                HttpClientResult httpClientResult = HttpUtil.get(WechatUrlConst.ACCESS_TOKEN, requestMap);

                if (SystemConst.SUCCES_CODE.equals(httpClientResult.getCode()) && StrUtil.isNotBlank(httpClientResult.getData())){
                    JSONObject jsonObject = JSON.parseObject(httpClientResult.getData());
                    String errcode = jsonObject.getString("errcode");
                    if(StrUtil.isNotBlank(errcode)){
                        // 微信会返回错误码等信息
                        logger.error("获取公众号 access_token 错误:::{}",jsonObject);
                    }else {
                        String accessToken = jsonObject.getString("access_token");
                        // 用于登录 (token_appid , access_token)
                        stringRedisTemplate.opsForValue().set(RedisKeyConst.TOKEN + gzhInfo.getAppId(), accessToken);
                        // 用于自动回复:  (原始id , access_token )
                        stringRedisTemplate.opsForValue().set(RedisKeyConst.ORIGINAL_ID + gzhInfo.getOriginalId(), accessToken);
                    }
                };
            });
        }

        // 获取小程序 access_token
        List<XcxInfoBean> xcxInfoList = xcxInfoMapper.selectList(null);
        if(xcxInfoList != null){
            xcxInfoList.forEach(xcxInfoBean -> {
                requestMap.put("appid", xcxInfoBean.getAppId());
                requestMap.put("secret", xcxInfoBean.getAppSecret());
                HttpClientResult httpClientResult = HttpUtil.get(WechatUrlConst.ACCESS_TOKEN, requestMap);

                if (SystemConst.SUCCES_CODE.equals(httpClientResult.getCode()) && StrUtil.isNotBlank(httpClientResult.getData())){
                    JSONObject jsonObject = JSON.parseObject(httpClientResult.getData());
                    String errcode = jsonObject.getString("errcode");
                    if(StrUtil.isNotBlank(errcode)){
                        // 微信会返回错误码等信息
                        logger.error("获取小程序 access_token 错误:::{}",jsonObject);
                    }else {
                        String accessToken = jsonObject.getString("access_token");
                        // 用于登录 (token_appid , access_token)
                        stringRedisTemplate.opsForValue().set(RedisKeyConst.TOKEN + xcxInfoBean.getAppId(), accessToken);
                    }
                };
            });
        }
    }
}
