package top.liubaiblog.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author 留白
 * @description 评论视图层对象
 */
@Data
public class CommentVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private UserVo author;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;

    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}