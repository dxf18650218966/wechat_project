package com.wechat.mall.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wechat.common.define.RedisKeyConst;
import com.wechat.mall.entity.QuickEntryBean;
import com.wechat.mall.entity.SlideShowBean;
import com.wechat.mall.mapper.QuickEntryMapper;
import com.wechat.mall.mapper.SlideShowMapper;
import com.wechat.mall.model.HomePageInitModel;
import com.wechat.model.ResponseUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.wx.util.MaterialImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小程序商城首页
 * @author dxf
 * @date 2020/12/7 20:18
 * @version 1.0
 */
@Service
public class HomePageService {
    @Autowired
    private QuickEntryMapper quickEntryMapper;
    @Autowired
    private SlideShowMapper slideShowMapper;
    @Autowired
    private MaterialImgUtil mediaUploadUtil;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 初始化商城首页
     * @param projectId 项目id
     * @return 初始化数据
     */
    public Object getInit(String projectId) {
        HomePageInitModel homePageInitModel = new HomePageInitModel();

        String quickEntryKey = RedisKeyConst.QUICK_ENTRY + projectId;
        String slideShowKey = RedisKeyConst.SLIDE_SHOW + projectId;
        String quickEntryJsonStr = redisUtil.get(quickEntryKey);
        String slideShowKeyJsonStr = redisUtil.get(slideShowKey);


        // ::: 首页快捷入口配置
        if(StrUtil.isBlank(quickEntryJsonStr)){
            List<QuickEntryBean> quickEntryList = quickEntryMapper.selectByProject(projectId);
            quickEntryList.forEach(quickEntry -> {
                quickEntry.setLogo( mediaUploadUtil.getImgUrl(quickEntry.getLogo(), MaterialImgUtil.LOCAL ));
            });
            homePageInitModel.setQuickEntryBeanList(quickEntryList);
            redisUtil.set(quickEntryKey, JSON.toJSONString(quickEntryList));
        }else {
            // JSON字符串 转--> 对象集合
            List<QuickEntryBean> quickEntryList = JSON.parseArray(quickEntryJsonStr,QuickEntryBean.class);
            homePageInitModel.setQuickEntryBeanList(quickEntryList);
        }


        // ::: 首页轮播图片
        if(StrUtil.isBlank(slideShowKeyJsonStr)){
            List<SlideShowBean> slideShowList = slideShowMapper.selectByProject(projectId);
            slideShowList.forEach(slideShow -> {
                slideShow.setImg( mediaUploadUtil.getImgUrl(slideShow.getImg(), MaterialImgUtil.LOCAL ));
            });
            homePageInitModel.setSlideShowBeanList(slideShowList);
            redisUtil.set(slideShowKey, JSON.toJSONString(slideShowList));
        }else {
            List<SlideShowBean> slideShowList = JSON.parseArray(quickEntryJsonStr,SlideShowBean.class);
            homePageInitModel.setSlideShowBeanList(slideShowList);
        }


        // java对象转 json对象
        JSONObject data = (JSONObject) JSON.toJSON(homePageInitModel);
        return ResponseUtil.resultSuccess(data);
    }
}
