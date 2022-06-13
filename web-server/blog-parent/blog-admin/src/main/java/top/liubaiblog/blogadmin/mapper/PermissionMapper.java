package top.liubaiblog.blogadmin.mapper;

import top.liubaiblog.blogadmin.pojo.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 13326
* @description 针对表【ms_permission】的数据库操作Mapper
* @createDate 2022-05-31 19:05:04
* @Entity top.liubaiblog.blogadmin.pojo.Permission
*/
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> selectByAdminId(Long adminId);

    List<Permission> selectByRoleId(Long rid);
}




