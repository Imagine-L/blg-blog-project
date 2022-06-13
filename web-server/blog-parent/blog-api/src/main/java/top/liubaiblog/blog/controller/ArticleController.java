package top.liubaiblog.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import top.liubaiblog.blog.annotation.WriteLog;
import top.liubaiblog.blog.exception.ParamIllegalException;
import top.liubaiblog.blog.service.ArticleService;
import top.liubaiblog.blog.threadlocal.UserThreadLocal;
import top.liubaiblog.blog.vo.ArticleVo;
import top.liubaiblog.blog.vo.LoginUserVo;
import top.liubaiblog.blog.vo.ResponseCode;
import top.liubaiblog.blog.vo.param.ArticleParam;
import top.liubaiblog.blog.vo.param.PageParams;
import top.liubaiblog.blog.vo.param.ResponseData;

import java.util.List;

/**
 * @author 留白
 * @description 关于文章的控制器
 */
@RestController
@RequestMapping("/articles")
@Slf4j
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 首页文章列表
     */
    @PostMapping
    public ResponseData articles(@RequestBody PageParams pageParams) {
        List<ArticleVo> list = articleService.listArticleVo(pageParams);
        return ResponseData.success(list);
    }

    /**
     * 获取某个作者的全部文章
     */
    @PostMapping("/author")
    public ResponseData getByAuthor(@RequestBody PageParams pageParams) {
        LoginUserVo loginUserVo = UserThreadLocal.get();
        pageParams.setAuthorId(loginUserVo.getId());
        return ResponseData.success(articleService.getByAuthor(pageParams));
    }

    /**
     * 最热文章
     */
    @PostMapping("/hot")
    public ResponseData hot(@RequestParam(required = false, defaultValue = "6") Integer limit) {
        List<ArticleVo> articles = articleService.hotArticles(limit);
        return ResponseData.success(articles);
    }

    /**
     * 最新文章
     */
    @PostMapping("/new")
    public ResponseData newArticles(@RequestParam(required = false, defaultValue = "6") Integer limit) {
        List<ArticleVo> articles = articleService.newArticles(limit);
        return ResponseData.success(articles);
    }

    /**
     * 文章归档
     */
    @PostMapping("/listArchives")
    public ResponseData listArchives() {
        return ResponseData.success(articleService.archive());
    }


    /**
     * 查看文章及其内容
     */
    @PostMapping(value = {"view/{id}", "/{id}"})
    public ResponseData view(@PathVariable("id") Long articleId) {
        try {
            return ResponseData.success(articleService.getByIdCarryBody(articleId));
        } catch (ParamIllegalException e) {
            return ResponseData.build(ResponseCode.PARAMS_ERROR, null);
        }
    }

    /**
     * 发布文章
     */
    @PostMapping("/publish")
    @WriteLog(module = "文章", operation = "创建/修改文章")
    public ResponseData publish(@RequestBody ArticleParam articleParam) {
        return ResponseData.success(articleService.saveOrUpdateByArticleParam(articleParam));
    }

    /**
     * 搜索文章
     */
    @PostMapping("/search")
    public ResponseData search(@RequestBody ArticleParam articleParam) {
        return ResponseData.success(articleService.searchByArticleParam(articleParam.getSearch()));
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/delete/{id}")
    @CacheEvict(cacheNames = {"listArticle", "getByAuthor", "articleBody"}, allEntries = true)
    @WriteLog(module = "文章", operation = "删除文章")
    public ResponseData delete(@PathVariable("id") Long articleId) {
        LoginUserVo loginUser = UserThreadLocal.get();
        // 用户只能删除自己的文章，先检查是否为本人的文章后删除
        if (articleService.checkAuthor(articleId, loginUser.getId())) {
            return ResponseData.success(articleService.removeById(articleId));
        } else {
            return ResponseData.build(ResponseCode.NO_PERMISSION, null);
        }
    }

}
