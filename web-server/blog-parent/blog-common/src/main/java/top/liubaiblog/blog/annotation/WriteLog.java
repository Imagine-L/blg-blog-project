package top.liubaiblog.blog.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author 留白
 * @description 标识日志写入注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WriteLog {

    // 模块
    String module() default "";

    // 操作
    @AliasFor("value")
    String operation() default "";

    @AliasFor("operation")
    String value() default "";
}
