package com.wechat.rocketmq.listener;

import com.wechat.constant.SystemConst;
import com.wechat.rocketmq.mapper.TransactionLogMapper;
import com.wechat.rocketmq.model.TransactionLogBean;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 事务消息监听器
 * @author dxf
 * @date 2020/7/12 10:30
 * @version 1.0
 */
@Component
public class TransactionListenerImpl implements TransactionListener {
    Logger logger = LoggerFactory.getLogger(TransactionListenerImpl.class);

    //事务日志
    @Autowired
    TransactionLogMapper transactionLogMapper;

// 注意点：不能使用事务，会出现异常：org.springframework.amqp.AmqpException: PublisherCallbackChannel is closed
// 因为：不能再开启一条线程

    /**
     * 执行本地事务 (事务消息发送成功后执行本地事务)
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        // 消息主题
        String topic = msg.getTopic();
        // 消息标签
        String tags = msg.getTags();
        // 消息ID
        String msgId = msg.getTransactionId();
        // 业务唯一标识
        String keys = msg.getKeys();
        // 消息体
        String body = new String(msg.getBody());
        logger.info("执行本地事务::Topic={} &Tag={} &Key={} &MsgId={} &Body={}",topic, tags, keys, msgId, body);

        String result = SystemConst.SUCCESS;
        try {

            // ... 执行业务处理（不能使用事务）
            logger.info("成功消费");
        }catch (Exception e){
            result = SystemConst.FAIL;
            logger.error("本地事务执行失败::key={} &MsgId={}",keys, msgId);
            logger.error(ExceptionUtils.getStackTrace(e));
        }

        // 写入事务日志
        TransactionLogBean transactionLogBean = new TransactionLogBean();
        transactionLogBean.setId(msgId); // 消息id
        transactionLogBean.setBusinessId(keys); // 业务标识
        transactionLogBean.setResult(result); // 业务处理结果
        transactionLogMapper.insert(transactionLogBean);

        if(SystemConst.SUCCESS.equals(result)){
            // 提交事务消息，消费者可以看到此消息
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        // 回滚事务消息，消费者不会看到此消息
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    /**
     * 回查本地事务状态（执行本地事务没有返回事务状态，进行事务回查）
     * @param msg
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        // 消息ID
        String msgId = msg.getTransactionId();
        // 业务唯一标识
        String keys = msg.getKeys();
        TransactionLogBean transactionLogBean = transactionLogMapper.selectById(msgId);
        // 是否执行过本地事务
        if(transactionLogBean != null){
            // 执行过本地事务，判断事务日志的业务处理结果
            if(SystemConst.SUCCESS.equals(transactionLogBean.getResult())){
                return LocalTransactionState.COMMIT_MESSAGE;
            }else{
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        }
        // 没执行过本地事务，需要重新执行下
        String result = SystemConst.SUCCESS;
        try {
            logger.info("事务回查成功消费");
            // ... 执行业务处理（不能使用事务）
        }catch (Exception e){
            result = SystemConst.FAIL;
            logger.error("回查事务执行失败,Key={} &MsgId={}",keys, msgId);
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        // 写入事务日志
        TransactionLogBean logBean = new TransactionLogBean();
        logBean.setId(msgId); // 消息id
        logBean.setBusinessId(keys); // 业务标识
        logBean.setResult(result); // 业务处理结果
        transactionLogMapper.insert(logBean);

        if(SystemConst.SUCCESS.equals(result)){
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
