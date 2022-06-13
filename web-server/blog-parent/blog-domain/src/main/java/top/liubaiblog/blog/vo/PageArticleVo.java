package top.liubaiblog.blog.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 留白
 * @description 分页文章视图对象
 */
@Data
public class PageArticleVo {
    private Long total;              // 总共的记录数量
    private Long pageSize;           // 获取记录的数量
    private List<ArticleVo> articles;   // 获取到的记录
}
