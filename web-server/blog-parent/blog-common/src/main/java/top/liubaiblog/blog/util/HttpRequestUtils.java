package top.liubaiblog.blog.util;

import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.liubaiblog.blog.constant.SecureConstants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author 留白
 * @description 请求工具类
 */
@Component
public class HttpRequestUtils {

    public final static String USER_AGENT_STRING = "User-Agent";

    /**
     * 获取当前的请求，这个对象是ThreadLocal维护的，是当前线程请求的副本
     */
    public HttpServletRequest getLocalRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(requestAttributes, "当前线程请求为空").getRequest();
    }

    /**
     * 解析当前线程的UserAgent头信息
     */
    public UserAgent getUserAgent() {
        String header = getLocalRequest().getHeader(USER_AGENT_STRING);
        return new UserAgent(header);
    }

    /**
     * 获取指定cookie值
     */
    public Cookie getCookie(String key) {
        Cookie[] cookies = getLocalRequest().getCookies();
        List<Cookie> cookieList = Arrays.stream(cookies)
                .filter(filterCookie -> Objects.equals(filterCookie.getName(), key))
                .limit(1).collect(Collectors.toList());
        return cookieList.isEmpty() ? null : cookieList.get(0);
    }

    /**
     * 获取指定cookie值，并执行接口进行后续处理
     */
    public <T> T getCookie(String key, Function<String, T> function) {
        Cookie cookie = getCookie(key);
        return cookie != null ? function.apply(cookie.getValue()) : null;
    }

    /**
     * 获取token
     */
    public String getToken() {
        return getLocalRequest().getHeader("Authorization");
    }

    /**
     * 获取指定字段的token
     */
    public String getToken(String header) {
        return getLocalRequest().getHeader(header);
    }

}
