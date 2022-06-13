package top.liubaiblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.liubaiblog.blog.exception.ParamIllegalException;
import top.liubaiblog.blog.mapper.CommentMapper;
import top.liubaiblog.blog.pojo.Comment;
import top.liubaiblog.blog.service.ArticleService;
import top.liubaiblog.blog.service.CommentService;
import top.liubaiblog.blog.threadlocal.UserThreadLocal;
import top.liubaiblog.blog.util.PojoUtils;
import top.liubaiblog.blog.vo.CommentVo;
import top.liubaiblog.blog.vo.LoginUserVo;
import top.liubaiblog.blog.vo.PageCommentVo;
import top.liubaiblog.blog.vo.param.CommentParam;
import top.liubaiblog.blog.vo.param.PageParams;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 13326
 * @description 针对表【ms_comment】的数据库操作Service实现
 * @createDate 2022-05-28 22:01:18
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    @Autowired
    private ArticleService articleService;

    @Override
    @Cacheable(cacheNames = "comment", keyGenerator = "cacheKeyGenerator", unless = "#result == null")
    public List<CommentVo> getByArticleId(Long articleId) {
        if (articleId == null) throw new ParamIllegalException("文章id为空");
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId);
        wrapper.eq(Comment::getLevel, 1);
        List<Comment> comments = list(wrapper);
        return PojoUtils.comment.copyList(comments);
    }

    @Override
    public List<CommentVo> getChildren(Long parentId) {
        if (parentId == null) return Collections.emptyList();
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getParentId, parentId);
        List<Comment> children = list(wrapper);
        return PojoUtils.comment.copyList(children);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"comment", "articleBody"}, allEntries = true)
    public CommentVo saveByCommentParam(CommentParam commentParam) {
        // 表单验证
        if (!StringUtils.hasLength(commentParam.getContent()) ||
                commentParam.getArticleId() == null) {
            return null;
        }
        // 构造实体类对象
        Comment comment = new Comment();
        LoginUserVo user = UserThreadLocal.get();
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(user.getId());
        Long parent = commentParam.getParent();
        comment.setParentId(parent == null ? 0 : parent);
        comment.setToUid(commentParam.getToUserId() == null ? 0 : commentParam.getToUserId());
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        // 保存评论
        if (this.save(comment)) {
            // 增加文章评论量
            articleService.addCommentCounts(commentParam.getArticleId(), 1);
        }
        // 返回结果
        return PojoUtils.comment.copy(comment);
    }

    @Override
    public boolean checkAuthor(Long commentId, Long authorId) {
        Comment comment = getById(commentId);
        if (comment == null) return false;
        return comment.getAuthorId().equals(authorId);
    }

    @Override
    @CacheEvict(cacheNames = {"comment", "articleBody"}, allEntries = true)
    public boolean removeByCommentParam(CommentParam commentParam) {
        if (commentParam == null) throw new ParamIllegalException("参数错误");
        // 删除指定评论
        boolean result = removeById(commentParam.getCommentId());
        if (result) {
            // 查看子评论的数量，但不用删除
            LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Comment::getParentId, commentParam.getCommentId());
            long count = count(wrapper);
            // 减少评论量(删除的一条评论量，以及其子评论数量，子评论虽然未删除，但是不再可见)
            articleService.subCommentCounts(commentParam.getArticleId(), 1 + count);
        }
        return result;
    }

    @Override
    @Cacheable(cacheNames = "comment", keyGenerator = "cacheKeyGenerator", unless = "#result == null")
    public PageCommentVo getByAuthor(PageParams pageParams) {
        // 查看包含此作者的所有评论
        Page<Comment> commentPage = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        getBaseMapper().selectByAuthorId(commentPage, pageParams.getAuthorId());
        List<Comment> oldComments = commentPage.getRecords().stream().peek(comment -> {
            if (comment.getDeleted() == 1) comment.setContent("[原评论已删除]");
        }).collect(Collectors.toList());
        List<CommentVo> comments = PojoUtils.comment.copyList(oldComments);
        // 封装对象
        PageCommentVo pageCommentVo = new PageCommentVo();
        pageCommentVo.setTotal(commentPage.getTotal());
        pageCommentVo.setPageSize(commentPage.getSize());
        pageCommentVo.setComments(comments);
        return pageCommentVo;
    }
}




