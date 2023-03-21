package com.yun.admin.rabbitmq.model.dto;

import lombok.Data;

@Data
public class MessageDto {
    private String id;
    private String name;
    private String message;
    private Integer ttlTime;
}
