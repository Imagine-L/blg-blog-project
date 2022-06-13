package top.liubaiblog.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.liubaiblog.blog.pojo.SysUser;
import top.liubaiblog.blog.vo.param.LoginParam;

import java.util.Map;

/**
* @author 13326
* @description 针对表【ms_sys_user】的数据库操作Mapper
* @createDate 2022-05-25 17:43:08
* @Entity top.liubaiblog.blog.pojo.SysUser
*/
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser selectByArticleId(Long articleId);

    SysUser selectByLoginParam(LoginParam loginParam);

    int updatePassword(@Param("id") Long id, @Param("oldPwd") String oldPwd, @Param("newPwd") String newPwd);

}




