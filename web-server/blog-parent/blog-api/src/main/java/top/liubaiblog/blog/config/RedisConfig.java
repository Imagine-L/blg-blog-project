package top.liubaiblog.blog.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

/**
 * @author 留白
 * @description redis配置类
 */
@Configuration
@Import(CacheProperties.class)
public class RedisConfig {

    @Bean("myRedisTemplate")
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // redisTemplate一般使用<String, Object>的泛型
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(factory);

        // json 序列化配置(利用jackson工具)
        Jackson2JsonRedisSerializer<Object> jacksonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域，PropertyAccessor.ALL表示要序列化的所有字段，JsonAutoDetect.Visibility.ANY表示任何权限修饰符
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);  // 此方法已过时，下行是替代的方案
        // 指定序列化输入的类型，DefaultTyping.NON_FINAL表示非final修饰的类，
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jacksonRedisSerializer.setObjectMapper(objectMapper);
        // String 序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key 采用String方式序列化
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value 采用jackson序列化
        redisTemplate.setValueSerializer(jacksonRedisSerializer);
        redisTemplate.setHashValueSerializer(jacksonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 配置redis作为缓存
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory, CacheProperties properties) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jacksonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域，PropertyAccessor.ALL表示要序列化的所有字段，JsonAutoDetect.Visibility.ANY表示任何权限修饰符
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);  // 此方法已过时，下行是替代的方案
        // 指定序列化输入的类型，DefaultTyping.NON_FINAL表示非final修饰的类，
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        //解决查询缓存转换异常的问题
        jacksonRedisSerializer.setObjectMapper(objectMapper);

        // Redis缓存配置类
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(properties.getRedis().getTimeToLive())                      // 过期时间600秒
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(stringRedisSerializer))         // 配置key的序列化方式
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jacksonRedisSerializer))        // 配置value的序列化方式
                .disableCachingNullValues();                            // 禁止缓存空值

        // 根据redis链接工厂和配置构造一个CacheManager
        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }

}
