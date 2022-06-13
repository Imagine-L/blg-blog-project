package top.liubaiblog.blog.service;

import top.liubaiblog.blog.vo.LoginUserVo;
import top.liubaiblog.blog.vo.param.LoginParam;

/**
 * @author 留白
 * @description 登录相关服务类
 */
public interface LoginService {

    /**
     * 执行登录逻辑，并生成token返回
     */
    String loginAfterCreateToken(LoginParam loginParam);

    /**
     * 执行登出逻辑，并删除token
     */
    void logoutAfterRemoveToken(String token);

    /**
     * 执行注册逻辑，并生成token返回
     */
    String registerAfterCreateToken(LoginParam loginParam);

    /**
     * 检查token是否合法，如果合法则返回登录后的用户对象
     */
    LoginUserVo checkTokenAndGetLoginUser(String token);

    /**
     * 获取当前登录的用户
     */
    LoginUserVo getLoginUserByToken(String token);

}
