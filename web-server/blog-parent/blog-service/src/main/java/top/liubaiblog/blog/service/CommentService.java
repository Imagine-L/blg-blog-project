package top.liubaiblog.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.liubaiblog.blog.pojo.Comment;
import top.liubaiblog.blog.vo.CommentVo;
import top.liubaiblog.blog.vo.PageCommentVo;
import top.liubaiblog.blog.vo.param.CommentParam;
import top.liubaiblog.blog.vo.param.PageParams;

import java.util.List;

/**
* @author 13326
* @description 针对表【ms_comment】的数据库操作Service
* @createDate 2022-05-28 22:01:18
*/
public interface CommentService extends IService<Comment> {

    /**
     * 获取某篇文章的评论列表
     */
    List<CommentVo> getByArticleId(Long articleId);

    /**
     * 获取某条评论的所有子评论
     * @param parentId 某条父评论的id
     */
    List<CommentVo> getChildren(Long parentId);

    /**
     * 增加一条评论
     */
    CommentVo saveByCommentParam(CommentParam commentParam);

    /**
     * 判断某条评论是否为该作者的
     */
    boolean checkAuthor(Long commentId, Long authorId);

    /**
     * 根据参数删除评论
     */
    boolean removeByCommentParam(CommentParam commentParam);

    /**
     * 获取某个作者的所有评论
     */
    PageCommentVo getByAuthor(PageParams pageParams);
}
