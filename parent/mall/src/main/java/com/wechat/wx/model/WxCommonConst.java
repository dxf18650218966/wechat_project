package com.wechat.wx.model;

/**
 * 通用常量
 * @author dxf
 * @version 1.0
 * @date 2020/11/7 21:33
 */
public interface WxCommonConst {
    String ERRCODE = "errcode";
    String ERRMSG = "errmsg";

    /**
     * 文本消息
     */
    String MESSAGE_TEXT = "text";
    /**
     * 图片消息
     */
    String MESSAGE_IMAGE = "image";
    /**
     * 图文消息
     */
    String MESSAGE_NEWS = "news";
    /**
     * 事件消息
     */
    String MESSAGE_EVENT = "event";
    /**
     * 关注事件
     */
    String EVENT_SUBSCRIBE = "subscribe";
    /**
     * 取消关注事件
     */
    String EVENT_UNSUBSCRIBE = "unsubscribe";


}
