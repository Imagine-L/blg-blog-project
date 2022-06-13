package top.liubaiblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.liubaiblog.blog.constant.RabbitConstants;
import top.liubaiblog.blog.exception.AuthorizationException;
import top.liubaiblog.blog.exception.ParamIllegalException;
import top.liubaiblog.blog.message.ArticleMessage;
import top.liubaiblog.blog.message.seviceenum.ArticleOperateEnum;
import top.liubaiblog.blog.pojo.Article;
import top.liubaiblog.blog.pojo.ArticleBody;
import top.liubaiblog.blog.pojo.ArticleTag;
import top.liubaiblog.blog.service.ArticleBodyService;
import top.liubaiblog.blog.service.ArticleService;
import top.liubaiblog.blog.mapper.ArticleMapper;
import org.springframework.stereotype.Service;
import top.liubaiblog.blog.service.ArticleTagService;
import top.liubaiblog.blog.threadlocal.UserThreadLocal;
import top.liubaiblog.blog.util.PojoUtils;
import top.liubaiblog.blog.vo.*;
import top.liubaiblog.blog.vo.param.ArticleParam;
import top.liubaiblog.blog.vo.param.PageParams;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author 13326
 * @description 针对表【ms_article】的数据库操作Service实现
 * @createDate 2022-05-25 17:23:21
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Autowired
    @SuppressWarnings("all")
    private Executor taskExecutor;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleBodyService articleBodyService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    @Cacheable(cacheNames = "listArticle", keyGenerator = "cacheKeyGenerator", unless = "#result == null")
    public List<ArticleVo> listArticleVo(PageParams pageParams) {
        Page<Article> articlePage = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        getBaseMapper().selectByParams(articlePage,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articlePage.getRecords();
        return PojoUtils.article.copyList(records, true, true);
    }

    @Override
    @Cacheable(cacheNames = "hotArticles", keyGenerator = "cacheKeyGenerator", unless = "#result == null")
    public List<ArticleVo> hotArticles(Integer limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        // 根据浏览量降序(高->低)
        wrapper.orderByDesc(Article::getViewCounts);
        wrapper.select(Article::getId, Article::getTitle);
        wrapper.last("limit " + limit);
        List<Article> list = this.list(wrapper);
        // 将文章对象列表转换为vo对象
        return PojoUtils.article.copyList(list, false, false);
    }

    @Override
    public List<ArticleVo> newArticles(Integer limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        // 根据创作时间降序(近->远)
        wrapper.orderByDesc(Article::getCreateDate);
        wrapper.select(Article::getId, Article::getTitle);
        wrapper.last("limit " + limit);
        List<Article> list = this.list(wrapper);
        // 将文章对象列表转换为vo对象
        return PojoUtils.article.copyList(list, false, false);
    }

    @Override
    public List<ArticleArchive> archive() {
        return getBaseMapper().archive();
    }

    @Override
    @Cacheable(cacheNames = "articleBody", keyGenerator = "cacheKeyGenerator", unless = "#result == null")
    public ArticleVo getByIdCarryBody(Long articleId) {
        if (articleId == null) throw new ParamIllegalException("文章id为空");
        Article article = getById(articleId);
        if (article == null) return null;
        taskExecutor.execute(() -> addViewCounts(article.getId(), 1));
        return PojoUtils.article.copy(article, true, true, true, true);
    }


    @Override
    public void addViewCounts(Long articleId, long addCount) {
        // 向mq发送消息表示要增加评论量
        ArticleMessage articleMessage = new ArticleMessage();
        articleMessage.setArticleId(articleId);
        articleMessage.setOperate(ArticleOperateEnum.ADD_VIEW_COUNTS);
        articleMessage.setOperNumber(1);
        rabbitTemplate.convertAndSend(RabbitConstants.ARTICLE_EXCHANGE,
                RabbitConstants.ARTICLE_EXCHANGE_BINDING_QUEUE,
                articleMessage);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"listArticle", "articleBody", "getByAuthor"}, allEntries = true)
    public ArticleVo saveOrUpdateByArticleParam(ArticleParam articleParam) {
        if (articleParam == null) throw new ParamIllegalException("文章为空");
        return articleParam.getId() == null ? saveByArticleParam(articleParam) :
                updateByArticleParam(articleParam);
    }

    @Override
    @Async("taskExecutor")
    public void addCommentCounts(Long articleId, long addCount) {
        // 向mq发送消息表示要增加评论量
        ArticleMessage articleMessage = new ArticleMessage();
        articleMessage.setArticleId(articleId);
        articleMessage.setOperate(ArticleOperateEnum.ADD_COMMENT_COUNTS);
        articleMessage.setOperNumber(1);
        rabbitTemplate.convertAndSend(RabbitConstants.ARTICLE_EXCHANGE,
                RabbitConstants.ARTICLE_EXCHANGE_BINDING_QUEUE,
                articleMessage);
    }

    @Override
    @Async("taskExecutor")
    public void subCommentCounts(Long articleId, long subCount) {
        // 向mq发送消息表示要减少评论量
        ArticleMessage articleMessage = new ArticleMessage();
        articleMessage.setArticleId(articleId);
        articleMessage.setOperate(ArticleOperateEnum.SUB_COMMENT_COUNTS);
        articleMessage.setOperNumber(subCount);
        rabbitTemplate.convertAndSend(RabbitConstants.ARTICLE_EXCHANGE,
                RabbitConstants.ARTICLE_EXCHANGE_BINDING_QUEUE,
                articleMessage);
    }

    @Override
    public List<ArticleVo> searchByArticleParam(String title) {
        List<Article> articles = null;
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Article::getId, Article::getTitle);
        wrapper.orderByDesc(Article::getViewCounts);
        // 判断搜索内容是否为空
        if (StringUtils.hasLength(title)) {
            wrapper.like(Article::getTitle, title);
        }
        articles = this.list(wrapper);
        return PojoUtils.article.copyList(articles, false, false);
    }

    @Override
    public boolean checkAuthor(Long articleId, Long authorId) {
        Article article = getById(articleId);
        if (article.getAuthorId() == null) return false;
        return article.getAuthorId().equals(authorId);
    }

    @Override
    public List<Long> getCategoryIdAll() {
        return getBaseMapper().selectCategoryIdAll();
    }

    @Override
    @Cacheable(cacheNames = "getByAuthor", keyGenerator = "cacheKeyGenerator", unless = "#result == null")
    public PageArticleVo getByAuthor(PageParams pageParams) {
        // 分页数据
        Page<Article> articlePage = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        // 获取数据
        getBaseMapper().selectByAuthor(articlePage, pageParams.getAuthorId(), pageParams.getSortName());
        // 封装数据
        PageArticleVo pageArticleVo = new PageArticleVo();
        List<ArticleVo> articles = PojoUtils.article.copyList(articlePage.getRecords(), true, true);
        pageArticleVo.setArticles(articles);
        pageArticleVo.setPageSize(articlePage.getSize());
        pageArticleVo.setTotal(articlePage.getTotal());
        return pageArticleVo;
    }

    /**
     * 保存文章
     */
    private ArticleVo saveByArticleParam(ArticleParam articleParam) {
        // 获取作者
        LoginUserVo user = UserThreadLocal.get();

        // 生成article，获取到新的id
        Article article = new Article();
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(0);
        article.setAuthorId(user.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        this.save(article);

        // 插入body对象
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyService.save(articleBody);

        // 对article和tag进行映射，并将映射结果存入数据库
        List<TagVo> tags = articleParam.getTags();
        List<ArticleTag> articleTags = tags.stream().map(tagVo -> {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setTagId(tagVo.getId());
            articleTag.setArticleId(article.getId());
            return articleTag;
        }).collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

        // 更新article的数据
        article.setBodyId(articleBody.getId());
        this.updateById(article);

        // 返回有 articleVo 对象
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return articleVo;
    }

    /**
     * 修改文章
     */
    private ArticleVo updateByArticleParam(ArticleParam articleParam) {
        // 判断当前请求的身份
        LoginUserVo loginUser = UserThreadLocal.get();
        if (!checkAuthor(articleParam.getId(), loginUser.getId())) {
            log.warn("用户【{}】试图修改他人文章【{}】", loginUser.getId(), articleParam.getId());
            throw new AuthorizationException("非法修改文章");
        }
        // 更新tag和文章的映射
        // 1. 删除原有的映射
        LambdaQueryWrapper<ArticleTag> articleTagWrapper = new LambdaQueryWrapper<>();
        articleTagWrapper.eq(ArticleTag::getArticleId, articleParam.getId());
        articleTagService.remove(articleTagWrapper);
        // 2. 创建新的映射
        List<TagVo> newTags = articleParam.getTags();
        List<ArticleTag> articleTags = newTags.stream().map(tagVo -> {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setTagId(tagVo.getId());
            articleTag.setArticleId(articleParam.getId());
            return articleTag;
        }).collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

        // 更新body内容
        // 1. 删除原有的内容
        LambdaQueryWrapper<ArticleBody> bodyWrapper = new LambdaQueryWrapper<>();
        bodyWrapper.eq(ArticleBody::getArticleId, articleParam.getId());
        articleBodyService.remove(bodyWrapper);
        // 2. 换上新的内容
        ArticleBody newArticleBody = new ArticleBody();
        newArticleBody.setArticleId(articleParam.getId());
        newArticleBody.setContent(articleParam.getBody().getContent());
        newArticleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyService.save(newArticleBody);

        // 更新文章内容
        Article article = new Article();
        article.setId(articleParam.getId());
        article.setTitle(articleParam.getTitle());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setSummary(articleParam.getSummary());
        article.setBodyId(newArticleBody.getId());
        this.updateById(article);


        // 返回有 articleVo 对象
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return articleVo;
    }

}




