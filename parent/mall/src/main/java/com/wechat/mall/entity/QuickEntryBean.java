package com.wechat.mall.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * quick_entry 首页快捷入口配置
 * @Author dai
 * @Date 2020/12/07
 */
@Data
public class QuickEntryBean implements Serializable {
    private static final long serialVersionUID = -7285870772799157340L;

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 图标
     */
    private String logo;

    /**
     * 跳转页面ID
     */
    private String jumpPage;

    /**
     * 跳转页面
     */
    private String jumpPageName;

}