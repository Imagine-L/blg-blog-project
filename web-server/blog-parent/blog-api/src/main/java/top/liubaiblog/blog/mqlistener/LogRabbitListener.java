package top.liubaiblog.blog.mqlistener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import top.liubaiblog.blog.constant.RabbitConstants;
import top.liubaiblog.blog.pojo.SysLog;
import top.liubaiblog.blog.service.SysLogService;

import java.io.IOException;

/**
 * @author 留白
 * @description 日志消息的处理
 */
@Slf4j
@Component
public class LogRabbitListener {

    @Autowired
    private SysLogService sysLogService;

    @RabbitListener(queues = RabbitConstants.LOG_QUEUE)
    public void logListener(SysLog sysLog) {
        try {
            // 向数据库持久化存储日志，如果失败则打印日志
            if (!sysLogService.save(sysLog)) {
                log.error("日志存储失败 -> {}", sysLog);
            }
        } catch (Exception e) {
            log.error("日志存储失败 -> {}", sysLog);
        }
    }

}
