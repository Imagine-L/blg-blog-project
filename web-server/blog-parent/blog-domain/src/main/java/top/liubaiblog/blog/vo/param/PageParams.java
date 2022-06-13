package top.liubaiblog.blog.vo.param;

import lombok.Data;

/**
 * @author 留白
 * @description
 */
@Data
public class PageParams {
    private int page;           // 当前页码
    private int pageSize;       // 当前页显示的数据条数
    private Long categoryId;    // 文章分类id
    private Long authorId;      // 作者Id
    private Long tagId;         // 标签id
    private String year;        // 年份
    private String month;       // 月份
    private String sortName;    // 排序方式
}
