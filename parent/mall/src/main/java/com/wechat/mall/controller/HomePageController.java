package com.wechat.mall.controller;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wechat.mall.service.HomePageService;
import com.wechat.model.ErrCode;
import com.wechat.model.ResponseUtil;
import com.wechat.tool.RedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 小程序商城首页
 * @author dxf
 * @date 2020/12/7 20:17
 * @version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("home_page")
public class HomePageController {
    @Autowired
    private HomePageService homePageService;

    /**
     * 初始化商城首页
     */
    @PostMapping("init")
    public Object init(@Validated @RequestBody JSONObject json){
        String projectId = json.getString("project_id");
        if(! StrUtil.isAllNotBlank(projectId)){
            return ResponseUtil.returnFail( ErrCode.MISSING_REQUEST_PARAMETERS );
        }

        return homePageService.getInit(projectId);
    }
}
