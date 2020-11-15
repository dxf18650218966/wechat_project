package com.wechat.wx.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechat.model.ErrCode;
import com.wechat.model.HttpClientResult;
import com.wechat.model.ResponseUtil;
import com.wechat.constant.SystemConst;
import com.wechat.crypto.UrlEncodeUtil;
import com.wechat.tool.HttpUtil;
import com.wechat.tool.ObjectUtil;
import com.wechat.wx.entity.GzhMenuBean;
import com.wechat.wx.model.Level1MenuModel;
import com.wechat.wx.model.Level2MenuModel;
import com.wechat.wx.mapper.GzhMenuMapper;
import com.wechat.wx.model.WechatUrlConst;
import com.wechat.wx.model.WxCommonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义菜单
 *
 * @author dxf
 * @version 1.0
 * @date 2020/11/14 14:55
 */
@Service
public class CustomMenuService {
    private Logger logger = LoggerFactory.getLogger(CustomMenuService.class);
    @Autowired
    private GzhMenuMapper gzhMenuMapper;

    /**
     * 公众号相对路径拼接成绝对路径
     * @param gzhAppid
     * @return
     */
    @Value("${sys.absolute-path}")
    private String absolutePath;

    /**
     * 获取自定义菜单数据
     *
     * @param gzhAppid
     * @return
     */
    public ArrayList<Level1MenuModel> getMenuInfo(String gzhAppid) {
        List<GzhMenuBean> gzhMenuList = gzhMenuMapper.selectList(new QueryWrapper<GzhMenuBean>().eq("gzh", gzhAppid));
        if (ObjectUtil.isNull(gzhMenuList)) {
            return null;
        }
        // 一级菜单容器
        ArrayList<Level1MenuModel> level1MenuList = new ArrayList<>(16);

        // 过滤:  查询上级菜单为空
        List<GzhMenuBean> collect = gzhMenuList.stream()
                .filter(menu -> StrUtil.isBlank(menu.getParentMenu()))
                .sorted(Comparator.comparing(GzhMenuBean ::getSort)).collect(Collectors.toList());
        collect.forEach(menu -> {
            // 创建一级菜单
            Level1MenuModel level1MenuModel = new Level1MenuModel();
            level1MenuModel.setName(menu.getMenuName());
            // ::: 有跳转类型，说明他没有二级菜单
            if(StrUtil.isNotBlank(menu.getJumpType())){
                level1MenuModel.setType(SystemConst.ZERO .equals(menu.getJumpType()) ? "miniprogram" : "view");
                if (SystemConst.ZERO.equals(menu.getJumpType())) {
                    level1MenuModel.setUrl( WechatUrlConst.PLATFORM_DOMAIN_NAME );
                } else {
                    // http开头是自定义链接， 否则页面相对路径
                    if (! menu.getPageUrl().startsWith(SystemConst.HTTP )) {
                        // 相对路径拼接成绝对路径
                        menu.setPageUrl(absolutePath + menu.getPageUrl());
                    }
                    // UrlEncode编码
                    String urlEncode = UrlEncodeUtil.encodeByUtf(menu.getPageUrl());
                    // 拼接公众号网页授权
                    String format = String.format(WechatUrlConst.WECHAT_AUTHORIZATION , gzhAppid, urlEncode);
                    level1MenuModel.setUrl(format);
                }
                level1MenuModel.setAppid(menu.getXcxAppid());
                level1MenuModel.setPagepath(menu.getPageUrl());
            } else {
                // 通过一级菜单查找出属于自己的二级菜单
                List<GzhMenuBean> subCollect = gzhMenuList.stream()
                        .filter(subMenu -> subMenu.getParentMenu().equals(menu.getId()))
                        .sorted(Comparator.comparing(GzhMenuBean ::getSort)).collect(Collectors.toList());
                if (subCollect.size() > 0) {
                    // 二级菜单容器
                    ArrayList<Level2MenuModel> level2MenuList = new ArrayList<>(16);
                    subCollect.forEach(subMenu -> {
                        // 创建二级菜单
                        Level2MenuModel level2MenuModel = new Level2MenuModel();
                        level2MenuModel.setType(SystemConst.ZERO .equals(subMenu.getJumpType()) ? "miniprogram" : "view");
                        level2MenuModel.setName(subMenu.getMenuName());
                        // 跳转类型  0小程序、1公众号
                        if (SystemConst.ZERO.equals(subMenu.getJumpType())) {
                            level2MenuModel.setUrl(WechatUrlConst.PLATFORM_DOMAIN_NAME );
                        } else {
                            // http开头是自定义链接， 否则页面相对路径
                            if (! subMenu.getPageUrl().startsWith(SystemConst.HTTP)) {
                                // 相对路径拼接成绝对路径
                                subMenu.setPageUrl(absolutePath + subMenu.getPageUrl());
                            }
                            // UrlEncode编码
                            String urlEncode = UrlEncodeUtil.encodeByUtf(subMenu.getPageUrl());
                            // 拼接公众号网页授权
                            String format = String.format(WechatUrlConst.WECHAT_AUTHORIZATION , gzhAppid, urlEncode);
                            level2MenuModel.setUrl(format);
                        }
                        level2MenuModel.setAppid(subMenu.getXcxAppid());
                        level2MenuModel.setPagepath(subMenu.getPageUrl());
                        level2MenuList.add(level2MenuModel);
                    });
                    level1MenuModel.setSub_button(level2MenuList);
                }
            }
            level1MenuList.add(level1MenuModel);
        });
        return level1MenuList;
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
        HttpClientResult httpClientResult = HttpUtil.post(url, jsonObject.toJSONString());

        // 处理响应结果
        if (SystemConst.SUCCES_CODE .equals(httpClientResult.getCode()) && StrUtil.isNotBlank(httpClientResult.getData())){
            JSONObject  response = JSON.parseObject(httpClientResult.getData());
            String errcode = response.getString(WxCommonConst.ERRCODE);
            String errmsg = response.getString(WxCommonConst.ERRMSG);
            if(! SystemConst.ZERO .equals(errcode)){
                logger.error("创建菜单失败:::appid={},errMsg={}" ,gzhAppid, errmsg);
                return ResponseUtil.resultFail(errcode,errmsg);
            }else {
                return ResponseUtil.resultSuccess(errmsg);
            }
        }else{
            return ResponseUtil.resultFail(ErrCode.SYSTEM_INTERNAL_ERROR);
        }
    }
}

