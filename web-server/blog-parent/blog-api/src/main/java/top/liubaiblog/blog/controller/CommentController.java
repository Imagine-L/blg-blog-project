package top.liubaiblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import top.liubaiblog.blog.annotation.WriteLog;
import top.liubaiblog.blog.threadlocal.UserThreadLocal;
import top.liubaiblog.blog.vo.CommentVo;
import top.liubaiblog.blog.vo.LoginUserVo;
import top.liubaiblog.blog.vo.ResponseCode;
import top.liubaiblog.blog.vo.param.CommentParam;
import top.liubaiblog.blog.service.CommentService;
import top.liubaiblog.blog.vo.param.PageParams;
import top.liubaiblog.blog.vo.param.ResponseData;

/**
 * @author 留白
 * @description 评论控制器
 */
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("article/{id}")
    public ResponseData comments(@PathVariable("id") Long articleId) {
        return ResponseData.success(commentService.getByArticleId(articleId));
    }

    @PostMapping("/create/change")
    @WriteLog(module = "文章评论", operation = "添加文章评论")
    public ResponseData create(@RequestBody CommentParam commentParam) {
        CommentVo commentVo = commentService.saveByCommentParam(commentParam);
        if (commentVo != null) {
            return ResponseData.success(commentVo);
        } else {
            return ResponseData.build(ResponseCode.PARAMS_ERROR, null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseData delete(@RequestBody CommentParam commentParam) {
        // 判断删除评论消息是否为作者本人请求
        LoginUserVo loginUserVo = UserThreadLocal.get();
        if (commentService.checkAuthor(commentParam.getCommentId(), loginUserVo.getId())) {
            // 删除指定评论
            return ResponseData.success(commentService.removeByCommentParam(commentParam));
        } else {
            return ResponseData.build(ResponseCode.NO_PERMISSION, null);
        }
    }

    @PostMapping("/author")
    public ResponseData getByAuthor(@RequestBody PageParams pageParams) {
        LoginUserVo loginUserVo = UserThreadLocal.get();
        pageParams.setAuthorId(loginUserVo.getId());
        return ResponseData.success(commentService.getByAuthor(pageParams));
    }

}
