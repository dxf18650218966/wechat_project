package com.wechat.rocketmq.send;

import com.wechat.rocketmq.model.MsgBodyBean;
import com.wechat.rocketmq.model.OrderMsgBean;
import com.wechat.tool.ConvertUtil;
import com.wechat.tool.ObjectUtil;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import javax.annotation.Resource;
import javax.naming.Name;
import java.util.ArrayList;

/**
 * 发送消息
 * @author dxf
 * @date 2020/7/11 22:48
 * @version 1.0
 */

@Component
public class MessageSend {
    private String SENDSTATUS= "SEND_OK";
    private Logger logger = LoggerFactory.getLogger(MessageSend.class);

    /**
     * 普通消息生产者
     */
    @Resource
    private DefaultMQProducer defaultMQProducer;

    /**
     * 事务消息生产者
     */
    @Resource
    private TransactionMQProducer transactionProducer;


    /**
     * 同步发送消息
     * @param topic 主题
     * @param tags 标签
     * @param key 业务标识
     * @param message 消息载体
     * @return true发送成功
     */
    public Boolean sendSyncMsg(String topic, String tags, String key, String message) {
        try {
            Message msg = new Message(topic, tags, key, message.getBytes());
            SendResult sendResult = defaultMQProducer.send(msg);
            // 消息发送成功
            if(SENDSTATUS.equals(sendResult.getSendStatus().name())){
                logger.info("同步消息发送成功:::Key={}&MsgId={}",key, sendResult.getMsgId());
                return true;
            };
            return false;
        }catch (Exception e){
            logger.error("同步消息发送失败:::Key={}&Message={}",key, message);
            logger.error("失败原因:::"+ ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 异步发送消息
     * @param topic 主题
     * @param tags 标签
     * @param key 业务标识
     * @param message 消息载体
     */
    public void sendAsyncMsg(String topic, String tags, String key, String message) {
        Message msg = new Message(topic, tags, key, message.getBytes());
        try {
            defaultMQProducer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    // 消息发送成功
                    logger.info("异步消息发送成功:::Key={}&MsgId={}",key, sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
                    logger.error("异步消息发送失败:::Key={}&Message={}",key, message);
                    logger.error("失败原因:::"+ ExceptionUtils.getStackTrace(e));
                }
            });
        }catch (Exception e){
            logger.error("异步消息发送失败:::Key={}&Message={}",key, message);
            logger.error("失败原因:::"+ ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 单向模式发送消息
     * @param topic 主题
     * @param tags 标签
     * @param key 业务标识
     * @param message 消息载体
     */
    public void sendOnewayMsg(String topic, String tags, String key, String message) {
        try {
            Message msg = new Message(topic, tags, key, message.getBytes());
            defaultMQProducer.sendOneway(msg);
            logger.info("单向消息发送成功:::Key={}",key);
        }catch (Exception e){
            logger.error("单向消息发送失败:::Key={}&Message={}",key, message);
            logger.error("失败原因:::"+ ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 批量发送
     * @param msgBodyBeans 批量发送的消息
     * @return true发送成功
     * ps: 一批消息只能有相同的topic, 相同的waitStroeMsgOK, 不能是延时消息
     */
    public Boolean sendSyncMsg(List<MsgBodyBean> msgBodyBeans) {
        if(msgBodyBeans.size() == 0){
            return false;
        }
        List<Message> msgList = new ArrayList<>();
        try {
            for (MsgBodyBean msgBodyBean : msgBodyBeans) {
                Message msg = new Message(msgBodyBean.getTopic(), msgBodyBean.getTags(), msgBodyBean.getKey(), msgBodyBean.getMessage().getBytes());
                msgList.add(msg);
            }
            // 批量发送消息
            SendResult sendResult = defaultMQProducer.send(msgList);
            // 消息发送成功
            if(SENDSTATUS.equals(sendResult.getSendStatus().name())){
                logger.info("批量消息发送成功:::MsgId={}",sendResult.getMsgId());
                return true;
            };
            return false;
        }catch (Exception e){
            logger.error("批量消息发送失败:::"+ ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 以同步模式发送延迟消息
     * @param topic 主题
     * @param tags 标签
     * @param key 业务标识
     * @param message 消息载体
     * @param delayTimeLevel 延时级别
     *  messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h 【对应级别从1开始数】
     */
    public Boolean sendDelayMsg(String topic, String tags, String key, String message, int delayTimeLevel) {
        try {
            Message msg = new Message(topic, tags, key, message.getBytes());
            msg.setDelayTimeLevel(delayTimeLevel);
            SendResult sendResult = defaultMQProducer.send(msg);
            // 消息发送成功
            if(SENDSTATUS.equals(sendResult.getSendStatus().name())){
                logger.info("延迟消息发送成功:::Key={} &MsgId={}",key, sendResult.getMsgId());
                return true;
            };
            return false;
        }catch (Exception e){
            logger.error("延迟消息发送失败:::Key={} &Message={}",key, message);
            logger.error("失败原因:::"+ ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 发送事务消息
     * @param topic 主题
     * @param tags 标签
     * @param key 业务标识
     * @param message 消息载体
     * @return true发送成功
     */
    public Boolean sendTransactioMsg(String topic, String tags, String key, String message) {
        try {
            Message msg = new Message(topic, tags, key, message.getBytes());
            TransactionSendResult sendResult =  transactionProducer.sendMessageInTransaction(msg, null);
            // 消息发送成功
            if(SENDSTATUS.equals(sendResult.getSendStatus().name())){
                logger.info("事务消息发送成功:::Key={} &MsgId={} &本地事务执行状态={}",key, sendResult.getMsgId(),sendResult.getLocalTransactionState());
                return true;
            };
            return false;
        }catch (Exception e){
            logger.error("事务消息发送失败:::Key={} &Message={}",key, message);
            logger.error("失败原因:::"+ ExceptionUtils.getStackTrace(e));
            return false;
        }
    }


    /**
     * 发送顺序消息
     * @param topic 消息主题
     * @param key 业务标识（同一组顺序消息，业务标识需要一致）      ps: 选取指定队列方式，需要key%队列数量，所以业务标识需要是数字
     * @param orderMsgBeanList 顺序消息列表
     * @return 是否整组顺序消息都发送成功了
     */
    public Boolean sendOrderMsg(String topic, String key, List<OrderMsgBean> orderMsgBeanList) {
        Boolean result = true;
        if (ObjectUtil.isNotNull(orderMsgBeanList)){
            for (OrderMsgBean orderMsgBean : orderMsgBeanList) {
                Message msg = new Message(topic, orderMsgBean.getTags(), key, orderMsgBean.getMessage().getBytes());

                try {
                    SendResult sendResult = defaultMQProducer.send(msg, new MessageQueueSelector(){
                        // 选择队列: 同一组顺序消息需要发送到同一个Queue
                        @Override
                        public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                            // arg为业务标识，根据选取方式：业务标识 % 队列数量 = 消息队列[i]
                            Integer id = ConvertUtil.toInteger(arg);
                            int index = id % mqs.size();
                            return mqs.get(index);
                        }
                    }, key); // 根据业务标识分组

                    // 消息发送成功
                    if(SENDSTATUS.equals(sendResult.getSendStatus().name())){
                        logger.info("顺序消息发送成功:::Key={} &MsgId={}",key, sendResult.getMsgId());
                    }else{
                        logger.info("顺序消息发送失败:::Key={} &MsgId={} SendStatus={}",key, sendResult.getMsgId(),sendResult.getSendStatus());
                        result = false;
                    }
                }catch (Exception e){
                    result = false;
                    logger.error("顺序消息发送失败:::Key={} &Message={}",key, orderMsgBean.getMessage());
                    logger.error("失败原因:::"+ ExceptionUtils.getStackTrace(e));
                }
            }
        }
        return result;
    }
}

