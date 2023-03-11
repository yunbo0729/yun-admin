package com.yun.admin.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RabbitMQSend {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send() {
        rabbitTemplate.convertAndSend("exchange01", "send.msg", "这是直连模式", new CorrelationData(UUID.randomUUID().toString()));
    }
    public void send1() {
        amqpTemplate.convertAndSend("direcle", "这是直连模式");
    }
    public void topicSend() {
        amqpTemplate.convertAndSend("topicExchange", "topic.msg1", "这里是topic模式");
    }
}
