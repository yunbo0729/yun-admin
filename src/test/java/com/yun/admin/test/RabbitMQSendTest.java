package com.yun.admin.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest()
@RunWith(SpringJUnit4ClassRunner.class)
public class RabbitMQSendTest {
    @Autowired
    RabbitMQSend rabbitMQSend;

    @Test
    public void send(){
        rabbitMQSend.send();
    }
    @Test
    public void topicSend(){
        rabbitMQSend.topicSend();
    }
}