package top.liubaiblog.blogadmin.service;

import top.liubaiblog.blogadmin.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 13326
* @description 针对表【ms_admin】的数据库操作Service
* @createDate 2022-05-31 19:05:04
*/
public interface AdminService extends IService<Admin> {

    /**
     * 根据用户名获取用户对象
     */
    Admin getByUsername(String username);

}
