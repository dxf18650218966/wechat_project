package com.wechat.mall.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * slide_show 首页轮播图片
 * @Author dai
 * @Date 2020/12/07
 */
@Data
public class SlideShowBean implements Serializable {
    private static final long serialVersionUID = 7518224887978117543L;

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 跳转页面ID
     */
    private String jumpPage;

    /**
     * 跳转页面
     */
    private String jumpPageName;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 轮播图
     */
    private String img;

}