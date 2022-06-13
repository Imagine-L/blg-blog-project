package top.liubaiblog.blog.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 留白
 * @description 七牛云相关配置
 */
@Data
@ConfigurationProperties("qiniu")
public class QiNiuCloudProperties {

    @Value("${qiniu.secure.accessKey}")
    private String accessKey;
    @Value("${qiniu.secure.secretKey}")
    private String secretKey;
    private String url;
    private String bucket;

}
