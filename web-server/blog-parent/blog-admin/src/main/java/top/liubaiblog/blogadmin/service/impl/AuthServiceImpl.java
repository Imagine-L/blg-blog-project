package top.liubaiblog.blogadmin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import top.liubaiblog.blogadmin.pojo.Admin;
import top.liubaiblog.blogadmin.pojo.Permission;
import top.liubaiblog.blogadmin.pojo.Role;
import top.liubaiblog.blogadmin.service.AdminService;
import top.liubaiblog.blogadmin.service.AuthService;
import top.liubaiblog.blogadmin.service.PermissionService;
import top.liubaiblog.blogadmin.service.RoleService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 留白
 * @description
 */
@Slf4j
@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminService adminService;

    @Override
    public boolean auth(HttpServletRequest request, Authentication authentication) {
        String requestURI = request.getRequestURI();
        log.info("request uri -> {}", requestURI);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        // 如果用户为空，或者为匿名用户，则直接拦截
        if (principal == null || "anonymousUser".equals(principal.getUsername())) {
            return false;
        }
        // 根据用户名查询用户
        Admin user = adminService.getByUsername(principal.getUsername());
        if (user == null) {
            return false;
        }
        // 根据用户id查询角色
        List<Role> roles = roleService.getByAdminId(user.getId());
        // 如果用户角色为admin，则直接放行
        for (Role role : roles) {
            if ("admin".equals(role.getRoleMark())) {
                return true;
            }
        }
        // 根据角色获取到所有的权限
        List<Permission> permissions = roles.stream()
                .flatMap(role -> permissionService.listByRoleId(role.getRid()).stream())
                .distinct()
                .collect(Collectors.toList());
        permissions.forEach(System.out::println);
        requestURI = requestURI.split("\\?")[0];
        // 其余用户，查询权限表判断是否有此接口权限
        for (Permission permission : permissions) {
            if (requestURI.equals(permission.getPath())) {
                return true;
            }
        }
        return false;
    }
}
