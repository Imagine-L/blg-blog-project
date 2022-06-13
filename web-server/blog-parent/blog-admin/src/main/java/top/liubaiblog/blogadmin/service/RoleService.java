package top.liubaiblog.blogadmin.service;

import top.liubaiblog.blogadmin.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 13326
* @description 针对表【ms_role(角色表)】的数据库操作Service
* @createDate 2022-05-31 21:39:57
*/
public interface RoleService extends IService<Role> {

    /**
     * 查询指定用户的角色列表
     * @return
     * @param adminId
     */
    List<Role> getByAdminId(Long adminId);
}
