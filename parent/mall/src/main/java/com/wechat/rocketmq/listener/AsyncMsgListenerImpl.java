package com.wechat.rocketmq.listener;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 异步消息监听
 * @author dxf
 * @date 2020/7/11 22:58
 * @version 1.0
 */
@Component
public class AsyncMsgListenerImpl implements MessageListenerConcurrently {
    private Logger logger = LoggerFactory.getLogger(AsyncMsgListenerImpl.class);

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        MessageExt msg = msgs.get(0);
        String topic = msg.getTopic();
        String tags = msg.getTags();
        String keys = msg.getKeys();
        String msgId = msg.getMsgId();
        String body = new String(msg.getBody());
        logger.info("异步消息监听:: Topic={} &Tag={} &Key={} &MsgId={} &Body={}",topic, tags, keys, msgId , body);

        try {
            int i = 0/1;
            // ========= 业务处理 ===========
            // 成功消费
            logger.info("成功消费");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS ;
        }catch (Exception e){
            logger.error("异步消费失败，Key={} &MsgId={}", keys, msgId);
            logger.error(ExceptionUtils.getStackTrace(e));

            //  如果重试了三次就返回成功   reconsumeTimes: 第几次重试
            if (msg.getReconsumeTimes() == 3) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS ;
            }
            // 消费失败，稍后重新消费
            return ConsumeConcurrentlyStatus.RECONSUME_LATER ;
        }
    }
}
