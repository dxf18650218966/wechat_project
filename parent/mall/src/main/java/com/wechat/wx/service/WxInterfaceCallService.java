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
import org.springframework.data.redis.core.StringRedisTemplate;
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
        HttpClientResult httpClientResult = HttpUtil.get(WechatUrlConst.ACCESS_TOKEN , requestMap);

        // 处理请求结果
        if (SystemConst.SUCCES_CODE.equals(httpClientResult.getCode()) && StrUtil.isNotBlank(httpClientResult.getData())){
            JSONObject jsonObject = JSON.parseObject(httpClientResult.getData());
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
                // 用于获取公众号AppSecret （appId ，appSecret）
                redisUtil.set(appid, appSecret);
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
        HttpClientResult httpClientResult = HttpUtil.get(WechatUrlConst.ACCESS_TOKEN, requestMap);

        // 处理请求结果
        if (SystemConst.SUCCES_CODE.equals(httpClientResult.getCode()) && StrUtil.isNotBlank(httpClientResult.getData())){
            JSONObject jsonObject = JSON.parseObject(httpClientResult.getData());
            String errcode = jsonObject.getString( WxCommonConst.ERRCODE );
            if(StrUtil.isNotBlank(errcode)){
                // 微信会返回错误码等信息
                LOGGER.error("获取小程序 access_token 错误:::{}",jsonObject);
            }else {
                String accessToken = jsonObject.getString("access_token");
                // 用于登录 (token_appid , access_token)
                redisUtil.set(RedisKeyConst.TOKEN + appid, accessToken);
                // 用于获取小程序AppSecret （appId ，appSecret）
                redisUtil.set(appid,appSecret);
            }
        };
    }

    /**
     * 登录凭证校验
     * @param appid 小程序 appId
     * @param appSecret 小程序 appSecret
     * @param code 登录时获取的 code
     * @return
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
        if (SystemConst.SUCCES_CODE .equals(response.getCode()) && StrUtil.isNotBlank(response.getData())){
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

        // 处理响应结果
        if (SystemConst.SUCCES_CODE .equals(response.getCode()) && StrUtil.isNotBlank(response.getData())){
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
}
