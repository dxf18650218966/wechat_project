package com.wechat.email.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 搜狐邮箱身份认证
 * 如果需要身份认证，则创建一个密码验证器
 * @Author dai
 * @Date 2020/1/12
 */
public class SoHulAuthenticator extends Authenticator {
    private String userName;
    private String password;

    @Override
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(userName, password);
    }
}   