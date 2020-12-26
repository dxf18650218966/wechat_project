package com.wechat.email.model;

import lombok.Data;

import java.util.ArrayList;

/** 搜狐邮箱(发送内容分装)
 * @Author dai
 * @Date 2020/10/31
 */
@Data
public class SoHuBean {
    /**
     * html格式
     */
    public static final String FORMAT_HTML = "text/html;charset=utf-8";
    /**
     * 文本格式
     */
    public static final String FORMAT_TEXT ="UTF-8";

    /**
     *  邮件接收者的地址 (必)
     */
    private ArrayList<String> toAddress;
    /**
     * 邮件主题 (必)
     */
    private String subject;
    /**
     *  邮件内容 (必)
     */
    private String content;
    /**
     * FORMAT_HTML = "text/html;charset=utf-8"
     * FORMAT_TEXT ="UTF-8"
     * 邮件格式 (必)
     */
    private String format;
    /**
     * 邮件附件路径
     */
    private ArrayList<String> attachFilePath;
}
