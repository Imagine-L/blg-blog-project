package top.liubaiblog.blog.message.seviceenum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author 留白
 * @description 文章操作
 */
@AllArgsConstructor
@Getter
@ToString
public enum ArticleOperateEnum {
    ADD_VIEW_COUNTS(1, "添加文章阅读量"),
    ADD_COMMENT_COUNTS(2, "增加文章评论量"),
    SUB_COMMENT_COUNTS(3, "减少文章阅读量");

    private int operCode;
    private String operation;
}
