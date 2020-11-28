package com.wechat.wx.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  用户信息
 * @Author dai
 * @Date 2020/11/1
 */
@Data
public class UserAccountBean implements Serializable {
  private static final long serialVersionUID = 3236682729441123746L;
  private String id;
  private String foreignKey;

  /**
   * 会员卡号
   */
  private String cardId;

  /**
   * 手机号
   */
  private String phone;

  /**
   * 积分
   */
  private long integral;

  /**
   * 余额
   */
  private BigDecimal balance;

  /**
   * 密码
   */
  private String password;

  /**
   * 是否免密  0否 1是
   */
  private String withoutCode;
  private String withoutCodeName;

}
