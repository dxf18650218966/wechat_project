package com.wechat.wx.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wechat.constant.SystemConst;
import com.wechat.model.ErrCode;
import com.wechat.model.HttpClientResult;
import com.wechat.model.ResponseModel;
import com.wechat.model.ResponseUtil;
import com.wechat.tool.HttpUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.wx.model.RedisKeyConst;
import com.wechat.wx.model.WechatUrlConst;
import com.wechat.wx.model.WxCommonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 微信接口对接
 * @Author dai
 * @Date 2020/11/22 
 */
@Service
public class WxInterfaceCallService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxInterfaceCallService.class);
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取公众号 Access token（接口调用凭据）
     * @param appid
     * @param appSecret
     * @param originalId  原始公众号id
     */
    public void gzhAccessToken(String appid, String appSecret, String originalId){
        HashMap<String, Object> requestMap = new HashMap<>(16);
        requestMap.put("grant_type","client_credential");
        requestMap.put("appid", appid);
        requestMap.put("secret", appSecret);
        // 发送请求
        HttpClientResult response = HttpUtil.get(WechatUrlConst.ACCESS_TOKEN , requestMap);

        // 处理响应
        if (HttpClientResult.businessResult(response)){
            JSONObject jsonObject = JSON.parseObject(response.getData());
            String errcode = jsonObject.getString( WxCommonConst.ERRCODE );
            if(StrUtil.isNotBlank(errcode)){
                // 微信会返回错误码等信息
                LOGGER.error("获取公众号 access_token 错误:::{}",jsonObject);
            }else {
                String accessToken = jsonObject.getString("access_token");
                // 用于登录 (token_appid , access_token)
                redisUtil.set(RedisKeyConst.TOKEN + appid, accessToken);
                // 用于自动回复:  (原始id , access_token )
                redisUtil.set(RedisKeyConst.ORIGINAL_ID + originalId, accessToken);
            }
        };
    }

    /**
     * 获取小程序 Access token （接口调用凭据）
     * @param appid
     * @param appSecret
     */
    public void xcxAccessToken(String appid, String appSecret){
        HashMap<String, Object> requestMap = new HashMap<>(16);
        requestMap.put("grant_type","client_credential");
        requestMap.put("appid", appid);
        requestMap.put("secret", appSecret);
        // 发送请求：  获取 Access token
        HttpClientResult response = HttpUtil.get(WechatUrlConst.ACCESS_TOKEN, requestMap);

        // 处理响应
        if (HttpClientResult.businessResult(response)){
            JSONObject jsonObject = JSON.parseObject(response.getData());
            String errcode = jsonObject.getString( WxCommonConst.ERRCODE );
            if(StrUtil.isNotBlank(errcode)){
                // 微信会返回错误码等信息
                LOGGER.error("获取小程序 access_token 错误:::{}",jsonObject);
            }else {
                String accessToken = jsonObject.getString("access_token");
                // 用于登录 (token_appid , access_token)
                redisUtil.set(RedisKeyConst.TOKEN + appid, accessToken);
            }
        };
    }

    /**
     * 登录凭证校验
     * @param appid 小程序 appId
     * @param appSecret 小程序 appSecret
     * @param code 登录时获取的 code
     * @return
     *   openid：用户唯一标识
     *   session_key：会话密钥
     *   unionid：用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    public ResponseModel jscode2session(String appid, String appSecret, String code){
        // 发起请求
        HashMap<String, Object> requestMap = new HashMap<>(16);
        requestMap.put("appid", appid);
        requestMap.put("secret", appSecret);
        requestMap.put("js_code", code);
        requestMap.put("grant_type", "authorization_code");
        HttpClientResult response = HttpUtil.get(WechatUrlConst.JSCODE_2_SESSION , requestMap);

        // 处理响应
        if (HttpClientResult.businessResult(response)){
            JSONObject jsonObject = JSON.parseObject(response.getData());
            String errcode = jsonObject.getString( WxCommonConst.ERRCODE );
            if(StrUtil.isNotBlank(errcode)){
                // 微信返回错误码等信息
                LOGGER.error("获取登录凭证错误，请求参数:::{}",requestMap);
                LOGGER.error("获取登录凭证错误，响应结果:::{}",jsonObject);
                return ResponseUtil.resultFail(errcode, jsonObject.getString( WxCommonConst.ERRMSG ));
            }else {
                return ResponseUtil.resultSuccess(jsonObject);
            }
        }else{
            LOGGER.error("获取登录凭证错误，响应结果:::{}",response);
            return ResponseUtil.returnFail(response.getCode()+"", response.getData());
        }
    }


    /**
     * 创建公众号菜单
     * @param jsonObject 自定义菜单数据
     * @param accessToken 令牌
     * @return
     */
    public Object createCustomMenu(JSONObject jsonObject, String gzhAppid, String accessToken) {
        // 发起请求
        String url = String.format(WechatUrlConst.CREATE_MENU , accessToken);
        HttpClientResult response = HttpUtil.post(url, jsonObject.toJSONString());

        // 处理响应
        if (HttpClientResult.businessResult(response)){
            JSONObject  json = JSON.parseObject(response.getData());
            String errcode = json.getString(WxCommonConst.ERRCODE);
            String errmsg = json.getString(WxCommonConst.ERRMSG);
            if(! SystemConst.ZERO .equals(errcode)){
                LOGGER.error("创建菜单失败，请求参数:::{}" ,jsonObject.toJSONString());
                LOGGER.error("创建菜单失败，响应结果:::{}" ,response);
                return ResponseUtil.resultFail(errcode,errmsg);
            }else {
                return ResponseUtil.resultSuccess(errmsg);
            }
        }else{
            LOGGER.error("创建菜单失败，响应结果:::{}",response);
            return ResponseUtil.resultFail(ErrCode.SYSTEM_INTERNAL_ERROR);
        }
    }

    /**
     * 通过code换取网页授权access_token（用户的身份验证访问令牌）
     * @param appid 公众号的唯一标识
     * @param appSecret 公众号的appsecret
     * @param code 临时凭证
     * @return
     *   access_token：网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     *   openid：用户唯一标识
     *   unionid：用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    public ResponseModel oauthAccessToken(String appid, String appSecret, String code) {
        // 发起请求
        HashMap<String, Object> requestMap = new HashMap<>(16);
        requestMap.put("appid", appid);
        requestMap.put("secret", appSecret);
        requestMap.put("code", code);
        requestMap.put("grant_type", "authorization_code");
        HttpClientResult response = HttpUtil.get(WechatUrlConst.OAUTH_ACCESS_TOKEN , requestMap);

        // 处理响应
        if (HttpClientResult.businessResult(response)){
            JSONObject jsonObject = JSON.parseObject(response.getData());
            String errcode = jsonObject.getString( WxCommonConst.ERRCODE );
            if(StrUtil.isNotBlank(errcode)){
                // 微信返回错误码等信息
                LOGGER.error("通过code换取网页授权access_token，请求参数:::{}",requestMap);
                LOGGER.error("通过code换取网页授权access_token，响应结果:::{}",jsonObject);
                return ResponseUtil.resultFail(errcode, jsonObject.getString( WxCommonConst.ERRMSG ));
            }else {
                return ResponseUtil.resultSuccess(jsonObject);
            }
        }else{
            LOGGER.error("通过code换取网页授权access_token, 响应结果:::{}",response);
            return ResponseUtil.returnFail(response.getCode()+"", response.getData());
        }
    }

    /**
     * 公众号拉取用户信息
     * @param openid 公众号用户唯一标识
     * @param accessToken 网页授权
     * @return
     *    openid: 用户的唯一标识
     *    nickname: 用户昵称
     *    sex: 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     *    headimgurl: 用户头像
     */
    public ResponseModel userInfo(String openid, String accessToken) {
        // 发起请求
        HashMap<String, Object> requestMap = new HashMap<>(16);
        requestMap.put("openid", openid);
        requestMap.put("access_token", accessToken);
        requestMap.put("lang", "zh_CN");
        HttpClientResult response = HttpUtil.get(WechatUrlConst.USER_INFO , requestMap);

        // 处理响应
        if (HttpClientResult.businessResult(response)){
            JSONObject jsonObject = JSON.parseObject(response.getData());
            String errcode = jsonObject.getString( WxCommonConst.ERRCODE );
            if(StrUtil.isNotBlank(errcode)){
                // 微信返回错误码等信息
                LOGGER.error("公众号拉取用户信息，请求参数:::{}",requestMap);
                LOGGER.error("公众号拉取用户信息，响应结果:::{}",jsonObject);
                return ResponseUtil.resultFail(errcode, jsonObject.getString( WxCommonConst.ERRMSG ));
            }else {
                return ResponseUtil.resultSuccess(jsonObject);
            }
        }else{
            LOGGER.error("公众号拉取用户信息, 响应结果:::{}",response);
            return ResponseUtil.returnFail(response.getCode()+"", response.getData());
        }
    }
}
