package top.liubaiblog.blogadmin.mapper;

import top.liubaiblog.blogadmin.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 13326
* @description 针对表【ms_role(角色表)】的数据库操作Mapper
* @createDate 2022-05-31 21:39:57
* @Entity top.liubaiblog.blogadmin.pojo.Role
*/
public interface RoleMapper extends BaseMapper<Role> {


    List<Role> selectByAdminId(Long adminId);
}




