package com.yun.admin.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


/**
 * RabbitMQ支持*匹配扫描包
 */
@Slf4j
@Configuration
public class RabbitMQConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*
    * 直连队列  队列和消费者一一对应
    * */
    @Bean
    Queue direcleQueue() {
        return new Queue("direcle");
    }

    /*
    * 队列1
    * */
    @Bean
    Queue topicQueue1() {
        return new Queue("topic.msg1");
    }
    /*
     * 队列2
     * */
    @Bean
    Queue topicQueue2() {
        return new Queue("topic.msg2");
    }
    /*
    * 交换机
    * */
    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange("topicExchange");
    }

    /*
    * 将队列topicQueue1绑定到交换机上
    * */
    @Bean
    Binding binding1(@Qualifier("topicQueue1")Queue topicQueue1, TopicExchange topicExchange){
        return BindingBuilder.bind(topicQueue1).to(topicExchange).with("topic.msg1");
    }

    /*
     * 将队列topicQueue2绑定到交换机topicExchange上
     * */
    @Bean
    Binding binding2(@Qualifier("topicQueue2")Queue topicQueue2, TopicExchange topicExchange){
        return BindingBuilder.bind(topicQueue2).to(topicExchange).with("topic.#");
    }



    /**
     * 队列：
     */
    @Bean
    public Queue queue01() {
        return new Queue("queue01", true, false, false);
    }

    /**
     * 交换机
     */
    @Bean
    public Exchange exchange01() {
        return new DirectExchange("exchange01", true, false);
    }

    /**
     * 绑定
     */
    @Bean
    public Binding binding() {
        return new Binding("queue01", Binding.DestinationType.QUEUE, "exchange01", "send.msg", null);
    }

    @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                String id = correlationData.getId();
                if (!ack) {
                    log.info("id为: {}的消息,未能抵达broker, 原因: {}", id, cause);
                } else {
                    log.info("broker已接收id为: {}的消息", id);
                }
            }
        });
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                // 该消息抵达的交换机
                String exchange = returned.getExchange();
                // 消息体的详细信息
                Message message = returned.getMessage();
                // 回复的状态码
                int replyCode = returned.getReplyCode();
                // 该消息使用的路由键
                String routingKey = returned.getRoutingKey();
                // 回复的文本内容
                String replyText = returned.getReplyText();
                log.info(
                        "exchange: {}, message: {}, replyCode: {}, routingKey: {}, replyText: {}",
                        exchange, message, replyCode, routingKey, replyText
                );
            }
        });
    }
}
