package com.yun.admin.test;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSend {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send() {
        amqpTemplate.convertAndSend("direcle", "这是直连模式");
    }

    public void topicSend(){
        amqpTemplate.convertAndSend("topicExchange","topic.msg1","这里是topic模式");
    }
}
