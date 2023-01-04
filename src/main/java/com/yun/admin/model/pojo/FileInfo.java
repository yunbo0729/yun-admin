package com.yun.admin.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: 文件信息
 * @author: dyg
 * @date: 2022/4/14 19:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "file_info")
public class FileInfo implements Serializable {
    private static final long serialVersionUID = 6943641372262086829L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 文件名称
     */
    @NotNull(message = "文件名称不能为空")
    @NotEmpty(message = "文件名称不能为空")
    private String name;

    /**
     * MinIo中访问Url
     */
    private String minioUrl;
    /**
     * 桶名称
     */
    @NotNull(message = "桶名称不能为空")
    @NotEmpty(message = "桶名称不能为空")
    private String bucketName;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 是否删除
     * 1-否  0-是
     */
    @TableLogic
    private Integer isDeleted;
    /**
     * 上传人
     */
    private String uploadPhone;
}