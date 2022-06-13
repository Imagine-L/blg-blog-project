package top.liubaiblog.blog.service;

import top.liubaiblog.blog.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import top.liubaiblog.blog.vo.ArticleArchive;
import top.liubaiblog.blog.vo.ArticleVo;
import top.liubaiblog.blog.vo.PageArticleVo;
import top.liubaiblog.blog.vo.param.ArticleParam;
import top.liubaiblog.blog.vo.param.PageParams;

import java.util.List;

/**
 * @author 13326
 * @description 针对表【ms_article】的数据库操作Service
 * @createDate 2022-05-25 17:23:21
 */
public interface ArticleService extends IService<Article> {

    /**
     * 获取文章列表返回vo对象集合
     */
    List<ArticleVo> listArticleVo(PageParams pageParams);

    /**
     * 查询最热文章
     */
    List<ArticleVo> hotArticles(Integer limit);

    /**
     * 查询最新文章
     */
    List<ArticleVo> newArticles(Integer limit);

    /**
     * 文章归档
     */
    List<ArticleArchive> archive();

    /**
     * 获取文章以及文章内容
     */
    ArticleVo getByIdCarryBody(Long articleId);

    /**
     * 增加文章阅读量
     */
    void addViewCounts(Long articleId, long addCount);

    /**
     * 根据文章参数保存发布的文章
     *
     * @return ArticleVo中只有成功后的articleId参数
     */
    ArticleVo saveOrUpdateByArticleParam(ArticleParam articleParam);

    /**
     * 增加文章评论量
     *
     * @param articleId 文章id
     */
    void addCommentCounts(Long articleId, long addCount);

    /**
     * 根据文章id减少文章的评论
     */
    void subCommentCounts(Long articleId, long subCount);

    /**
     * 根据名字模糊查询文章
     */
    List<ArticleVo> searchByArticleParam(String title);

    /**
     * 判断文章是否为该作者的
     */
    boolean checkAuthor(Long articleId, Long authorId);

    /**
     * 获取所有的分类id
     */
    List<Long> getCategoryIdAll();


    /**
     * 获取某个作者的全部文章
     */
    PageArticleVo getByAuthor(PageParams pageParams);
}
