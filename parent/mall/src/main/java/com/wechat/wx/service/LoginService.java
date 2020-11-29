package com.wechat.wx.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wechat.common.define.RedisKeyConst;
import com.wechat.crypto.AESUtils;
import com.wechat.tool.DateUtil;
import com.wechat.tool.RandomUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.wx.entity.UserAccountBean;
import com.wechat.wx.entity.UserInfoBean;
import com.wechat.wx.mapper.UserAccountMapper;
import com.wechat.wx.mapper.UserInfoMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;


/**
 * 公众号、小程序注册登录
 * @Author dai
 * @Date 2020/11/21 
 */
@Service
public class LoginService {
    private static final Logger LOG = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 小程序获取手机号
     * @param encryptedData 加密数据
     * @param iv 加密算法的初始向量
     * @param sessionKey 会话秘钥
     * @return 手机号
     */
    public String getPhoneNumber(String encryptedData, String iv, String sessionKey) {
        String phoneNumber = "";
        try {
            String result = AESUtils.decrypt(encryptedData, sessionKey, iv);
            JSONObject jsonObject = (JSONObject) JSON.parse(result);
            phoneNumber = (String) jsonObject.get("phoneNumber");
            return phoneNumber;
        }catch (Exception ex){
            return "";
        }
    }

    /**
     * 通过手机号，判断用户是否已存在
     * @param phoneNumber 手机号
     * @return 存在返回true
     */
    public Boolean existsUserByPhone(String phoneNumber) {
        String cardId = userInfoMapper.selectCardIdByPhone(phoneNumber);
        if(StrUtil.isBlank(cardId)){
            return false;
        }
        // 清用户缓存
        redisUtil.del(RedisKeyConst.CARD_ID + cardId);
        return true;
    }

    /**
     * 通过手机号，判断用户是否已存在
     * @param openId 公众号openId
     * @return 存在返回true
     */
    public Boolean existsUserByOpenId(String openId) {
        String cardId = userInfoMapper.selectCardIdByOpenId(openId);
        if(StrUtil.isBlank(cardId)){
            return false;
        }
        // 清用户缓存
        redisUtil.del(RedisKeyConst.CARD_ID + cardId);
        return true;
    }

    /**
     * 注册会员信息
     * @param userInfoBean 会员信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean registeredUserInfo(UserInfoBean userInfoBean) {
        userInfoBean.setId(RandomUtil.randomUUID());
        // 会员卡号
        String cardId = RandomUtil.cardId();
        userInfoBean.setCardId(cardId);
        // 注册时间
        userInfoBean.setRegistrationTime(DateUtil.date());
        // 性别
        switch (userInfoBean.getGender()){
            case "1":
                userInfoBean.setGenderName("男性");
                break;
            case "2":
                userInfoBean.setGenderName("女性");
                break;
            default :
                userInfoBean.setGenderName("未知");
        }

        // :: 账户信息
        UserAccountBean userAccountBean = new UserAccountBean();
        userAccountBean.setId(RandomUtil.randomUUID());
        userAccountBean.setCardId(cardId);
        userAccountBean.setPhone(userInfoBean.getPhone());
        userAccountBean.setIntegral(0);
        userAccountBean.setBalance(BigDecimal.ZERO);
        userAccountBean.setWithoutCode("0");
        userAccountBean.setWithoutCodeName("否");

        int res1 = userInfoMapper.insert(userInfoBean);
        int res2 = userAccountMapper.insert(userAccountBean);
        if(res1==1 && res2==1){
            return true;
        }
        //  手动回滚事务
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }

    /**
     * 更新会员信息
     * @param userInfoBean 会员信息
     */
    public Boolean updateUserInfo(UserInfoBean userInfoBean) {
        // 性别
        switch (userInfoBean.getGender()){
            case "1":
                userInfoBean.setGenderName("男性");
                break;
            case "2":
                userInfoBean.setGenderName("女性");
                break;
            default :
                userInfoBean.setGenderName("未知");
        }
        return userInfoMapper.updateByPhoneNumber(userInfoBean);
    }
}
