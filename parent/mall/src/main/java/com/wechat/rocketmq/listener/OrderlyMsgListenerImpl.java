package com.wechat.rocketmq.listener;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 顺序消息监听器
 * @Author dai
 * @Date 2020/7/15
 */
@Component
public class OrderlyMsgListenerImpl implements MessageListenerOrderly {
    private Logger logger = LoggerFactory.getLogger(OrderlyMsgListenerImpl.class);


    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        MessageExt messageExt = msgs.get(0);
        String topic = messageExt.getTopic();
        String tags = messageExt.getTags();
        String keys = messageExt.getKeys();
        String msgId = messageExt.getMsgId();
        String body = new String(messageExt.getBody());

        logger.info("顺序消息监听:: Topic={} &Tag={} &Key={} &MsgId={} &Body={}",topic, tags, keys, msgId , body);

        try {
            logger.info("成功消费");

        }catch (Exception e){
            logger.error("顺序消费失败，key={}&MsgId={}", keys, msgId);
            logger.error(ExceptionUtils.getStackTrace(e));
            // 由于顺序消费是要前者消费成功才能继续消费，所以没有RECONSUME_LATER的这个状态，
            // 只有SUSPEND_CURRENT_QUEUE_A_MOMENT来暂停队列的其余消费，直到原消息不断重试成功为止才能继续消费。
            // ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT
            //于顺序消息来说，如果消费失败后将其延迟消费，那么顺序性实际就被破坏掉了。
            //
            //所以顺序消息消费失败的话，消息消费不会再推进，直到失败的消息消费成功为止。
        }
        // 成功消费
        return ConsumeOrderlyStatus.SUCCESS;

    }
}