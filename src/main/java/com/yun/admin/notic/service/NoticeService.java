package com.yun.admin.notic.service;

/**
 * @descriptions: 消息通知接口
 * @author: xucl
 */
public interface NoticeService {

    /**
     * 发送错误信息至群机器人
     * @param throwable
     * @param msg
     */
    void sendError(Throwable throwable,String msg);

    /**
     * 发送文本信息至群机器人
     * @param msg
     */
    void sendByMd(String msg);

    /**
     * 发送md至群机器人
     * @param msg 文本消息
     * @param isAtALL  是否@所有人 true是 false否
     */
    void sendByText(String msg,boolean isAtALL);
}

