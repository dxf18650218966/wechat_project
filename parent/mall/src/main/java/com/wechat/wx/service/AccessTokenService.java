package com.wechat.wx.service;

import com.wechat.wx.entity.GzhInfoBean;
import com.wechat.wx.entity.XcxInfoBean;
import com.wechat.wx.mapper.GzhInfoMapper;
import com.wechat.wx.mapper.XcxInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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
            });
        }

        // 获取小程序 access_token
        List<XcxInfoBean> xcxInfoList = xcxInfoMapper.selectList(null);
        if(xcxInfoList != null){
            xcxInfoList.forEach(xcxInfo -> {
                wxInterfaceCallService.xcxAccessToken(xcxInfo.getAppId(), xcxInfo.getAppSecret());
            });
        }
    }
}
