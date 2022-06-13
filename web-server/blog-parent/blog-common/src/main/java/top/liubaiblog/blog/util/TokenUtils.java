package top.liubaiblog.blog.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import top.liubaiblog.blog.properties.JWTProperties;

import java.util.*;

/**
 * @author 留白
 * @description 生成token工具类
 */
@Slf4j
public class TokenUtils {

    private final static JWTProperties jwtProperties;

    static {
        jwtProperties = (JWTProperties) SpringAppUtils.getBean(JWTProperties.class);
    }

    /**
     * 创建token
     */
    public static String create(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey())   // 签名算法和密钥
                .setId(UUID.randomUUID().toString())    // token的id
                .setClaims(claims)          // body参数
                .setIssuedAt(new Date())    // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))    // 过期时间
                .compact();
    }

    /**
     * 解析token
     */
    public static Map<String, Object> parse(String token) {
        try {
            Jwt parse = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parse(token);
            Assert.notNull(parse, token + "解析为空");
            return (Map<String, Object>) parse.getBody();
        } catch (Exception e) {
            log.warn(e.getMessage());
//            if (!(e instanceof ExpiredJwtException)) e.printStackTrace();
            return Collections.emptyMap();
        }
    }


}
