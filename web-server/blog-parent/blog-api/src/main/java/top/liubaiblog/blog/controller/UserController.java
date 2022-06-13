package top.liubaiblog.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.liubaiblog.blog.annotation.WriteLog;
import top.liubaiblog.blog.constant.SecureConstants;
import top.liubaiblog.blog.exception.AuthorizationException;
import top.liubaiblog.blog.pojo.SysUser;
import top.liubaiblog.blog.service.LoginService;
import top.liubaiblog.blog.service.SysUserService;
import top.liubaiblog.blog.service.UploadService;
import top.liubaiblog.blog.threadlocal.UserThreadLocal;
import top.liubaiblog.blog.util.PojoUtils;
import top.liubaiblog.blog.vo.LoginUserVo;
import top.liubaiblog.blog.vo.ResponseCode;
import top.liubaiblog.blog.vo.param.PasswordParam;
import top.liubaiblog.blog.vo.param.ResponseData;
import top.liubaiblog.blog.vo.param.UserParam;

/**
 * @author 留白
 * @description 用户相关控制器
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private SysUserService userService;

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/currentUser")
    public ResponseData currentUser(@RequestHeader(SecureConstants.REQUEST_SECURE_HEADER) String token) {
        try {
            // 从token中获取用户信息
            LoginUserVo loginUserVo = loginService.getLoginUserByToken(token);
            // 判断token中的用户信息是否为最新的
            return ResponseData.success(loginUserVo);
        } catch (AuthorizationException e) {
            return ResponseData.build(ResponseCode.TOKEN_NOT_VALID, null);
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/pwd")
    @WriteLog(module = "用户操作", operation = "用户修改密码")
    public ResponseData changePwd(@RequestBody PasswordParam passwordParam) {
        if (userService.changePassword(passwordParam)) {
            return ResponseData.success(null);
        } else {
            return ResponseData.build(ResponseCode.PARAMS_ERROR, null);
        }
    }

    /**
     * 更新用户数据(携带头像)
     */
    @PostMapping("/update")
    @WriteLog(module = "用户操作", operation = "用户更新个人信息且更换头像")
    public ResponseData uploadCarryAvatar(UserParam userParam) {
        try {
            return ResponseData.success(userService.updateByParam(userParam));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.build(ResponseCode.UNKNOWN, null);
        }
    }

    /**
     * 更新用户数据(未携带头像)
     */
    @PutMapping("/update")
    @WriteLog(module = "用户操作", operation = "用户更新个人信息未更换头像")
    public ResponseData uploadData(@RequestBody UserParam userParam) {
        try {
            return ResponseData.success(userService.updateByParam(userParam));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.build(ResponseCode.UNKNOWN, null);
        }
    }

}
