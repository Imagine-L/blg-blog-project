package top.liubaiblog.blog.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.liubaiblog.blog.threadlocal.UserThreadLocal;
import top.liubaiblog.blog.vo.LoginUserVo;
import top.liubaiblog.blog.vo.param.ResponseData;

/**
 * @author 留白
 * @description 测试控制器
 */
@RestController
@Profile("dev")
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ResponseData test() {
        LoginUserVo loginUserVo = UserThreadLocal.get();
        return ResponseData.success(loginUserVo);
    }

}
