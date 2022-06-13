package top.liubaiblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.liubaiblog.blog.mapper.ArticleBodyMapper;
import top.liubaiblog.blog.pojo.ArticleBody;
import top.liubaiblog.blog.service.ArticleBodyService;
import top.liubaiblog.blog.util.PojoUtils;
import top.liubaiblog.blog.vo.ArticleBodyVo;

/**
 * @author 13326
 * @description 针对表【ms_article_body】的数据库操作Service实现
 * @createDate 2022-05-27 20:50:44
 */
@Service
public class ArticleBodyServiceImpl extends ServiceImpl<ArticleBodyMapper, ArticleBody>
        implements ArticleBodyService {

    @Override
    public ArticleBodyVo getByArticleId(long articleId) {
        LambdaQueryWrapper<ArticleBody> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleBody::getArticleId, articleId);
        wrapper.last("limit 1");
        ArticleBody articleBody = getOne(wrapper);
        return PojoUtils.articleBody.copy(articleBody);
    }

}




