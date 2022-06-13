package top.liubaiblog.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.liubaiblog.blog.pojo.SysUser;
import top.liubaiblog.blog.vo.LoginUserVo;
import top.liubaiblog.blog.vo.param.LoginParam;
import top.liubaiblog.blog.vo.param.PasswordParam;
import top.liubaiblog.blog.vo.param.UserParam;

/**
* @author 13326
* @description 针对表【ms_sys_user】的数据库操作Service
* @createDate 2022-05-25 17:43:08
*/
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据文章的id获取其作者对象
     */
    SysUser getByArticleId(Long articleId);

    /**
     * 对登录参数进行校验
     */
    LoginUserVo checkLoginParam(LoginParam loginParam);

    /**
     * 修改密码
     */
    boolean changePassword(PasswordParam passwordParam);

    /**
     * 更新用户数据
     */
    boolean updateByParam(UserParam userParam);
}
