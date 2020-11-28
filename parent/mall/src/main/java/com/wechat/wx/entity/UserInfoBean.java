package com.wechat.wx.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  用户信息
 * @Author dai
 * @Date 2020/11/1
 */
@Data
public class UserInfoBean implements Serializable {
  private static final long serialVersionUID = -7877767478650942199L;
  private String id;
  private String foreignKey;

  /**
   *  项目
   */
  private String project;
  private String projectName;

  /**
   *  注册时间
   */
  private LocalDateTime registrationTime;

  /**
   *  公众号openId
   */
  private String gzhOpenId;

  /**
   *  小程序openId
   */
  private String xcxOpenId;

  /**
   *  卡号
   */
  private String cardId;

  /**
   *  手机号
   */
  private String phone;

  /**
   *  会员名
   */
  private String name;

  /**
   *  昵称
   */
  private String nickname;

  /**
   *  性别  0女1男
   */
  private String gender;
  private String genderName;

  /**
   *  生日
   */
  private LocalDateTime birthday;

  /**
   *  会员等级
   */
  private String level;
  private String levelName;

  /**
   *  头像
   */
  private String headPortrait;
}
