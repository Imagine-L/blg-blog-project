package top.liubaiblog.blog.vo.param;

import lombok.Data;

/**
 * @author 留白
 * @description 密码参数
 */
@Data
public class PasswordParam {

    private String oldPwd;  // 原密码
    private String newPwd;  // 新密码

}
