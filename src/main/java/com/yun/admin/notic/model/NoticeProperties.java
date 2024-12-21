package com.yun.admin.notic.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @descriptions: 配置参数
 * @author: xucl
 */
@Component
@Data
@ConfigurationProperties(prefix = "notice")
public class NoticeProperties {

    private String wechatKey;

    private List<String> phoneList;
}

