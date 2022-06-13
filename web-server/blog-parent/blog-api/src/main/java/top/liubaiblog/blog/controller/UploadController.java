package top.liubaiblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.liubaiblog.blog.annotation.WriteLog;
import top.liubaiblog.blog.service.UploadService;
import top.liubaiblog.blog.vo.ResponseCode;
import top.liubaiblog.blog.vo.param.ResponseData;

import java.util.Arrays;

/**
 * @author 留白
 * @description 文件上传控制器
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping
    @WriteLog(module = "文件", operation = "文件上传操作")
    public ResponseData upload(@RequestParam("image") MultipartFile image) {
        if (uploadService.checkFileSuffix(image, Arrays.asList("jpg", "png", "gif", "jpeg", "svg", ".ico"))) {
            String uploadFilename = uploadService.upload(image);
            return uploadFilename != null ? ResponseData.success(uploadFilename) :
                    ResponseData.build(ResponseCode.UNKNOWN, null);
        } else {
            return ResponseData.build(ResponseCode.FILE_SUFFIX_ILLEGAL, null);
        }
    }

}
