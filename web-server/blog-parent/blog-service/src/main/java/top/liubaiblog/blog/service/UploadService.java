package top.liubaiblog.blog.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 留白
 * @description 文件上传服务类
 */
public interface UploadService {

    /**
     * 文件上传，返回上传后的文件url
     */
    String upload(MultipartFile file);

    /**
     * 检查文件后缀，文件后缀数组中不需要带"."
     */
    boolean checkFileSuffix(MultipartFile file, List<String> allowSuffix);

}
