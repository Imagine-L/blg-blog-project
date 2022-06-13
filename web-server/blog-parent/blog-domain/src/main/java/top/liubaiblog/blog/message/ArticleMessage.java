package top.liubaiblog.blog.message;

import lombok.Data;
import top.liubaiblog.blog.message.seviceenum.ArticleOperateEnum;

/**
 * @author 留白
 * @description 发送给消息队列的消息
 */
@Data
public class ArticleMessage {
    private Long articleId;             // 文章id
    private ArticleOperateEnum operate; // 执行的操作
    private long operNumber;            // 操作增加/减少的数量

    public long getOperNumber() {
        // 操作量不能小于0
        return operNumber <= 0 ? 1 : operNumber;
    }
}
