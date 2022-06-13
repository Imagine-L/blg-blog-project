package top.liubaiblog.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author 留白
 * @description 文章pojo表示层展示数据
 */
@Data
public class ArticleVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;

    /**
     * 创建时间
     */
    private String createDate;

    private UserVo author;

    private String authorAvatar;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;

}
