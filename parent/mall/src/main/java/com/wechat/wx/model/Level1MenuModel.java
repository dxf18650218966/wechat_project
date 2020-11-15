package com.wechat.wx.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 一级菜单
 * @Author dai
 * @Date 2020/11/14 
 */
@Data
public class Level1MenuModel implements Serializable {
    /**
     * 菜单标题，不超过16个字节，子菜单不超过60个字节
     */
    private String name;

    /**
     * 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型
     */
    private String type;

    /**
     * 网页链接
     */
    private String url;

    /**
     * miniprogram类型必须   小程序的appid
     */
    private String appid;

    /**
     * miniprogram类型必须 	小程序的页面路径
     */
    private String pagepath;

    /**
     * 二级菜单数组
     */
    private ArrayList<Level2MenuModel> sub_button;


}
