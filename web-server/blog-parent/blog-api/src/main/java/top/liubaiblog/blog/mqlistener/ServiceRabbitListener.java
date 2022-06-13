package top.liubaiblog.blog.mqlistener;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.liubaiblog.blog.constant.RabbitConstants;
import top.liubaiblog.blog.message.ArticleMessage;
import top.liubaiblog.blog.pojo.Article;
import top.liubaiblog.blog.service.ArticleService;

/**
 * @author 留白
 * @description 业务mq消息处理
 */
@Component
@Slf4j
public class ServiceRabbitListener {

    @Autowired
    private ArticleService articleService;

    @RabbitListener(queues = RabbitConstants.ARTICLE_QUEUE)
    public void articleListener(ArticleMessage articleMessage) {
        if (articleMessage == null) {
            log.error("文章mq队列消息为空");
            return;
        }
        // 判断需要的操作并处理
        switch (articleMessage.getOperate()) {
            case ADD_VIEW_COUNTS:
                if (!addViewCount(articleMessage.getArticleId(), articleMessage.getOperNumber())) {
                    throw new RuntimeException(articleMessage.getArticleId() + " 文章增加阅读量失败");
                }
                break;
            case ADD_COMMENT_COUNTS:
                if (!addCommentCounts(articleMessage.getArticleId(), articleMessage.getOperNumber())) {
                    throw new RuntimeException(articleMessage.getArticleId() + " 文章增加评论量失败");
                }
                break;
            case SUB_COMMENT_COUNTS:
                if (!subCommentCounts(articleMessage.getArticleId(), articleMessage.getOperNumber())) {
                    throw new RuntimeException(articleMessage.getArticleId() + " 文章减少评论量失败");
                }
                break;
            default:
                log.error("【{}】文章队列消息未执行任何操作", articleMessage.getArticleId());
                break;
        }
    }

    /**
     * 增加文章阅读量
     */
    private boolean addViewCount(Long articleId, long addCount) {
        // 根据文章id获取到文章
        Article article = articleService.getById(articleId);
        // 更新条件的语句
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, article.getId());
        wrapper.eq(Article::getViewCounts, article.getViewCounts());    // 乐观锁
        wrapper.set(Article::getViewCounts, article.getViewCounts() + addCount);
        // 如果成功则直接跳出循环
        if (articleService.update(wrapper)) {
            log.info("【{}】文章增加(+{})阅读量成功", articleId, addCount);
            return true;
        }
        log.error("【{}】文章增加(+{})阅读量失败", articleId, addCount);
        return false;
    }

    /**
     * 增加文章评论量
     */
    public boolean addCommentCounts(Long articleId, long addCount) {
        // 根据文章id查询到文章评论量
        Article article = articleService.getById(articleId);
        // 将文章评论的数量+1
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, articleId);
        wrapper.eq(Article::getCommentCounts, article.getCommentCounts());  // 乐观锁
        wrapper.set(Article::getCommentCounts, article.getCommentCounts() + addCount);
        if (articleService.update(wrapper)) {
            log.info("【{}】文章增加(+{})评论量成功", articleId, addCount);
            return true;
        }
        log.error("【{}】文章增加(+{})评论量失败", articleId, addCount);
        return false;
    }

    /**
     * 减少文章评论量
     */
    public boolean subCommentCounts(Long articleId, long subCount) {
        // 根据文章id查询到文章评论量
        Article article = articleService.getById(articleId);
        // 将文章评论的数量-1
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, articleId);
        wrapper.eq(Article::getCommentCounts, article.getCommentCounts());  // 乐观锁
        wrapper.set(Article::getCommentCounts, article.getCommentCounts() - subCount);
        if (articleService.update(wrapper)) {
            log.info("【{}】文章减少(-{})评论量成功", articleId, subCount);
            return true;
        }
        log.error("【{}】文章减少(-{})评论量失败", articleId, subCount);
        return false;
    }


}
