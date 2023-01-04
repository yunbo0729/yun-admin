package com.yun.admin.service;

import com.sun.deploy.net.URLEncoder;
import com.yun.admin.conf.MinIoConfig;
import com.yun.admin.model.pojo.FileInfo;
import com.yun.admin.mapper.FileInfoMapper;
import com.yun.admin.utils.Result;
import com.yun.admin.utils.StringUtils;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;

import static com.yun.admin.utils.FileServerOptUtil.deleteFile;
import static com.yun.admin.utils.FileServerOptUtil.uploadFile;

/**
 * @description: 文件处理相关
 * @author: dyg
 * @date: 2022/4/15 8:59
 */
@Service
@Slf4j
public class FileInfoService  {
    @Autowired
    MinioClient minioClient;
    @Autowired
    private MinIoConfig minIoConfig;
    @Autowired
    private FileInfoMapper fileInfoMapper;

    /**
     * 上传文件保存信息
     *
     * @param file
     * @param type
     * @param phone
     * @return 文件在MinIO访问地址
     */
    @Transactional(rollbackFor = Exception.class)
    public Result uploadFileAndSaveInfoIntoDb(MultipartFile file, Integer type, String phone) throws Exception{
        try {
            String bucketName = "";
            if (1 == type.intValue()) {
                bucketName = minIoConfig.getPassagerBucket();
            } else if (2 == type.intValue()) {
                bucketName = minIoConfig.getDriverBucket();
            } else if (3 == type.intValue()) {
                bucketName = minIoConfig.getMinyunBucket();
            } else {
                return Result.errorWithOutData("请选择 上传文件类型 客户文件-passager 司机文件-driver");
            }
            String originalFilename = file.getOriginalFilename();
            String name = file.getName();
            long size = file.getSize();
            // 是否要有文件大小限制
            String fileName = System.currentTimeMillis() + "-" + originalFilename;
            String url = uploadFile(minioClient, bucketName, file, fileName);
            FileInfo fileInfo = FileInfo.builder()
                    .name(fileName)
                    .uploadTime(new Date())
                    .bucketName(bucketName)
                    .fileSize(size)
                    .minioUrl(url)
                    .uploadPhone(phone)
                    .isDeleted(1)
                    .build();
            fileInfoMapper.insert(fileInfo);

            return Result.okWithDataNoPage(fileInfo);
        } catch (Exception e) {
            log.error("----调用上传文件接口发生异常 {} ", e);
            return Result.errorWithOutData(e.toString());
        }
    }

    /**
     * 预览文件
     *
     * @param id
     * @return
     */
    public Result viewFileById(Integer id) {
        FileInfo fileInfo = fileInfoMapper.getById(id);
        if (Objects.isNull(fileInfo)) {
            return Result.errorWithOutData("当前Id未查询到对应的文件");
        }
        String minIoUrl = fileInfo.getMinioUrl();
        if (StringUtils.isEmpty(minIoUrl)) {
            return Result.errorWithOutData("当前Id查询到文件获取到的url为空");
        }
        return Result.okWithDataNoPage(minIoUrl);
    }

    /**
     * 删除文件
     *
     * @param id
     * @return
     */
    public Result removeFileById(Integer id) {
        try {
            FileInfo fileInfo = fileInfoMapper.getById(id);
            if (Objects.isNull(fileInfo)) {
                return Result.errorWithOutData("当前Id未查询到对应的文件");
            }
            String bucketName = fileInfo.getBucketName();
            String name = fileInfo.getName();

            deleteFile(minioClient, bucketName, name);
            fileInfoMapper.removeById(id);

            return Result.okWithOutData();
        } catch (Exception e) {
            log.error("---删除文件{} 发生异常{} ", id, e);
            return Result.errorWithOutData(e.getMessage());
        }
    }

    /**
     * 文件下载
     *
     * @param response
     * @param id
     */
    public void downLoadFile(HttpServletResponse response, Integer id) throws Exception {
        FileInfo fileInfo = fileInfoMapper.getById(id);
        if (Objects.isNull(fileInfo)) {
            throw new Exception("当前Id未查询到对应的文件");
        }
        String minIoUrl = fileInfo.getMinioUrl();
        if (StringUtils.isEmpty(minIoUrl)) {
            throw new Exception("当前Id查询到文件获取到的url为空");
        }
        String name = fileInfo.getName();
        String bucketName = fileInfo.getBucketName();
        try (InputStream ism = new BufferedInputStream(minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(name).build()))) {
            /**
             * 调用statObject()来判断对象是否存在。
             - 如果不存在, statObject()抛出异常,
             - 否则则代表对象存在
             */
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name).build());
            byte buf[] = new byte[1024];
            int length = 0;
            response.reset();
            /**
             * Content-disposition 是 MIME 协议的扩展，MIME 协议指示 MIME 用户代理如何显示附加的文件。
             - Content-disposition其实可以控制用户请求所得的内容存为一个文件的时候提供一个默认的文件名，
             - 文件直接在浏览器上显示或者在访问时弹出文件下载对话框。
             */
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8"));
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("utf-8");
            OutputStream osm = new BufferedOutputStream(response.getOutputStream());
            while ((length = ism.read(buf)) > 0) {
                osm.write(buf, 0, length);
            }
            osm.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
