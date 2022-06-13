package top.liubaiblog.blog.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 留白
 * @description 线程池相关配置
 */
@EnableAsync
@ConditionalOnBean(TaskExecutionProperties.class)
public class ThreadPoolConfig {

    @Bean("taskExecutor")
    public Executor asyncServiceExecutor(TaskExecutionProperties properties) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 配置核心线程池数量
        taskExecutor.setCorePoolSize(properties.getPool().getCoreSize());
        // 配置最大线程池数量
        taskExecutor.setMaxPoolSize(properties.getPool().getMaxSize());
        // 线程池所使用的缓冲队列
        taskExecutor.setQueueCapacity(properties.getPool().getQueueCapacity());
        // 等待时间（默认为0，此时立即停止），并没等待xx秒后强制停止
        taskExecutor.setAwaitTerminationSeconds(60);
        // 空闲线程存活时间
        taskExecutor.setKeepAliveSeconds((int) properties.getPool().getKeepAlive().getSeconds());
        // 等待任务在关机时完成--表明等待所有线程执行完
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池名称前缀
        taskExecutor.setThreadNamePrefix(properties.getThreadNamePrefix());
        // 线程池拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        // 线程池初始化
        taskExecutor.initialize();
        return taskExecutor;
    }

}
