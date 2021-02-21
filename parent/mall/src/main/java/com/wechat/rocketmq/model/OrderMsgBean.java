package com.wechat.rocketmq.model;

import lombok.Data;

/**
 * 顺序消息
 * @Author dai
 * @Date 2020/7/12
 */
@Data
public class OrderMsgBean {
    // 消息标签
    private String tags;

    // 方法体
    private String message;
}
