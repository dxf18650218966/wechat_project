package com.wechat.wx.model;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户信息
 * 用于封装用户授权之后，微信回调中返回的用户信息
 * @Author dai
 * @Date 2020/11/21 
 */
@Data
public class WxUserInfo implements Serializable {
    private static final long serialVersionUID = 2105201789833687954L;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像图片的 URL
     */
    private String avatarUrl;

    /**
     * 用户性别
     */
    private String gender;

}
