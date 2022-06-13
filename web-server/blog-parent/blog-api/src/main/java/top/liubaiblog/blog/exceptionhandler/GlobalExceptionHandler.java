package top.liubaiblog.blog.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.liubaiblog.blog.exception.AuthorizationException;
import top.liubaiblog.blog.exception.ParamIllegalException;
import top.liubaiblog.blog.vo.ResponseCode;
import top.liubaiblog.blog.vo.param.ResponseData;

/**
 * @author 留白
 * @description 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局的异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseData exceptionHandle(Exception e) {
        e.printStackTrace();
        log.error("系统全局异常 -> {}", e.getMessage());
        return ResponseData.fail(-999, "系统繁忙，请稍后重试");
    }

    /**
     * 未授权抛出的异常
     */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseData authorizationExceptionHandler(AuthorizationException e) {
        return ResponseData.build(ResponseCode.NO_LOGIN, e.getMessage());
    }

    /**
     * 请求参数非法抛出的异常
     */
    @ExceptionHandler(ParamIllegalException.class)
    public ResponseData paramIllegalExceptionHandler(ParamIllegalException e) {
        return ResponseData.build(ResponseCode.PARAMS_ERROR, null);
    }

}
