package top.liubaiblog.blog.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 留白
 * @description 检测文件上传的过滤器
 */
@Slf4j
public class CheckFileInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是文件上传的请求，则直接放行
        if (!ServletFileUpload.isMultipartContent(request)) {
            return true;
        }
        ServletFileUpload upload = new ServletFileUpload();
        List<FileItem> fileItems = upload.parseRequest((RequestContext) request);
        fileItems.stream().filter(fileItem -> !fileItem.isFormField()).forEach(fileItem -> {
            String filename = fileItem.getName();
            log.info("上传的文件名：{}", fileItem);
        });
        return true;
    }
}
