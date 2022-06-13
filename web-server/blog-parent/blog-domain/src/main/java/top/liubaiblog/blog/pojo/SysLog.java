package top.liubaiblog.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName ms_sys_log
 */
@TableName(value ="ms_sys_log")
@Data
public class SysLog implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long createDate;

    /**
     * 
     */
    private String ip;

    /**
     * 
     */
    private String method;

    /**
     * 
     */
    private String module;

    /**
     * 
     */
    private String nickname;

    /**
     * 
     */
    private String operation;

    /**
     * 
     */
    private String params;

    /**
     * 
     */
    private Long time;

    /**
     * 
     */
    private Long userid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}