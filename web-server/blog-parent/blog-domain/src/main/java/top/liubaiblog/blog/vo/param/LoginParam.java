package top.liubaiblog.blog.vo.param;

import lombok.Data;

/**
 * @author 留白
 * @description 登录相关参数
 */
@Data
public class LoginParam {
    private String account;
    private String password;
    private String nickname;
}
