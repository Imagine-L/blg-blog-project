package top.liubaiblog.blogadmin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName ms_permission
 */
@TableName(value ="ms_permission")
@Data
public class Permission implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String path;

    /**
     * 
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}