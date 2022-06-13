package top.liubaiblog.blog.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.liubaiblog.blog.service.UploadService;
import top.liubaiblog.blog.util.QiNiuUploadUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author 留白
 * @description 文件上传服务实现类
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Autowired
    private QiNiuUploadUtils uploadUtils;

    @Override
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        // 文件前缀为项目+当前日期组成的目录
        String prefix = "blog/" + new DateTime(System.currentTimeMillis()).toString("yyyy-MM-dd") + "/";
        // 获取文件后缀
        String suffix = null;
        if (originalFilename != null) suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 上传的文件名用UUID生成
        String key = prefix + UUID.randomUUID() + suffix;
        try {
            if (uploadUtils.upload(file.getInputStream(), key)) {
                return uploadUtils.getProperties().getUrl() + "/" + key;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkFileSuffix(MultipartFile file, List<String> allowSuffix) {
        if (file == null || allowSuffix == null) return false;
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) return false;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        log.info("检测到文件后缀为 -> {}", suffix);
        return allowSuffix.contains(suffix);
    }

}
