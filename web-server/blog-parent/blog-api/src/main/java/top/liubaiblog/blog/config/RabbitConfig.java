package top.liubaiblog.blog.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 留白
 * @description rabbitmq的配置类
 */
@Configuration(proxyBeanMethods = false)
public class RabbitConfig {

    /**
     * 配置mq的消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
