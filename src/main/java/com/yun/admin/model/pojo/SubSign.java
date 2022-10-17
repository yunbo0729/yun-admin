package com.yun.admin.model.pojo;


import lombok.Data;

@Data
public class SubSign {
    private static final long serialVersionUID = 1L;
    /** 签到主键 */
    private Long id;
    /** 用户id */
    private String userId;
    /** 签入时间 */
    private String signInTime;
    /** 签出时间 */
    private String signOutTime;
    /** 创建时间" */
    private String createTime;
}
