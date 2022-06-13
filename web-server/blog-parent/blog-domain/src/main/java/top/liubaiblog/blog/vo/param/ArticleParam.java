package top.liubaiblog.blog.vo.param;

import lombok.Data;
import top.liubaiblog.blog.vo.CategoryVo;
import top.liubaiblog.blog.vo.TagVo;

import java.util.List;

/**
 * @author 留白
 * @description 文章的传参对象
 */
@Data
public class ArticleParam {
    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;

    private String search;
}
