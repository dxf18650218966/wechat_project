package com.wechat.wx.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *  公众号信息
 * @Author dai
 * @Date 2020/11/1
 */
@Data
@TableName("gzh_menu")
public class GzhMenuBean implements Serializable {

  private String id;
  private String foreignKey;

  /**
   * 所属公众号
   */
  private String gzh;
  private String gzhName;

  /**
   * 上级菜单
   */
  private String parentMenu;
  private String parentMenuName;

  /**
   * 菜单名称
   */
  private String menuName;

  /**
   * 排序
   */
  private String sort;

  /**
   * 跳转类型   0小程序 1公众号
   */
  private String jumpType;
  private String jumpTypeName;

  /**
   * 页面名称
   */
  private String pageName;

  /**
   * 页面路径
   */
  private String pageUrl;

  /**
   * 所属小程序
   */
  private String xcxAppid;
  private String xcxAppidName;
}
