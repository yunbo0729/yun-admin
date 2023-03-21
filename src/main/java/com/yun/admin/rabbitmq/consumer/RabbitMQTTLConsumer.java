package com.yun.admin.rabbitmq.consumer;


import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.JDKSaslConfig;
import com.yun.admin.conf.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * 队列ttl的消费者
 */
@Component
@Slf4j
public class RabbitMQTTLConsumer {

    @RabbitListener(queues = RabbitMQConfig.DELAYED_QUEUE_NAME)
    @RabbitHandler
    public void receiveD(JSONObject msg, Channel channel, Message message) throws IOException {
        log.info("当前时间:{},收到死信队列的消息:{}", new Date(), msg);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 处理完毕 手动ack
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 出现异常 拒绝
            channel.basicNack(deliveryTag, false, true);
            //channel.basicReject(deliveryTag, true);
        }
    }
}