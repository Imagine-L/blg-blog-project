package top.liubaiblog.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;
import top.liubaiblog.blogadmin.pojo.Permission;
import top.liubaiblog.blogadmin.service.PermissionService;
import top.liubaiblog.blogadmin.mapper.PermissionMapper;
import org.springframework.stereotype.Service;
import top.liubaiblog.blogadmin.vo.PageParam;
import top.liubaiblog.blogadmin.vo.PageResult;

import java.util.Collections;
import java.util.List;

/**
 * @author 13326
 * @description 针对表【ms_permission】的数据库操作Service实现
 * @createDate 2022-05-31 19:05:04
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionService {

    @Override
    public PageResult<Permission> listByParam(PageParam pageParam) {
        PageResult<Permission> pageResult = new PageResult<>();
        // 非空判断
        if (pageParam == null) {
            pageResult.setList(Collections.emptyList());
            pageResult.setTotal(0L);
            return pageResult;
        }
        Page<Permission> permissionPage = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        // 筛选条件
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(pageParam.getQueryString())) {
            wrapper.like(Permission::getName, pageParam.getQueryString());
        }
        this.page(permissionPage, wrapper);
        // 设置结果集
        pageResult.setList(permissionPage.getRecords());
        pageResult.setTotal(permissionPage.getTotal());
        return pageResult;
    }

    @Override
    public List<Permission> listByAdminId(Long adminId) {
        return getBaseMapper().selectByAdminId(adminId);
    }

    @Override
    public List<Permission> listByRoleId(Long rid) {
        return getBaseMapper().selectByRoleId(rid);
    }
}




