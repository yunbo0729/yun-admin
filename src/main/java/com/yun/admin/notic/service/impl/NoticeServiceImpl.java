package com.yun.admin.notic.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dtflys.forest.http.ForestResponse;
import com.google.gson.JsonObject;
import com.yun.admin.notic.client.NoticeClient;
import com.yun.admin.notic.model.NoticeProperties;
import com.yun.admin.notic.service.NoticeService;
import com.yun.admin.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.profiles.active}")
    private String env;

    @Autowired
    private NoticeClient noticeClient;

    @Autowired
    private NoticeProperties noticeProperties;

    /**
     * 发送错误信息至群机器人
     *
     * @param throwable
     * @param msg
     */
    @Override
    public void sendError(Throwable throwable, String msg) {
        String errorClassName = throwable.getClass().getSimpleName();
        if (StringUtils.isBlank(msg)){
            msg = throwable.getMessage() == null ? "出现null值" : throwable.getMessage();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(baos));
        String bodyStr = StringUtils.limitStrNone(regexThrowableStr(baos.toString()),450);
        String md = getMdByTemplate(appName, env, errorClassName, msg, bodyStr);
        sendByMd(md);
    }

    /**
     * 发送文本信息至群机器人
     *
     * @param msg
     */
    @Override
    public void sendByMd(String msg) {
        try {
            Map<String, Object> params = buildMdParams(msg);
            ForestResponse<JsonObject> response = noticeClient.weChatNotice(noticeProperties.getWechatKey(), params);
            log.debug("WeChatRobo-Send Error:{} Status:{}",response.isError(),response.getStatusCode());
        } catch (Exception e) {
            log.error("WeChatRobot-发送文本消息异常 body:{}",msg,e);
        }
    }

    /**
     * 发送md至群机器人
     *
     * @param msg
     */
    @Override
    public void sendByText(String msg,boolean isAtALL) {
        try {
            Map<String, Object> params = buildTextParams(msg,noticeProperties.getPhoneList(),isAtALL);
            ForestResponse<JsonObject> response = noticeClient.weChatNotice(noticeProperties.getWechatKey(), params);
            log.debug("WeChatRobo-Send Error:{} Status:{}",response.isError(),response.getStatusCode());
        } catch (Exception e) {
            log.error("WeChatRobot-发送文本消息异常 body:{}",msg,e);
        }
    }

    /**
     * 构建发送文本消息格式的参数
     * @param phoneList @群用户
     * @param isAtALL  是否@所有人
     * @return
     */
    private Map<String,Object> buildTextParams(String text, List<String> phoneList, boolean isAtALL){
        Map<String,Object> params = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        data.put("content",text);
        if (isAtALL){
            phoneList.add("@all");
        }
        if (CollectionUtils.isNotEmpty(phoneList)){
            data.put("mentioned_mobile_list", phoneList);
        }
        params.put("msgtype","text");
        params.put("text",data);
        return params;
    }

    /**
     * 构建发送markdown消息格式的参数
     *
     * @param md
     * @return
     */
    private Map<String,Object> buildMdParams(String md){
        Map<String,Object> params = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        data.put("content",md);
        params.put("msgtype","markdown");
        params.put("markdown",data);
        return params;
    }


    private String regexThrowableStr(String str){
        try {
            // 注意：这里的包名（com.github）填自己项目内的包名
            String pattern = "(com)(\\.)(github)(.{10,200})(\\))";
            Pattern r = Pattern.compile(pattern);
            Matcher m=r.matcher(str);
            List<String> list = new ArrayList<>();
            while (m.find()) {
                list.add(m.group());
            }
            if (CollectionUtils.isEmpty(list)){
                return str;
            }
            String s = list.stream().collect(Collectors.joining("\n"));
            return s;
        } catch (Exception e) {
            return str;
        }
    }


    private String getMdByTemplate(String appName,String env,String errorClassName,String msg,String bodyStr){
        String titleTpl = "### 异常告警通知\n#### 应用：%s\n#### 环境：<font color=\"info\">%s</font>\n##### 异常：<font color=\"warning\">%s</font>\n";
        String bodyTpl = "\nMsg：%s\nDetail：\n>%s";
        String footerTpl = "\n<font color=\"comment\">%s</font>";
        String title = String.format(titleTpl, appName, env, errorClassName);
        String body = String.format(bodyTpl, msg,bodyStr);
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        String footer = String.format(footerTpl, dateStr);
        return title.concat(body).concat(footer);
    }
}

