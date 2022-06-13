package top.liubaiblog.blog.exception;

/**
 * @author 留白
 * @description 用户已存在异常
 */
public class AccountExistException extends RuntimeException {
    public AccountExistException() {
    }

    public AccountExistException(String message) {
        super(message);
    }
}
