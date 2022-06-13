package top.liubaiblog.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.liubaiblog.blog.pojo.ArticleBody;
import top.liubaiblog.blog.vo.ArticleBodyVo;

/**
* @author 13326
* @description 针对表【ms_article_body】的数据库操作Service
* @createDate 2022-05-27 20:50:44
*/
public interface ArticleBodyService extends IService<ArticleBody> {

    /**
     * 根据文章id获取文章主体的vo对象
     */
    ArticleBodyVo getByArticleId(long articleId);

}
