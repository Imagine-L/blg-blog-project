package top.liubaiblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.liubaiblog.blog.annotation.WriteLog;
import top.liubaiblog.blog.exception.AccountExistException;
import top.liubaiblog.blog.exception.ParamIllegalException;
import top.liubaiblog.blog.service.LoginService;
import top.liubaiblog.blog.vo.ResponseCode;
import top.liubaiblog.blog.vo.param.LoginParam;
import top.liubaiblog.blog.vo.param.ResponseData;

/**
 * @author 留白
 * @description 注册相关控制器
 */
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private LoginService loginService;

    @WriteLog(module = "注册", operation = "前台新用户注册")
    @PostMapping
    public ResponseData register(@RequestBody LoginParam loginParam) {
        try {
            String token = loginService.registerAfterCreateToken(loginParam);
            return ResponseData.success(token);
        } catch (ParamIllegalException e) {
            return ResponseData.build(ResponseCode.PARAMS_ERROR, null);
        } catch (AccountExistException e) {
            return ResponseData.build(ResponseCode.ACCOUNT_EXIST, null);
        }
    }

}
