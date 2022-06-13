package top.liubaiblog.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.liubaiblog.blogadmin.pojo.Admin;
import top.liubaiblog.blogadmin.service.AdminService;
import top.liubaiblog.blogadmin.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author 13326
* @description 针对表【ms_admin】的数据库操作Service实现
* @createDate 2022-05-31 19:05:04
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{

    @Override
    public Admin getByUsername(String username) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username);
        return getOne(wrapper);
    }
}




