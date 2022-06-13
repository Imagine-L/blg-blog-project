package top.liubaiblog.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author 留白
 * @description 登录用户对象
 */
@Data
public class LoginUserVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String account;     // 账号
    private String nickname;    // 昵称
    private String avatar;      // 头像
    private String info;        // 认证信息
    private String address;     // 地址
    private String email;       // 邮箱
    private String description; // 描述
    private String remoteIp;    // ip地址
    private String system;      // 系统
    private String browser;     // 浏览器
}
