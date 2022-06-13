package top.liubaiblog.blog.vo;

import lombok.*;

/**
 * @author 留白
 * @description
 */
@AllArgsConstructor
@Getter
@ToString
public enum ResponseCode {

    SUCCESS(200, "success"),
    PARAMS_ERROR(10001, "参数有误"),
    ACCOUNT_PWD_NOT_EXIST(10002, "用户名或密码不存在"),
    ACCOUNT_EXIST(10003, "用户名已存在"),
    TOKEN_NOT_VALID(10002, "token不合法"),
    FILE_SUFFIX_ILLEGAL(10001, "文件后缀不合法"),
    NO_PERMISSION(70001, "无访问权限"),
    SESSION_TIME_OUT(90001, "会话超时"),
    UNKNOWN(500, "fail"),
    NO_LOGIN(90002, "未登录");

    private int code;
    private String msg;
}
