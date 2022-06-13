package top.liubaiblog.blog.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.liubaiblog.blog.constant.RabbitConstants;

/**
 * @author 留白
 * @description 为服务提供mq的配置类
 */
@Configuration
public class ServiceRabbitConfig {

    /**
     * 文章交换机
     *
     * @return
     */
    @Bean
    public DirectExchange articleExchange() {
        return new DirectExchange(RabbitConstants.ARTICLE_EXCHANGE, true, false);
    }

    /**
     * 文章队列：这个队列的消息主要是要操作文章阅读量、评论量的增减
     */
    @Bean
    public Queue articleQueue() {
        return QueueBuilder
                .durable(RabbitConstants.ARTICLE_QUEUE)
                .build();
    }

    /**
     * 文章交换机和队列绑定
     */
    @Bean
    public Binding article_exchange_binding_queue() {
        return BindingBuilder.bind(articleQueue())
                .to(articleExchange())
                .with(RabbitConstants.ARTICLE_EXCHANGE_BINDING_QUEUE);
    }
}
