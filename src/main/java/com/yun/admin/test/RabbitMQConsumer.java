package com.yun.admin.test;

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
    @RabbitListener( queues=("topic.msg2"))
    public void topicReceive(String msg){
        log.info("topic接收到的消息---"+msg);
    }

}
