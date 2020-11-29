package com.wechat.wx.service;

import com.wechat.tool.RedisUtil;
import com.wechat.wx.entity.GzhInfoBean;
import com.wechat.wx.entity.XcxInfoBean;
import com.wechat.wx.mapper.GzhInfoMapper;
import com.wechat.wx.mapper.XcxInfoMapper;
import com.wechat.common.define.RedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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

    @Autowired
    private GzhInfoMapper gzhInfoMapper;
    @Autowired
    private XcxInfoMapper xcxInfoMapper;
    @Autowired
    private WxInterfaceCallService wxInterfaceCallService;
    @Autowired
    private RedisUtil redisUtil;

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
        // 获取公众号 access_token
        List<GzhInfoBean> gzhInfoList = gzhInfoMapper.selectList(null);
        if(gzhInfoList != null){
            gzhInfoList.forEach(gzhInfo ->{
                wxInterfaceCallService.gzhAccessToken(gzhInfo.getAppId(), gzhInfo.getAppSecret(), gzhInfo.getOriginalId());

                // 存入缓存，便于操作 ( key:项目)
                HashMap<String, Object> hash = new HashMap<>(16);
                hash.put(RedisKeyConst.GZH_APPID , gzhInfo.getAppId());
                hash.put(RedisKeyConst.GZH_APP_SECRET , gzhInfo.getAppSecret());
                hash.put(RedisKeyConst.PROJECT_NAME , gzhInfo.getProjectName());
                redisUtil.set(gzhInfo.getProject(), hash);
            });
        }

        // 获取小程序 access_token
        List<XcxInfoBean> xcxInfoList = xcxInfoMapper.selectList(null);
        if(xcxInfoList != null){
            xcxInfoList.forEach(xcxInfo -> {
                wxInterfaceCallService.xcxAccessToken(xcxInfo.getAppId(), xcxInfo.getAppSecret());

                // 存入缓存，便于操作
                HashMap<String, Object> hash = new HashMap<>(16);
                hash.put(RedisKeyConst.XCX_APPID , xcxInfo.getAppId());
                hash.put(RedisKeyConst.XCX_APP_SECRET , xcxInfo.getAppSecret());
                hash.put(RedisKeyConst.PROJECT_NAME , xcxInfo.getProjectName());
                redisUtil.set(xcxInfo.getProject(), hash);
            });
        }
    }
}
