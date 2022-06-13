package top.liubaiblog.blog.exception;

/**
 * @author 留白
 * @description 授权异常
 */
public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
