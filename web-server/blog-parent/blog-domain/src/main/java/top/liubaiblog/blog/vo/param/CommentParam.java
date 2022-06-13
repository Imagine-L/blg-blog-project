package top.liubaiblog.blog.vo.param;

import lombok.Data;

/**
 * @author 留白
 * @description 评论的传参对象
 */
@Data
public class CommentParam {

    private Long commentId;     // 评论id，一般为空，如果不为空表示删除

    private Long articleId;     // 评论所在的文章id

    private String content;     // 评论内容

    private Long parent;        // 评论的父评论id

    private Long toUserId;      // 评论是发给哪个用户的用户id
}
