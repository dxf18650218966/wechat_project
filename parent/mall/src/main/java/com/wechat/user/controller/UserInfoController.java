package com.wechat.user.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wechat.common.define.RedisKeyConst;
import com.wechat.model.ErrCode;
import com.wechat.model.ResponseUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户信息管理
 * @Author dai
 * @Date 2020/11/28 
 */
@RestController
@CrossOrigin
@RequestMapping("user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("init")
    public Object initUserInfo(@Validated @RequestBody Map<String,String> requestrMap){
        String cardId = requestrMap.get("card_id");
        String sessionToken = requestrMap.get("session_token");
        if(! StrUtil.isAllNotBlank( sessionToken, cardId)){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }
        if( StrUtil.isBlank(redisUtil.get(sessionToken))){
            return ResponseUtil.returnFail( ErrCode.PLEASE_LOG_IN );
        }

        String key = RedisKeyConst.CARD_ID + cardId;
        if (StrUtil.isNotBlank(redisUtil.get(key))) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(RedisKeyConst.USER_INFO , redisUtil.get(key, RedisKeyConst.USER_INFO ));
            jsonObject.put(RedisKeyConst.BALANCE , redisUtil.get(key, RedisKeyConst.BALANCE ));
            jsonObject.put(RedisKeyConst.INTEGRAL , redisUtil.get(key, RedisKeyConst.INTEGRAL ));
            return ResponseUtil.resultSuccess(jsonObject);
        }
        return userInfoService.initUserInfo(cardId);
    }

}
