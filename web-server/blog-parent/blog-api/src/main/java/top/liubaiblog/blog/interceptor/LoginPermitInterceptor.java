package top.liubaiblog.blog.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.liubaiblog.blog.constant.SecureConstants;
import top.liubaiblog.blog.exception.AuthorizationException;
import top.liubaiblog.blog.service.LoginService;
import top.liubaiblog.blog.threadlocal.UserThreadLocal;
import top.liubaiblog.blog.vo.LoginUserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 留白
 * @description 判断是否已经登录的拦截器
 */
@Slf4j
@Component
public class LoginPermitInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否为控制器方法，不为控制器方法直接放行
        if (!(handler instanceof HandlerMethod)) return true;
        // 获取token
        String token = request.getHeader(SecureConstants.REQUEST_SECURE_HEADER);
        // 打印日志
        log.info("===========<request start>===========");
        log.info("request uri -> {}", request.getRequestURI());
        log.info("request method -> {}", request.getMethod());
        log.info("request token -> {}", token);
        log.info("===========<request end>===========");
        if (!StringUtils.hasLength(token)) {
            throw new AuthorizationException("token不存在");
        }
        // 通过登录服务判断token是否合法
        LoginUserVo loginUserVo = loginService.checkTokenAndGetLoginUser(token);
        // 认证通过则放行
        UserThreadLocal.put(loginUserVo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
