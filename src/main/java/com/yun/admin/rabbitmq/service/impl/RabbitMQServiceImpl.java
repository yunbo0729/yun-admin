package com.yun.admin.rabbitmq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yun.admin.conf.RabbitMQConfig;
import com.yun.admin.rabbitmq.service.RabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class RabbitMQServiceImpl implements RabbitMQService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void topicSend(JSONObject jsonObject, String uuid) {
        log.info("当前时间:{},发送信息给topic.msg1队列:{}", new Date(), jsonObject);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(uuid);
        rabbitTemplate.convertAndSend("topicExchange", "topic.msg1", jsonObject,correlationData);
    }

    @Override
    public void ackSend(JSONObject jsonObject, String uuid) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(uuid);
        rabbitTemplate.convertAndSend("exchange01", "send.msg", jsonObject, correlationData);
    }

    @Override
    public void ttlZSend(JSONObject jsonObject, Integer ttlTime) {
        log.info("当前时间:{},发送时长为{}毫秒的信息给延迟队列队列delayed.queue:{}", new Date(), ttlTime, jsonObject);
        rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_EXCHANGE_NAME
                , RabbitMQConfig.DELAYED_ROUTING_KEY, jsonObject, message -> {
                    //设置发送消息的延迟时间
                    message.getMessageProperties().setDelay(ttlTime);
                    return message;
                }, new CorrelationData(UUID.randomUUID().toString()));
    }
}
