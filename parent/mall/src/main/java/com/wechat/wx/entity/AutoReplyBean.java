package com.wechat.wx.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 自动回复
 * @author 
 */
@Data
@TableName("auto_reply")
public class AutoReplyBean implements Serializable {
    private String id;

    private String foreignKey;

    /**
     * 公众号
     */
    private String gzh;
    private String gzhName;

    /**
     * 是否生效
     * value：0否 1是
     */
    private String hasValid;
    private String hasValidName;

    /**
     * 回复类型
     * value：0关注回复 1关键字回复
     */
    private String replyType;
    private String replyTypeName;

    /**
     * 消息类型   text文本消息、image图片消息、news图文消息
     */
    private String messageType;
    private String messageTypeName;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 图片
     */
    private String picUrl;

    /**
     * 图文消息标题
     */
    private String title;

    /**
     * 图文消息描述
     */
    private String describe;

    /**
     * 图文消息跳转链接
     */
    private String url;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 关键字
     */
    private String keyword;
}