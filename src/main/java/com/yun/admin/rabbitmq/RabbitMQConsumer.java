package com.yun.admin.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQConsumer {

    @RabbitListener(queues = "direcle")
    public void receive(String msg){
        log.info("direcle接收到的消息---"+msg);
    }

    @RabbitListener( queues=("topic.msg1"))
    public void topicReceive1(String msg){
        log.info("topic1接收到的消息---"+msg);
    }

    @RabbitListener( queues=("topic.msg2"))
    public void topicReceive2(String msg){
        log.info("topic2接收到的消息---"+msg);
    }

}
