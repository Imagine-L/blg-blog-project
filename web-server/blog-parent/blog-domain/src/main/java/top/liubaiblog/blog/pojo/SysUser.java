package top.liubaiblog.blog.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

/**
 * @TableName ms_sys_user
 */
@TableName(value = "ms_sys_user")
@Data
public class SysUser implements Serializable {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 是否管理员
     */
    private Boolean admin;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 注册时间
     */
    private Long createDate;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 认证信息
     */
    private String info;

    /**
     * 地址
     */
    private String address;

    /**
     * 描述
     */
    private String description;

    /**
     * 最后登录时间
     */
    private Long lastLogin;

    /**
     * 手机号
     */
    private String mobilePhoneNumber;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * 状态
     */
    private String status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}