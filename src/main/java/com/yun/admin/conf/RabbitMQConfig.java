package com.yun.admin.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * RabbitMQ支持*匹配扫描包
 */
@Configuration
public class RabbitMQConfig {

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
}
