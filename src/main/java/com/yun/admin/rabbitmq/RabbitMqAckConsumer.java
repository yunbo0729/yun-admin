package com.yun.admin.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMqAckConsumer {

    @RabbitListener(queues = "queue01")
    @RabbitHandler
    public void receiveMsg(String msg, Channel channel, Message message) throws IOException {
        System.out.println("received msg:" + msg);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //制造异常，验证队列重试
            int i = 10/0;
            System.out.println("处理消息：" + msg);
            // 处理完毕 手动ack
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 出现异常 拒绝
            channel.basicNack(deliveryTag, false, true);
            //channel.basicReject(deliveryTag, true);
        }
    }

}
