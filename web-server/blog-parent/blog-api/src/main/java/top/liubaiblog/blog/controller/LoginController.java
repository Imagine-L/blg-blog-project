package top.liubaiblog.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.liubaiblog.blog.annotation.WriteLog;
import top.liubaiblog.blog.exception.AuthenticationException;
import top.liubaiblog.blog.exception.ParamIllegalException;
import top.liubaiblog.blog.service.LoginService;
import top.liubaiblog.blog.vo.ResponseCode;
import top.liubaiblog.blog.vo.param.LoginParam;
import top.liubaiblog.blog.vo.param.ResponseData;

/**
 * @author 留白
 * @description 登录相关控制器
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @WriteLog(module = "登录", operation = "前台用户登录")
    @PostMapping
    public ResponseData login(@RequestBody LoginParam loginParam) {
        try {
            String token = loginService.loginAfterCreateToken(loginParam);
            return ResponseData.success(token);
        } catch (ParamIllegalException e) {
            return ResponseData.build(ResponseCode.PARAMS_ERROR, null);
        } catch (AuthenticationException e) {
            return ResponseData.build(ResponseCode.ACCOUNT_PWD_NOT_EXIST, null);
        }
    }

}
