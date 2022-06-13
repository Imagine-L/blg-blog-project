package top.liubaiblog.blog.vo;

import lombok.Data;

/**
 * @author 留白
 * @description 文章归档
 */
@Data
public class ArticleArchive {
    private String year;    // 文章年份
    private String month;   // 文章月份
    private String count;   // 该年月文章数量
}
