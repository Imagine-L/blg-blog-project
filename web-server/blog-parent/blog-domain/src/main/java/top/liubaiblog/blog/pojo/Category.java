package top.liubaiblog.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName ms_category
 */
@TableName(value ="ms_category")
@Data
public class Category implements Serializable {
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
    private String categoryName;

    /**
     * 
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}