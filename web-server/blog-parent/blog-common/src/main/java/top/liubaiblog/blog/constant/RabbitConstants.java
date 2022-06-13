package top.liubaiblog.blog.constant;

/**
 * @author 留白
 * @description rabbit相关常量类
 */
public class RabbitConstants {

    /**
     * 日志相关常量
     */
    public final static String LOG_EXCHANGE = "log_exchange";
    public final static String LOG_QUEUE = "log_queue";
    public final static String LOG_EXCHANGE_BINDING_QUEUE = "log_exchange_binding_queue";

    /**
     * 文章相关常量
     */
    public final static String ARTICLE_EXCHANGE = "article_exchange";
    public final static String ARTICLE_QUEUE = "article_queue";
    public final static String ARTICLE_EXCHANGE_BINDING_QUEUE = "article_exchange_binding_queue";

}
