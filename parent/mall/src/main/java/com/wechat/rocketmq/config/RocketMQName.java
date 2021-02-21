package com.wechat.rocketmq.config;

/**
 * 定义集群名称、主题、标签等
 * @author dxf
 * @version 1.0
 * @date 2021/2/21 17:56
 */
public interface RocketMQName {
    // ===================  生产者集群  =====================
    /** 普通消息和顺序消息 */
    String DEFAULT_PRODUCER_GROUP = "defaultProducerGroup";
    /** 事务消息 */
    String TRANSACTIO_PRODUCER_GROUP = "transactioProducerGroup";

    // ===================  消费者集群  =====================
    String SYNC_CONSUMERS_GROUP = "syncConsumersGroup";
    String SYNC_CONSUMERS_TOPIC = "syncTopic";
    String SYNC_CONSUMERS_TAGS = "tagA||tagB";

    String ASYNC_CONSUMERS_GROUP = "asyncConsumersGroup";
    String ASYNC_CONSUMERS_TOPIC = "asyncTopic";
    String ASYNC_CONSUMERS_TAGS = "tagA||tagB";

    String ONEWAY_CONSUMERS_GROUP = "onewayConsumersGroup";
    String ONEWAY_CONSUMERS_TOPIC = "onewayTopic";
    String ONEWAY_CONSUMERS_TAGS = "tagA||tagB";

    String ORDERLY_CONSUMERS_GROUP = "orderlyConsumersGroup";
    String ORDERLY_CONSUMERS_TOPIC = "orderlyTopic";
    String ORDERLY_CONSUMERS_TAGS = "tagA||tagB";

    String TRANSACTION_CONSUMERS_GROUP = "transactionConsumersGroup";
    String TRANSACTION_CONSUMERS_TOPIC = "transactionTopic";
    String TRANSACTION_CONSUMERS_TAGS = "tagA||tagB";
}
