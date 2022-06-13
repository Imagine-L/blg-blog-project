package top.liubaiblog.blog.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 留白
 * @description 分页评论视图对象
 */
@Data
public class PageCommentVo {
    private Long total;              // 总共的记录数量
    private Long pageSize;           // 获取记录的数量
    private List<CommentVo> comments;   // 获取到的记录
}
