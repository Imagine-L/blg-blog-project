package top.liubaiblog.blogadmin;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.liubaiblog.blogadmin.pojo.Admin;
import top.liubaiblog.blogadmin.pojo.Permission;
import top.liubaiblog.blogadmin.pojo.Role;
import top.liubaiblog.blogadmin.service.AdminService;
import top.liubaiblog.blogadmin.service.PermissionService;
import top.liubaiblog.blogadmin.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 留白
 * @description
 */
@SpringBootTest
public class AppAdminTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PermissionService permissionService;

    @Test
    @Disabled
    public void test() {
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        adminService.save(admin);
    }

    @Test
    @Disabled
    public void test2() {
        List<Role> roles = roleService.getByAdminId(1L);
        List<Permission> permissions = roles.stream()
                .flatMap(role -> permissionService.listByRoleId(role.getRid()).stream())
                .distinct()
                .collect(Collectors.toList());
        permissions.forEach(System.out::println);
    }

    @Test
    public void test3() {
        System.out.println("hello");
    }
}
