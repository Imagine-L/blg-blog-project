package top.liubaiblog.blogadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.liubaiblog.blogadmin.pojo.Role;
import top.liubaiblog.blogadmin.service.RoleService;
import top.liubaiblog.blogadmin.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 13326
* @description 针对表【ms_role(角色表)】的数据库操作Service实现
* @createDate 2022-05-31 21:39:57
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Override
    public List<Role> getByAdminId(Long adminId) {
        return getBaseMapper().selectByAdminId(adminId);
    }
}




