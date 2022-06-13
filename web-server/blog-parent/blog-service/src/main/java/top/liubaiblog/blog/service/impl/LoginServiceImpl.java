package top.liubaiblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import top.liubaiblog.blog.constant.TokenConstants;
import top.liubaiblog.blog.exception.AccountExistException;
import top.liubaiblog.blog.exception.AuthenticationException;
import top.liubaiblog.blog.exception.AuthorizationException;
import top.liubaiblog.blog.exception.ParamIllegalException;
import top.liubaiblog.blog.pojo.SysUser;
import top.liubaiblog.blog.service.LoginService;
import top.liubaiblog.blog.service.SysUserService;
import top.liubaiblog.blog.util.PojoUtils;
import top.liubaiblog.blog.util.RedisUtils;
import top.liubaiblog.blog.util.TokenUtils;
import top.liubaiblog.blog.vo.LoginUserVo;
import top.liubaiblog.blog.vo.param.LoginParam;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 留白
 * @description 登录相关服务类实现
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService userService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String loginAfterCreateToken(LoginParam loginParam) {
        if (loginParam == null) throw new ParamIllegalException("用户名或密码为空");
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        // 检查参数是否合法
        if (!StringUtils.hasLength(account) || !StringUtils.hasLength(password)) {
            throw new ParamIllegalException("用户名或密码为空");
        }
        // 从数据库校验用户名和密码
        LoginUserVo loginUser = userService.checkLoginParam(loginParam);
        if (loginUser == null) {
            throw new AuthenticationException("用户名或密码错误");
        }
        // 生成token
        Map<String, Object> map = new HashMap<>(5);
        map.put("userId", loginUser.getId());
        map.put("account", loginUser.getAccount());
        String token = TokenUtils.create(map);
        // 将token存入redis
        redisUtils.valSet(TokenConstants.TOKEN_PREFIX + token,
                loginUser,
                TokenConstants.TOKEN_EXPIRATION,
                TokenConstants.TOKEN_TIMEUNIT);
        return token;
    }

    @Override
    public void logoutAfterRemoveToken(String token) {
        redisUtils.delete(TokenConstants.TOKEN_PREFIX + token);
    }

    @Override
    @Transactional
    public String registerAfterCreateToken(LoginParam loginParam) {
        // 检查参数合法性
        if (loginParam == null ||
                !StringUtils.hasLength(loginParam.getAccount()) ||
                !StringUtils.hasLength(loginParam.getPassword()) ||
                !StringUtils.hasLength(loginParam.getNickname())) {
            throw new ParamIllegalException("用户名或密码为空");
        }
        // 查询用户名是否已存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount, loginParam.getAccount());
        if (userService.count(wrapper) != 0) {
            throw new AccountExistException("用户已存在");
        }
        // 构造新用户对象
        SysUser newUser = newSysUser(loginParam);
        // 向数据库插入新用户
        userService.save(newUser);
        // 创建token
        HashMap<String, Object> map = new HashMap<>(5);
        map.put("userId", newUser.getId());
        map.put("account", loginParam.getAccount());
        String token = TokenUtils.create(map);
        // 向redis中记录token及其数据
        LoginUserVo newLoginUser = PojoUtils.sysUser.copy(newUser, true);
        redisUtils.valSet(TokenConstants.TOKEN_PREFIX + token,
                newLoginUser,
                TokenConstants.TOKEN_EXPIRATION,
                TokenConstants.TOKEN_TIMEUNIT);
        // 返回token
        return token;
    }

    @Override
    public LoginUserVo checkTokenAndGetLoginUser(String token) {
        // 使用jwt解析token是否合法
        Map<String, Object> map = TokenUtils.parse(token);
        if (map == null || map.isEmpty()) {
            throw new AuthorizationException("token不合法");
        }
        // 从redis中再次校验token是否过期
        LoginUserVo loginUserVo = redisUtils.valGetObject(TokenConstants.TOKEN_PREFIX + token, LoginUserVo.class);
        if (loginUserVo == null) {
            throw new AuthorizationException("token已过期");
        }
        // 未过期则返回登录的用户
        return loginUserVo;
    }

    @Override
    public LoginUserVo getLoginUserByToken(String token) {
        // token字符串合法性校验
        if (!StringUtils.hasLength(token)) {
            throw new AuthorizationException("token为空");
        }
        // 校验token，并获取登录的用户
        LoginUserVo loginUserVo = checkTokenAndGetLoginUser(token);
        // 从数据库中获取最新的数据
        SysUser sysUser = userService.getById(loginUserVo.getId());
        LoginUserVo newLoginUser = PojoUtils.sysUser.copy(sysUser, true);
        // 将最新的数据存入redis
        redisUtils.valSet(TokenConstants.TOKEN_PREFIX + token, newLoginUser);
        return newLoginUser;
    }

    private SysUser newSysUser(LoginParam loginParam) {
        // 生成参数
        long nowTime = System.currentTimeMillis();
        String slat = UUID.randomUUID().toString().replaceAll("-", "");
        String md5Pwd = DigestUtils.md5DigestAsHex((loginParam.getPassword() + slat).getBytes(StandardCharsets.UTF_8));
        // 填充参数
        SysUser sysUser = new SysUser();
        sysUser.setAccount(loginParam.getAccount());
        sysUser.setAdmin(false);
        sysUser.setAvatar("http://blog.liubaiblog.top/blog/default_avatar.png");
        sysUser.setCreateDate(nowTime);
        sysUser.setDeleted(false);
        sysUser.setEmail("");
        sysUser.setLastLogin(nowTime);
        sysUser.setMobilePhoneNumber("");
        sysUser.setNickname(loginParam.getNickname());
        sysUser.setPassword(md5Pwd);
        sysUser.setSalt(slat);
        sysUser.setStatus("1");
        return sysUser;
    }

}