package top.liubaiblog.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.liubaiblog.blog.interceptor.CheckFileInterceptor;
import top.liubaiblog.blog.interceptor.LoginPermitInterceptor;

/**
 * @author 留白 writen
 * @description
 */
@Configuration
public class BlogWebMvcConfig implements WebMvcConfigurer {

    @Autowired      // 登录许可拦截器
    private LoginPermitInterceptor loginPermitInterceptor;

    /**
     * 解决跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")          // 配置可以被跨域的路径，可以任意配置
                .allowedOriginPatterns("*")                    // 允许所有的请求域名访问我们的跨域资源
                .allowCredentials(true)                 // 响应头表示是否可以将请求的响应暴露给页面
                .allowedMethods("GET", "POST", "DELETE", "PUT")     // 允许的请求方式
                .maxAge(4600);                                      // 配置客户端缓存预检请求的响应时间（单位为秒）
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginPermitInterceptor)
                .addPathPatterns("/test",
                        "/comments/create/change",
                        "/articles/publish",
                        "/articles/delete/*",
                        "/articles/author",
                        "/categorys/save",
                        "/categorys/empty",
                        "/tags/save",
                        "/tags/empty",
                        "/comments/delete/*",
                        "/comments/author",
                        "/upload",
                        "/users/pwd",
                        "/users/update");
    }
}
