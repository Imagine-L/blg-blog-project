package top.liubaiblog.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.liubaiblog.blog.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.liubaiblog.blog.vo.ArticleArchive;

import java.util.List;

/**
* @author 13326
* @description 针对表【ms_article】的数据库操作Mapper
* @createDate 2022-05-25 17:23:21
* @Entity top.liubaiblog.blog.pojo.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 文章归档
     */
    List<ArticleArchive> archive();

    /**
     * 查询文章列表
     */
    Page<Article> selectByParams(Page<Article> page,
                                 @Param("categoryId") Long categoryId,
                                 @Param("tagId") Long tagId,
                                 @Param("year") String year,
                                 @Param("month") String month);

    /**
     * 获取不重复的所有分类id
     */
    List<Long> selectCategoryIdAll();

    /**
     * 获取某个作者的文章
     */
    Page<Article> selectByAuthor(Page<Article> page, @Param("authorId") Long authorId, @Param("sortName") String sortName);
}




