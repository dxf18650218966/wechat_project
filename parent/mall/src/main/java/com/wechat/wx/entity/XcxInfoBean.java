package com.wechat.wx.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 *  小程序信息
 * @Author dai
 * @Date 2020/11/1
 */
@Data
@TableName("xcx_info")
public class XcxInfoBean implements Serializable {

  private static final long serialVersionUID = 2008815960924293493L;
  private String id;
  private String foreignKey;

  /**
   *  项目
   */
  private String project;
  private String projectName;

  /**
   * 名称
   */
  private String name;

  /**
   * 开发者ID
   */
  private String appId;


  /**
   * 开发者密码
   */
  private String appSecret;

  /**
   * 商户号
   */
  private String mchId;

  /**
   * 商户名
   */
  private String mchName;

  /**
   * 秘钥
   */
  private String apiKey;

  /**
   * 证书路径
   */
  private String apiclientUrl;
}
