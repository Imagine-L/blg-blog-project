package top.liubaiblog.blog.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author 留白
 * @description
 */
@Component
public class CacheKeyGenerator implements KeyGenerator {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object generate(Object target, Method method, Object... params) {
        // 目标方法所在类的全类名
        String className = target.getClass().toString().substring(6);
        // 目标方法的方法名
        String methodName = method.getName();
        // 目标方法的全部参数
        StringBuilder paramsSb = new StringBuilder();
        Arrays.stream(params).forEach(param -> {
            String paramStr = null;
            try {
                paramStr = objectMapper.writeValueAsString(param);
                paramsSb.append(paramStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        String paramStr = null;
        if (StringUtils.hasLength(paramsSb)) {
            // 加密，以防止出现key过长以及字符转义获取不到的情况
            paramStr = DigestUtils.md5DigestAsHex(paramsSb.toString().getBytes(StandardCharsets.UTF_8));
        }
        String cacheKey = className + ":" + methodName + ":" + paramStr;
        return cacheKey;
    }

}
