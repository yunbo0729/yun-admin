package com.yun.admin.rabbitmq.service;

import com.alibaba.fastjson.JSONObject;


public interface RabbitMQService {

    /**
     * 交换机类型 topic
     * 回调方法 ：confirm 、returnedMessage
     * uuid：回调类设置业务id 唯一 CorrelationData
     * jsonObject：消息体
     */
    void topicSend(JSONObject jsonObject, String uuid);

    /**
     * ack 确认机制
     * 回调方法 ：confirm 、returnedMessage
     * uuid：回调类设置业务id 唯一 CorrelationData
     * jsonObject：消息体
     */
    void ackSend(JSONObject jsonObject, String uuid);

    /**
     * 模拟死信队列
     * 基于插件：rabbitmq_delayed_message_exchange
     * jsonObject：消息体
     * ttlTime：消息延时时间  毫秒
     */
    void ttlZSend(JSONObject jsonObject, Integer ttlTime);
}
