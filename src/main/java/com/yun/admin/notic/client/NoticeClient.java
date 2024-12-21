package com.yun.admin.notic.client;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Var;
import com.dtflys.forest.http.ForestResponse;
import com.google.gson.JsonObject;

import java.util.Map;

public interface NoticeClient {


    /**
     * 企业微信机器人 发送 https 请求
     *
     * @param keyValue
     * @return
     */
    @Post(
            url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key={keyValue}",
            headers = {
                    "Accept-Charset: utf-8",
                    "Content-Type: application/json"
            },
            dataType = "json"
    )
    ForestResponse<JsonObject> weChatNotice(@Var("keyValue") String keyValue, @JSONBody Map<String, Object> body);
}


