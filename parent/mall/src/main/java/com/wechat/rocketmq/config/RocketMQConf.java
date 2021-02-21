package com.wechat.rocketmq.config;

import com.wechat.rocketmq.listener.*;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 初始化消息 生产者、消费者
 * @author dxf
 * @date 2021/2/21 17:43
 * @version 1.0
 */
@Configuration
public class RocketMQConf {
    /**
     * name server 连接地址
     */
    @Value("${rocketmq.namesrv_addr}")
    public String namesrvAddr;

    /**
     * 消息最大长度 默认1024*4(4M)
     */
    @Value("${rocketmq.max_message_size}")
    public int maxMessageSize;

    /**
     * 发送消息超时时间,默认3000
     */
    @Value("${rocketmq.send_msg_timeout}")
    public int sendMsgTimeout;

    /**
     * 在同步模式下,发送失败最大重试次数
     */
    @Value("${rocketmq.retry_times_when_send_failed}")
    public int retryTimesWhenSendFailed;

    /**
     * 在异步模式下,发送失败最大重试次数
     */
    @Value("${rocketmq.retry_times_when_send_async_failed}")
    public int retryTimesWhenSendAsyncFailed;


    // 事务消息监听器实现类 (执行本地事务和事务回查)
    @Autowired
    private TransactionListenerImpl transactionListenerImpl;

    // 事务消息监听器实现类 (消费消息)
    @Autowired
    private TransactionMsgListenerImpl transactionMsgListenerImpl;

    // 异步消息监听器实现类
    @Autowired
    private AsyncMsgListenerImpl asyncMsgListenerImpl;

    // 同步消息监听器实现类
    @Autowired
    private SyncMsgListenerImpl syncMsgListenerImpl;

    // 单向消息监听器实现类
    @Autowired
    private OnewayMsgListenerImpl onewayMsgListenerImpl;

    // 顺序消息监听器实现类
    @Autowired
    private OrderlyMsgListenerImpl orderlyMsgListener;


    /**
     * 启动：普通消息生产者 （同步、异步、单向、顺序）
     *
     * @return
     * @throws MQClientException
     */
    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(RocketMQName.DEFAULT_PRODUCER_GROUP);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setMaxMessageSize(maxMessageSize);
        producer.setSendMsgTimeout(sendMsgTimeout);
        // 设置发送失败重试次数(默认2次）
        producer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        producer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenSendAsyncFailed);
        // 对象在使用之前必须要调用一次，只能初始化一次
        producer.start();
        return producer;
    }


    /**
     * 启动：事务消息生产者
     *
     * @return
     * @throws MQClientException
     */
    @Bean
    public TransactionMQProducer transactionProducer() throws MQClientException {
        // 普通消息生产者和事务生产者的集群需要不一样，不然start()启动报错的
        TransactionMQProducer producer = new TransactionMQProducer(RocketMQName.TRANSACTIO_PRODUCER_GROUP);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setMaxMessageSize(maxMessageSize);
        producer.setSendMsgTimeout(sendMsgTimeout);

        // 线程池：
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20, Integer.MAX_VALUE, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), (Runnable r) -> new Thread("Order Transaction Massage Thread"));
        //  自定义线程池, 执行事务操作
        producer.setExecutorService(executor);

        // 用于执行本地事务和事务状态回查的监听器
        producer.setTransactionListener(transactionListenerImpl);

        // 对象在使用之前必须要调用一次，只能初始化一次
        producer.start();
        return producer;
    }


    // ======  按消息类型划分消费者  =======
    /**
     * 启动：同步消费者
     * @return
     * @throws MQClientException
     */
    @Bean
    public DefaultMQPushConsumer syncMQConsumer() throws MQClientException {
        // 实例化消息生产者,指定组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQName.SYNC_CONSUMERS_GROUP );
        // 指定Namesrv地址信息.
        consumer.setNamesrvAddr(namesrvAddr);
        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe(RocketMQName.SYNC_CONSUMERS_TOPIC, RocketMQName.SYNC_CONSUMERS_TAGS );
        // 消费模式：  CLUSTERING集群模式  BROADCASTING广播模式
        consumer.setMessageModel( MessageModel.CLUSTERING );
        // 注册消息监听器，用来消费消息
        consumer.registerMessageListener(syncMsgListenerImpl);
        // 单次消费时一次性消费多少条消息，批量消费接口才有用, 默认值为1
        consumer.setConsumeMessageBatchMaxSize(1);
        // 第一次启动从消息队列头取数据, 后面按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 启动消息者
        consumer.start();
        return consumer;
    }

    /**
     * 启动：异步消费者
     * @return
     * @throws MQClientException
     */
    @Bean
    public DefaultMQPushConsumer asyncMQConsumer() throws MQClientException {
        // 实例化消息生产者,指定组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQName.ASYNC_CONSUMERS_GROUP );
        // 指定Namesrv地址信息.
        consumer.setNamesrvAddr(namesrvAddr);
        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe(RocketMQName.ASYNC_CONSUMERS_TOPIC , RocketMQName.ASYNC_CONSUMERS_TAGS );
        // 消费模式：  CLUSTERING集群模式  BROADCASTING广播模式
        consumer.setMessageModel( MessageModel.CLUSTERING );
        // 注册消息监听器，用来消费消息
        consumer.registerMessageListener(asyncMsgListenerImpl);
        // 单次消费时一次性消费多少条消息，批量消费接口才有用, 默认值为1
        consumer.setConsumeMessageBatchMaxSize(1);
        // 第一次启动从消息队列头取数据, 后面按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 启动消息者
        consumer.start();
        return consumer;
    }

    /**
     * 启动：单向消费者
     * @return
     * @throws MQClientException
     */
    @Bean
    public DefaultMQPushConsumer onewayMQConsumer() throws MQClientException {
        // 实例化消息生产者,指定组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQName.ONEWAY_CONSUMERS_GROUP );
        // 指定Namesrv地址信息.
        consumer.setNamesrvAddr(namesrvAddr);
        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe( RocketMQName.ONEWAY_CONSUMERS_TOPIC, RocketMQName.ONEWAY_CONSUMERS_TOPIC );
        // 消费模式：  CLUSTERING集群模式  BROADCASTING广播模式
        consumer.setMessageModel( MessageModel.CLUSTERING );
        // 注册消息监听器，用来消费消息
        consumer.registerMessageListener(onewayMsgListenerImpl);
        // 单次消费时一次性消费多少条消息，批量消费接口才有用, 默认值为1
        consumer.setConsumeMessageBatchMaxSize(1);
        // 第一次启动从消息队列头取数据, 后面按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 启动消息者
        consumer.start();
        return consumer;
    }

    /**
     * 启动：事务消费者
     * @return
     * @throws MQClientException
     */
    @Bean
    public DefaultMQPushConsumer transactionMQConsumer() throws MQClientException {
        // 实例化消息生产者,指定组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQName.TRANSACTION_CONSUMERS_GROUP );
        // 指定Namesrv地址信息.
        consumer.setNamesrvAddr(namesrvAddr);
        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe( RocketMQName.TRANSACTION_CONSUMERS_TOPIC, RocketMQName.TRANSACTION_CONSUMERS_TAGS);
        // 消费模式：  CLUSTERING集群模式  BROADCASTING广播模式
        consumer.setMessageModel( MessageModel.CLUSTERING );
        // 注册消息监听器，用来消费消息
        consumer.registerMessageListener(transactionMsgListenerImpl);
        // 单次消费时一次性消费多少条消息，批量消费接口才有用, 默认值为1
        consumer.setConsumeMessageBatchMaxSize(1);
        // 第一次启动从消息队列头取数据, 后面按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 启动消息者
        consumer.start();
        return consumer;
    }

    /**
     * 启动：顺序消费者
     * @return
     * @throws MQClientException
     */
    @Bean
    public DefaultMQPushConsumer orderlyMQConsumer() throws MQClientException {
        // 实例化消息生产者,指定组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQName.ORDERLY_CONSUMERS_GROUP);
        // 指定Namesrv地址信息.
        consumer.setNamesrvAddr(namesrvAddr);
        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe(RocketMQName.ORDERLY_CONSUMERS_TOPIC, RocketMQName.ORDERLY_CONSUMERS_TAGS );
        // 消费模式：  CLUSTERING集群模式  BROADCASTING广播模式
        consumer.setMessageModel( MessageModel.CLUSTERING );
        // 注册消息监听器，用来消费消息
        consumer.registerMessageListener(orderlyMsgListener);
        // 单次消费时一次性消费多少条消息，批量消费接口才有用, 默认值为1
        consumer.setConsumeMessageBatchMaxSize(1);
        // 第一次启动从消息队列头取数据, 后面按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 启动消息者
        consumer.start();
        return consumer;
    }
}
