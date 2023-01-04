package com.yun.admin.conf;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @description: MinIO配置
 * @author: dyg
 * @date: 2022/4/14 18:59
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
@Slf4j
public class MinIoConfig {
    /**
     * 服务器地址
     * -- 代理地址 使用nginx代理
     */
    private String minioUrl;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 乘客端文件上传保存桶名
     */
    private String passagerBucket;
    /**
     * 司机端文件上传保存桶名
     */
    private String driverBucket;
    /**
     * 后端营销系统上传文件保存桶名
     */
    private String systemBucket;
    /**
     * 后端营销minyun桶
     */
    private String minyunBucket;
    @Bean
    public MinioClient minioClient(){
        log.info("初始化MinioClient客户端：minioUrl:" + minioUrl + ",accessKey:" + accessKey + ",secretKey:" + secretKey);
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
        return minioClient;
    }
    @PostConstruct
    public void createBucket(){
        MinioClient minioClient = minioClient();
        try{
            if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(passagerBucket).build())){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(passagerBucket).build());
                log.info("----成功创建MinIO-乘客端的桶{}",passagerBucket);
            }
            if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(driverBucket).build())){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(driverBucket).build());
                log.info("----成功创建MinIO-司机端的桶{}",driverBucket);
            }
            if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(systemBucket).build())){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(systemBucket).build());
                log.info("----成功创建MinIO-后台营销系统的桶{}",systemBucket);
            }
            if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minyunBucket).build())){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minyunBucket).build());
                log.info("----成功创建MinIO-后台营销系统的桶{}",minyunBucket);
            }
        }catch (Exception e){
            log.error("-----初始化创建MinIo桶、创建临时下载目录 发生异常{} ",e);
        }
    }
}
