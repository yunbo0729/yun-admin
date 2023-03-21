package com.yun.admin.rabbitmq.controller;

import com.alibaba.fastjson.JSONObject;
import com.yun.admin.rabbitmq.model.dto.MessageDto;
import com.yun.admin.rabbitmq.service.RabbitMQService;
import com.yun.admin.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/rabbitmq")
public class RabbitMQTTLController {
    @Autowired
    private RabbitMQService rabbitMQService;

    /**
     * 确认机制队列
     */
    @PostMapping("/topicSend")
    public Result topicSend(@RequestBody MessageDto messageDto) {
        String uuid = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", messageDto.getId());
        jsonObject.put("name", messageDto.getName());
        jsonObject.put("message",messageDto.getMessage());
        rabbitMQService.topicSend(jsonObject, uuid);
        return Result.okWithDataNoPage("ok");
    }

    /**
     * 确认机制队列
     */
    @PostMapping("/ackSend")
    public Result ackSend(@RequestBody MessageDto messageDto) {
        String uuid = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", messageDto.getId());
        jsonObject.put("name", messageDto.getName());
        rabbitMQService.ackSend(jsonObject, uuid);
        return Result.okWithDataNoPage("ok");
    }

    /**
     * 延时队列
     * 基于插件：rabbitmq_delayed_message_exchange
     * 生产者，发送消息和延迟时间
     */
    @PostMapping("/ttlZSend")
    public Result ttlZSend(@RequestBody MessageDto messageDto) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", messageDto.getId());
        jsonObject.put("name", messageDto.getName());
        rabbitMQService.ttlZSend(jsonObject, messageDto.getTtlTime());
        return Result.okWithDataNoPage("ok");
    }

}
