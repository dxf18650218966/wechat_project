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
@TableName("gzh_info")
public class GzhInfoBean implements Serializable {

  private static final long serialVersionUID = -3932430706022550253L;
  private String id;
  private String foreignKey;

  /**
   *  项目
   */
  private String project;
  private String projectName;

  /**
   * 公众号名称
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
   * 令牌(Token)
   */
  private String token;

  /**
   * 消息加解密密钥
   */
  private String encodingAesKey;

  /**
   * 域名
   */
  private String url;

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

  /**
   * 原始公众号ID
   */
  private String originalId;
}
