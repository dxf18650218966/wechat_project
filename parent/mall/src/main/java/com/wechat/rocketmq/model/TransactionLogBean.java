package com.wechat.rocketmq.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *  rocketMQ事务日志
 * @author
 */
@Data
@TableName("transaction_log")
public class TransactionLogBean {
    /**
     * 事务id
     */
    private String id;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 事务结果
     */
    private String result;

}