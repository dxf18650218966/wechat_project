package com.wechat.rocketmq.model;


import lombok.Data;

/**
 * 批量消息
 * @Author dai
 * @Date 2020/7/12
 */
@Data
public class MsgBodyBean {
    /**
     *  消息主题
     */
    private String topic;

    /**
     * 消息标签
     */
    private String tags;

    /**
     * 业务标识
     */
    private String key;

    /**
     *  方法体
     */
    private String message;
}
