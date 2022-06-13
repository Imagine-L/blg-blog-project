package top.liubaiblog.blogadmin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.liubaiblog.blogadmin.pojo.Admin;
import top.liubaiblog.blogadmin.service.AdminService;

import java.util.ArrayList;

/**
 * @author 留白
 * @description
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin loadUser = adminService.getByUsername(username);
        if (loadUser == null) {
            throw new UsernameNotFoundException("未查询到用户名为【" + username + "】的用户!");
        }
        return new User(loadUser.getUsername(), loadUser.getPassword(), new ArrayList<>());
    }
}
