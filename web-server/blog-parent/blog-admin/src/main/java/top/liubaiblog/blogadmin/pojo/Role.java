package top.liubaiblog.blogadmin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 角色表
 * @TableName ms_role
 */
@TableName(value ="ms_role")
@Data
public class Role implements Serializable {
    /**
     * 主键id
     */
    @TableId
    private Long rid;

    /**
     * 角色标记
     */
    private String roleMark;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 描述
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}