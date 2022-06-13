package top.liubaiblog.blog.exception;

/**
 * @author 留白
 * @description 认证异常
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
