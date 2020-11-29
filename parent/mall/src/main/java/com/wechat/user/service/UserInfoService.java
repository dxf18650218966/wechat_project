package com.wechat.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wechat.common.define.RedisKeyConst;
import com.wechat.model.ResponseModel;
import com.wechat.model.ResponseUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.wx.entity.UserAccountBean;
import com.wechat.wx.entity.UserInfoBean;
import com.wechat.wx.mapper.UserAccountMapper;
import com.wechat.wx.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 用户信息管理
 * @Author dai
 * @Date 2020/11/28 
 */
@Service
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 初始化用户信息
     * @param cardId
     */
    public ResponseModel initUserInfo(String cardId) {
        UserInfoBean userInfoBean = userInfoMapper.selectByCardId(cardId);
        UserAccountBean userAccountBean = userAccountMapper.selectByCardId(cardId);

        //::: 写入缓存
        HashMap<String, Object> hashMap = new HashMap<>(16);
        // 用户信息
        hashMap.put(RedisKeyConst.USER_INFO , JSON.toJSONString(userInfoBean));
        // 账户余额
        hashMap.put(RedisKeyConst.BALANCE , userAccountBean.getBalance());
        // 账户积分
        hashMap.put(RedisKeyConst.INTEGRAL , userAccountBean.getIntegral());
        String key = RedisKeyConst.CARD_ID + cardId;
        redisUtil.set(key, hashMap);
        redisUtil.expire(key, 1, TimeUnit.DAYS );

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RedisKeyConst.USER_INFO , JSON.toJSONString(userInfoBean));
        jsonObject.put(RedisKeyConst.BALANCE , userAccountBean.getBalance());
        jsonObject.put(RedisKeyConst.INTEGRAL , userAccountBean.getIntegral());
        return ResponseUtil.resultSuccess(jsonObject);
    }
}
