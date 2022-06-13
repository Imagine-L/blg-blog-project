package top.liubaiblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.liubaiblog.blog.constant.TokenConstants;
import top.liubaiblog.blog.exception.AuthorizationException;
import top.liubaiblog.blog.mapper.SysUserMapper;
import top.liubaiblog.blog.pojo.SysUser;
import top.liubaiblog.blog.service.LoginService;
import top.liubaiblog.blog.service.SysUserService;
import top.liubaiblog.blog.service.UploadService;
import top.liubaiblog.blog.threadlocal.UserThreadLocal;
import top.liubaiblog.blog.util.HttpRequestUtils;
import top.liubaiblog.blog.util.PojoUtils;
import top.liubaiblog.blog.util.RedisUtils;
import top.liubaiblog.blog.util.TokenUtils;
import top.liubaiblog.blog.vo.LoginUserVo;
import top.liubaiblog.blog.vo.param.LoginParam;
import top.liubaiblog.blog.vo.param.PasswordParam;
import top.liubaiblog.blog.vo.param.UserParam;

import java.util.Arrays;
import java.util.Map;

/**
 * @author 13326
 * @description 针对表【ms_sys_user】的数据库操作Service实现
 * @createDate 2022-05-25 17:43:08
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    @Autowired
    private HttpRequestUtils httpRequestUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UploadService uploadService;

    @Override
    public SysUser getByArticleId(Long articleId) {
        return getBaseMapper().selectByArticleId(articleId);
    }

    @Override
    public LoginUserVo checkLoginParam(LoginParam loginParam) {
        SysUser sysUser = getBaseMapper().selectByLoginParam(loginParam);
        return sysUser != null ? PojoUtils.sysUser.copy(sysUser, true) : null;
    }

    @Override
    public boolean changePassword(PasswordParam passwordParam) {
        // 参数验证
        if (passwordParam == null ||
                !StringUtils.hasLength(passwordParam.getOldPwd()) ||
                !StringUtils.hasLength(passwordParam.getNewPwd())) {
            return false;
        }
        // 获取当前登录用户
        LoginUserVo loginUser = UserThreadLocal.get();
        if (loginUser == null) return false;
        // 修改密码
        int row = getBaseMapper().updatePassword(loginUser.getId(),
                passwordParam.getOldPwd(),
                passwordParam.getNewPwd());
        return row > 0;
    }

    @Override
    @Transactional
    public boolean updateByParam(UserParam userParam) {
        // 获取当前登录的用户
        LoginUserVo loginUserVo = UserThreadLocal.get();
        // 获取头像
        MultipartFile avatar = userParam.getAvatar();
        // 判断是否需要上传头像，需要则上传
        String avatarFilename = null;
        if (avatar != null && uploadService.checkFileSuffix(avatar, Arrays.asList("jpg", "png", "gif", "jpeg", "svg", ".ico"))) {
            avatarFilename = uploadService.upload(avatar);
        }
        // 封装更新的对象
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userParam, sysUser);
        sysUser.setId(loginUserVo.getId());
        sysUser.setAvatar(avatarFilename);
        // 向数据库更新数据
        if (updateById(sysUser)) {
            // 更新完数据后，再更新redis
            BeanUtils.copyProperties(userParam, loginUserVo);
            loginUserVo.setAvatar(avatarFilename != null ? avatarFilename : loginUserVo.getAvatar());
            String token = httpRequestUtils.getToken();
            redisUtils.valSet(TokenConstants.TOKEN_PREFIX + token, loginUserVo,
                    TokenConstants.TOKEN_EXPIRATION,
                    TokenConstants.TOKEN_TIMEUNIT);
            log.info("更新【{}】用户数据成功", loginUserVo.getAccount());
            return true;
        } else {
            log.error("更新【{}】用户数据失败", loginUserVo.getAccount());
            throw new RuntimeException("更新用户头像失败");
        }
    }
}




