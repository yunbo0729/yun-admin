package com.yun.admin.controller;

import com.yun.admin.service.FileInfoService;
import com.yun.admin.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: 文件相关数据
 * @author: dyg
 * @date: 2022/4/15 8:58
 */
@RestController
@Slf4j
@RequestMapping(value = "/minio")
public class MinIoFileController {
    @Autowired
    private FileInfoService fileInfoService;

    @PostMapping(value = "/upload")
    public Result uploadFile(@RequestBody MultipartFile file,
                             @RequestParam(value = "userType") Integer userType,
                             @RequestParam("uid") String uid) throws Exception {
        if (null == file) {
            return Result.errorWithOutData("上传文件不能为空");
        }
        log.info("----uid为 {}的{}用户 上传文件{}", uid, userType, file.getOriginalFilename());
        return fileInfoService.uploadFileAndSaveInfoIntoDb(file, userType, uid);
    }

    @GetMapping(value = "/view")
    public Result viewFile(@RequestParam(value = "id") Integer id) {
        return fileInfoService.viewFileById(id);
    }

    @GetMapping(value = "/downLoad")
    public void downLoad(@RequestParam(value = "id") Integer id, HttpServletResponse resp) throws Exception {
        fileInfoService.downLoadFile(resp, id);
    }

    @GetMapping("/delete")
    public Result removeFileById(@RequestParam(value = "id") Integer id) {
        return fileInfoService.removeFileById(id);
    }
}
