package com.wechat.wx.model;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户登录信息
 * 用于封装小程序登录请求信息
 * @Author dai
 * @Date 2020/11/21 
 */
@Data
public class WxLoginInfo implements Serializable {
    private static final long serialVersionUID = 6914943231897981594L;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 会话令牌， 如果有传入该值，说明session_key 已过期，前端需要重新登录，会产生新的session_token，那么之前的session_token 需要清理
     */
    private String session_token;

    /**
     * 用户信息对象
     */
    private WxUserInfo wxUserInfo;

    /**
     * 包括敏感数据在内的完整用户信息的加密数据 (前端获取手机号传递的加密数据)
     */
    private String encryptedData;

    /**
     * 加密算法的初始向量 （手机号）
     */
    private String iv;

    /**
     *  用户登录凭证（有效期五分钟）。开发者需要在开发者服务器后台调用 auth.code2Session，使用 code 换取 openid 和 session_key 等信息
     */
    private String code;

}
