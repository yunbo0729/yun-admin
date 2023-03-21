package com.yun.admin.rabbitmq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RabbitMQTopicConsumer {

    /*@RabbitListener(queues = "direcle")
    public void receive(String msg){
        log.info("direcle接收到的消息---"+msg);
    }

    @RabbitListener( queues=("topic.msg1"))
    public void topicReceive1(JSONObject msg){
        log.info("topic1接收到的消息---"+msg);
    }

    @RabbitListener( queues=("topic.msg2"))
    public void topicReceive2(JSONObject msg){
        log.info("topic2接收到的消息---"+msg);
    }*/

    @RabbitListener(queues = "topic.msg1")
    @RabbitHandler
    public void receiveMsg(JSONObject msg, Channel channel, Message message) throws IOException {
        System.out.println("topic.msg1 :" + msg);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //制造异常，验证队列重试
            //int i = 10/0;
            System.out.println("topic.msg1-处理消息：" + msg);
            // 处理完毕 手动ack
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 出现异常 拒绝
            channel.basicNack(deliveryTag, false, true);
            //channel.basicReject(deliveryTag, true);
        }
    }
}
