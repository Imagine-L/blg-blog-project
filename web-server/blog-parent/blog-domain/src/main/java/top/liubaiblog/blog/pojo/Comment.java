package top.liubaiblog.blog.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.Data;

/**
 * @TableName ms_comment
 */
@TableName(value = "ms_comment")
@Data
public class Comment implements Serializable {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     *
     */
    private String content;

    /**
     *
     */
    private Long createDate;

    /**
     *
     */
    private Long articleId;

    /**
     *
     */
    private Long authorId;

    /**
     *
     */
    private Long parentId;

    /**
     *
     */
    private Long toUid;

    /**
     *
     */
    private Integer level;

    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}