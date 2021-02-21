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
 * 单向消息监听
 * @Author dai
 * @Date 2020/7/18
 */
@Component
public class OnewayMsgListenerImpl  implements MessageListenerConcurrently {
    private Logger logger = LoggerFactory.getLogger(OnewayMsgListenerImpl.class);

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        MessageExt msg = msgs.get(0);
        String topic = msg.getTopic();
        String tags = msg.getTags();
        String keys = msg.getKeys();
        String msgId = msg.getMsgId();
        String body = new String(msg.getBody());
        logger.info("单向消息监听:: Topic={} &Tag={} &Key={} &MsgId={} &Body={}",topic, tags, keys, msgId , body);

        try {
            // ========= 业务处理 ===========

            // 成功消费
            logger.info("成功消费");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            logger.error("单向消费失败，key={} &MsgId={}", keys, msgId);
            logger.error(ExceptionUtils.getStackTrace(e));

            //  如果重试了三次就返回成功  reconsumeTimes: 第几次重试
            if (msg.getReconsumeTimes() == 3) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            //消费失败，稍后尝试使用
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
