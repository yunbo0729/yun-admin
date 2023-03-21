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
import java.util.HashMap;
import java.util.Map;


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
     * 交换机
     * */
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    /*
     * 队列1
     * */
    @Bean
    Queue topicQueue1() {
        return new Queue("topic.msg1");
    }

    /*
     * 将队列topicQueue1绑定到交换机上
     * */
    @Bean
    Binding binding1(@Qualifier("topicQueue1") Queue topicQueue1, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue1).to(topicExchange).with("topic.msg1");
    }

    /*
     * 队列2
     * */
    @Bean
    Queue topicQueue2() {
        return new Queue("topic.msg2");
    }

    /*
     * 将队列topicQueue2绑定到交换机topicExchange上
     * */
    @Bean
    Binding binding2(@Qualifier("topicQueue2") Queue topicQueue2, TopicExchange topicExchange) {
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




    public static final String X_EXCHANGE = "X";
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String DEAD_LETTER_QUEUE = "QD";
    //新的普通队列QC
    public static final String QUEUE_C = "QC";

    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> args = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(args).build();
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    // 声明 xExchange
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    // 声明 xExchange
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明死信队列 QD
    @Bean("queueD")
    public Queue queueD() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    //声明死信队列 QD 绑定关系
    @Bean
    public Binding deadLetterBindingQAD(@Qualifier("queueD") Queue queueD,
                                        @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }



    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    //声明交换机
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        /**
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否需要持久化
         * 4.是否需要自动删除
         * 5.其他参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message",
                true, false, args);
    }

    //声明队列
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //绑定
    @Bean
    public Binding bindingDelayedQueue(@Qualifier("delayedQueue") Queue delayedQueue,
                                       @Qualifier("delayedExchange") CustomExchange delayedExchange) {

        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
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
