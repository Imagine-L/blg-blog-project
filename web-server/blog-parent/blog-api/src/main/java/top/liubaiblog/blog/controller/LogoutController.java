package top.liubaiblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.liubaiblog.blog.annotation.WriteLog;
import top.liubaiblog.blog.constant.SecureConstants;
import top.liubaiblog.blog.service.LoginService;
import top.liubaiblog.blog.vo.param.ResponseData;

/**
 * @author 留白
 * @description 登出控制器
 */
@RestController
@RequestMapping("/logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    @WriteLog(module = "登出", operation = "前台用户登出")
    @GetMapping
    public ResponseData logout(@RequestHeader(SecureConstants.REQUEST_SECURE_HEADER) String token) {
        loginService.logoutAfterRemoveToken(token);
        return ResponseData.success(null);
    }

}
