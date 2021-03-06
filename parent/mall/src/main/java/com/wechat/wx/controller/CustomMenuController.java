package com.wechat.wx.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wechat.model.ErrCode;
import com.wechat.model.ResponseUtil;
import com.wechat.tool.ObjectUtil;
import com.wechat.tool.RedisUtil;
import com.wechat.wx.model.Level1MenuModel;
import com.wechat.common.define.RedisKeyConst;
import com.wechat.wx.service.CustomMenuService;
import com.wechat.wx.service.WxInterfaceCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 自定义菜单
 * @Author dai
 * @Date 2020/11/14 
 */
@RestController
@CrossOrigin
@RequestMapping("menu")
public class CustomMenuController {
    @Autowired
    private CustomMenuService customMenuService;
    @Autowired
    private WxInterfaceCallService wxInterfaceCallService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 创建自定义菜单
     * @return
     */
    @PostMapping("create_menu")
    public Object createMenu(@RequestBody HashMap<String,String> requestMap){
        String gzhAppid = requestMap.get("gzh_appid");
        if(StrUtil.isBlank(gzhAppid)){
            return ResponseUtil.resultFail(ErrCode.MISSING_REQUEST_PARAMETERS);
        }
        // 获取accessToken
        String accessToken = redisUtil.get(RedisKeyConst.TOKEN + gzhAppid);
        if (ObjectUtil.isNull(accessToken)) {
            return ResponseUtil.resultFail(ErrCode.REQUEST_PARAMETER_ERROR);
        }
        // 获取自定义菜单数据
        ArrayList<Level1MenuModel> buttonList = customMenuService.getMenuInfo(gzhAppid);

        if(ObjectUtil.isNull(buttonList)){
            return ResponseUtil.resultFail(ErrCode.REQUEST_PARAMETER_ERROR);
        }
        // 拼接请求参数，调创建菜单接口
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("button", buttonList);
        return wxInterfaceCallService.createCustomMenu(jsonObject, gzhAppid, accessToken);

    }
}
