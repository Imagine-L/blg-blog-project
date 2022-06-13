package top.liubaiblog.blog.exception;

/**
 * @author 留白
 * @description 参数不合法抛出异常
 */
public class ParamIllegalException extends RuntimeException {

    public ParamIllegalException() {
    }

    public ParamIllegalException(String message) {
        super(message);
    }
}
