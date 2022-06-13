package top.liubaiblog.blog.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.liubaiblog.blog.annotation.WriteLog;
import top.liubaiblog.blog.constant.RabbitConstants;
import top.liubaiblog.blog.pojo.SysLog;
import top.liubaiblog.blog.threadlocal.UserThreadLocal;
import top.liubaiblog.blog.util.HttpRequestUtils;
import top.liubaiblog.blog.vo.LoginUserVo;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * @author 留白
 * @description
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private HttpRequestUtils httpRequestUtils;

    @Autowired
    private Executor taskExecutor;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 表示对标记@WriteLog的方法进行增强
    @Pointcut("@annotation(top.liubaiblog.blog.annotation.WriteLog)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 记录开始时间
        long start = System.currentTimeMillis();
        // 执行目标方法
        Object result = joinPoint.proceed();
        // 记录方法执行时间
        long runtime = System.currentTimeMillis() - start;
        // 异步保存日志
        LoginUserVo operateUser = UserThreadLocal.get();
        HttpServletRequest localRequest = httpRequestUtils.getLocalRequest();
        taskExecutor.execute(() -> recordLog(joinPoint, runtime, localRequest, operateUser));
        return result;
    }

    /**
     * 记录日志
     *
     * @param joinPoint 方法切入点
     * @param runtime   方法执行时间
     */
    private void recordLog(ProceedingJoinPoint joinPoint,
                           long runtime,
                           HttpServletRequest localRequest,
                           LoginUserVo operateUser) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取到该方法
        Method method = signature.getMethod();
        // 获取到该方法的@Write注解
        WriteLog writeLog = method.getAnnotation(WriteLog.class);
        // 记录日志
        log.info("================= <log start> =================");
        log.info("module -> {}", writeLog.module());
        log.info("operation -> {}", writeLog.operation());
        // 请求的完整方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method -> {}", className + "." + methodName + "()");
        // 请求的参数
        String jsonArgs = null;
        try {
            Object[] args = joinPoint.getArgs();
            ObjectMapper objectMapper = new ObjectMapper();
            jsonArgs = objectMapper.writeValueAsString(args);
            if (jsonArgs.length() > 250) jsonArgs = jsonArgs.substring(0, 250) + "...";
            log.info("method param -> {}", jsonArgs);
        } catch (JsonProcessingException e) {
            log.info("method param -> 参数解析错误");
        }
        // 获取请求IP地址
        String ip = localRequest.getRemoteAddr();
        log.info("request uri -> {} | {}", localRequest.getRequestURI(), localRequest.getMethod());
        log.info("remote ip -> {}", ip);
        // 请求执行时间
        log.info("execute time -> {} ms", runtime);
        log.info("================= <log end> =================");
        // 向数据库存储日志
        SysLog sysLog = new SysLog();
        sysLog.setCreateDate(System.currentTimeMillis());
        sysLog.setMethod(methodName);
        sysLog.setModule(writeLog.module());
        if (operateUser != null) {
            sysLog.setIp(operateUser.getRemoteIp());
            sysLog.setNickname(operateUser.getNickname());
            sysLog.setUserid(operateUser.getId());
        } else {
            sysLog.setIp(ip);
            sysLog.setNickname("游客");
            sysLog.setUserid(-1L);
        }
        sysLog.setOperation(writeLog.operation());
        sysLog.setParams(jsonArgs);
        sysLog.setTime(runtime);
        // 发送给mq处理
        rabbitTemplate.convertAndSend(RabbitConstants.LOG_EXCHANGE,
                RabbitConstants.LOG_EXCHANGE_BINDING_QUEUE,
                sysLog);
    }
}
