package com.yun.admin.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: Web接口返回信息
 * @author: dyg
 * @date: 2022/3/22 17:39
 */
@Data
@Builder
@ApiModel(value = "接口返回参数")
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {
    public static final int SUCC_CODE = 200;
    public static final int FAIL_CODE = 9999;
    public static final String SUCC_STATUS = "succ";
    public static final String FAIL_STATUS = "fail";
    /**
     * 返回码
     * - 200 正常返回
     * - 9999 异常返回
     */
    @ApiModelProperty(name = "返回码", dataType = "int", notes = "200 正常返回", required = false)
    private int code;
    /**
     * 返回状态
     * - succ : 成功
     * - fail : 失败
     */
    @ApiModelProperty(name = "返回状态", dataType = "string", notes = "返回状态", required = false)
    private String status;
    /**
     * 返回异常信息
     * - 200 为空
     * - 9999 异常信息
     */
    @ApiModelProperty(name = "返回异常信息", dataType = "string", notes = "200 为空 非200-异常信息 ", required = false)
    private String msg;
    /**
     * 返回数据
     */
    @ApiModelProperty(name = "返回数据", dataType = "object", notes = "返回数据", required = false)
    private Object data;
    @ApiModelProperty(name = "返回分页数据",dataType = "list",notes = "返回分页数据", required = false)
    private List pageData;
    /**
     * 分页数据相关
     */
    @ApiModelProperty(name = "总条数", dataType = "int", notes = "总条数", required = false)
    private Integer count;
    @ApiModelProperty(name = "每页条数", dataType = "int", notes = "每页条数", required = false)
    private Integer pageSize;
    @ApiModelProperty(name = "页数", dataType = "int", notes = "页数", required = false)
    private Integer pageIndex;

    /**
     * 不带数据的成功调用
     *
     * @return
     */
    public static Result okWithOutData() {
        return Result.builder()
                .code(SUCC_CODE)
                .status(SUCC_STATUS)
                .msg("")
                .build();
    }

    /**
     * 不带数据的异常调用
     *
     * @param message
     * @return
     */
    public static Result errorWithOutData(String message) {
        return Result.builder()
                .code(FAIL_CODE)
                .status(FAIL_STATUS)
                .msg(message)
                .build();
    }

    /**
     * 带数据不支持分页的成功调用
     *
     * @param val
     * @return
     */
    public static Result okWithDataNoPage(Object val) {
        return Result.builder()
                .code(SUCC_CODE)
                .status(SUCC_STATUS)
                .data(val)
                .build();
    }
    public static Result okWithDataNoPage(String message,Object val) {
        return Result.builder()
                .code(SUCC_CODE)
                .status(SUCC_STATUS)
                .data(val)
                .msg(message)
                .build();
    }
}
