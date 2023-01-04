package com.yun.admin.mapper;


import com.yun.admin.model.pojo.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileInfoMapper {
    Integer insert(FileInfo fileInfo);

    @Select("select * from file_info where id = #{id}")
    @Results({@Result(column = "minio_url", property = "minioUrl"),
            @Result(column = "bucket_name", property = "bucketName"),
            @Result(column = "file_size", property = "fileSize"),
            @Result(column = "upload_time", property = "uploadTime"),
            @Result(column = "upload_phone", property = "uploadPhone"),
            @Result(column = "is_deleted", property = "isDeleted")})
    FileInfo getById(Integer id);

    void removeById(Integer id);
}
