package top.liubaiblog.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName ms_tag
 */
@TableName(value ="ms_tag")
@Data
public class Tag implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 
     */
    private String avatar;

    /**
     * 
     */
    private String tagName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}