package top.liubaiblog.blog.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.liubaiblog.blog.constant.RabbitConstants;

/**
 * @author 留白
 * @description 日志的mq配置类
 */
@Configuration
public class LogRabbitConfig {

    /**
     * 日志交换机
     */
    @Bean
    public DirectExchange logExchange() {
        return new DirectExchange(RabbitConstants.LOG_EXCHANGE, true, false);
    }

    /**
     * 日志队列
     */
    @Bean
    public Queue logQueue() {
        return QueueBuilder
                .durable(RabbitConstants.LOG_QUEUE)
                .build();
    }

    /**
     * 绑定日志交换机和队列
     */
    @Bean
    public Binding logExchangeBindingQueue() {
        return BindingBuilder.bind(logQueue())
                .to(logExchange())
                .with(RabbitConstants.LOG_EXCHANGE_BINDING_QUEUE);
    }

}
