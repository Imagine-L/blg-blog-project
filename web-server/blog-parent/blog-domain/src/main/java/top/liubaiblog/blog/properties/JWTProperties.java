package top.liubaiblog.blog.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 留白
 * @description
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {
    private String secretKey;   // 密钥
    private Long expiration = 1000 * 60 * 60 * 24L;    // 过期时间
}
