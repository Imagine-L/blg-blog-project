package top.liubaiblog.blogadmin.service;

import top.liubaiblog.blogadmin.pojo.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import top.liubaiblog.blogadmin.vo.PageParam;
import top.liubaiblog.blogadmin.vo.PageResult;

import java.util.List;

/**
 * @author 13326
 * @description 针对表【ms_permission】的数据库操作Service
 * @createDate 2022-05-31 19:05:04
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取权限列表结果集
     */
    PageResult<Permission> listByParam(PageParam pageParam);

    /**
     * 根据用户Id查询指定用户对应权限
     */
    List<Permission> listByAdminId(Long adminId);

    /**
     * 查询某个角色的全部权限
     */
    List<Permission> listByRoleId(Long rid);
}
