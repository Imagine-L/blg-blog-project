package top.liubaiblog.blogadmin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.liubaiblog.blog.vo.param.ResponseData;
import top.liubaiblog.blogadmin.pojo.Permission;
import top.liubaiblog.blogadmin.service.PermissionService;
import top.liubaiblog.blogadmin.vo.PageParam;

/**
 * @author 留白
 * @description 后台管理控制器
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PermissionService permissionService;

    // 查
    @PostMapping("/permission/permissionList")
    public ResponseData listPermission(@RequestBody PageParam pageParam) {
        return ResponseData.success(permissionService.listByParam(pageParam));
    }

    // 增
    @PostMapping("/permission/add")
    public ResponseData add(@RequestBody Permission permission) {
        return ResponseData.success(permissionService.save(permission));
    }

    // 改
    @PostMapping("/permission/update")
    public ResponseData update(@RequestBody Permission permission) {
        return ResponseData.success(permissionService.updateById(permission));
    }

    // 删
    @GetMapping("/permission/delete/{id}")
    public ResponseData delete(@PathVariable("id") Long id) {
        return ResponseData.success(permissionService.removeById(id));
    }

}
