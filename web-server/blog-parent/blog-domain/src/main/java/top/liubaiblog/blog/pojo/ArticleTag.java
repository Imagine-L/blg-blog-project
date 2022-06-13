package top.liubaiblog.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName ms_article_tag
 */
@TableName(value ="ms_article_tag")
@Data
public class ArticleTag implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long articleId;

    /**
     * 
     */
    private Long tagId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}