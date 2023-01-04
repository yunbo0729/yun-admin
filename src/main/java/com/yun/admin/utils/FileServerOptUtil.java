package com.yun.admin.utils;

import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @description: MinIO文件服务器工具类
 * @author: dyg
 * @date: 2022/4/14 19:03
 */
@Slf4j
public class FileServerOptUtil {
    /**
     * 判断MinIO中桶是否存在
     *
     * @param bucketName
     * @return
     */
    public static boolean bucketExist(MinioClient minioClient, String bucketName) throws Exception {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("---判断桶{}在MinIo服务器是否存在发生异常 {}", bucketName, e);
            throw new Exception(e);
        }
    }

    /**
     * 创建桶
     *
     * @param bucketName
     * @return
     */
    public static boolean createBucket(MinioClient minioClient, String bucketName) throws Exception {
        try {
            if (!bucketExist(minioClient, bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                return true;
            }
            log.error("----MinIo上当前桶{}存在", bucketName);
            return false;
        } catch (Exception e) {
            log.error("---在MinIo上创建桶{}发生异常{}", bucketName, e);
            throw new Exception(e);
        }
    }
    /**
     * 指定桶中上传文件
     *
     * @param bucketName
     * @param file
     * @return
     */
    public static String uploadFile(MinioClient minioClient, String bucketName, MultipartFile file, String fileName) throws Exception {
        try {
            InputStream inputStream = file.getInputStream();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .contentType(file.getContentType())
                    .stream(inputStream, file.getSize(), -1)
                    .object(fileName)
                    .build();
            minioClient.putObject(putObjectArgs);
            String fileUrl = getFileUrl(minioClient, bucketName, fileName);
            return fileUrl;
        } catch (Exception e) {
            log.error("---在往MinIo的桶{}上传文件{}时发生错误{}", bucketName, file.getName(), e);
            throw new Exception(e);
        }
    }
    /**
     * 获取上传文件的访问URL
     *
     * @param objectFile
     * @return
     */
    public static String getFileUrl(MinioClient minioClient, String bucketName, String objectFile) throws Exception {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectFile)
                    .build()
            );
        } catch (Exception e) {
            log.error("---获取上传文件的url发生异常 {}", e);
            throw new Exception(e);
        }
    }
    /**
     * 删除指定文件
     *
     * @param bucketName
     * @param fileName
     * @return
     */
    public static boolean deleteFile(MinioClient minioClient, String bucketName, String fileName) throws Exception {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("---获取上传文件的url发生异常 {}", e);
            throw new Exception(e);
        }
    }
    /**
     * 下载文件
     *
     * @param bucketName
     * @param fileName
     * @param toPath
     * @return
     */
    public static boolean downloadFile(MinioClient minioClient, String bucketName, String fileName, String toPath) throws Exception {
        try {
            minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .filename(toPath)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("---下载桶{}文件的{}发生异常 {}", bucketName, fileName, e);
            throw new Exception(e);
        }
    }

    /**
     * 设置浏览器下载响应头
     *
     * @param response
     * @param fileName
     */
    public static void setResponseHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {
            fileName = new String(fileName.getBytes(), "ISO8859-1");
            response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            response.setContentType("application/octet-stream");
            // 这后面可以设置导出Excel的名称，此例中名为student.xls
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }
    }
}
