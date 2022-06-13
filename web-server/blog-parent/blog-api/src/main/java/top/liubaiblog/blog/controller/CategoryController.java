package top.liubaiblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.liubaiblog.blog.annotation.WriteLog;
import top.liubaiblog.blog.pojo.Category;
import top.liubaiblog.blog.service.CategoryService;
import top.liubaiblog.blog.util.PojoUtils;
import top.liubaiblog.blog.vo.CategoryVo;
import top.liubaiblog.blog.vo.ResponseCode;
import top.liubaiblog.blog.vo.param.ResponseData;

import java.util.List;

/**
 * @author 留白
 * @description 文章分类控制器
 */
@RestController
@RequestMapping("/categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取部分字段分类列表
     */
    @GetMapping
    public ResponseData categories() {
        List<CategoryVo> categoryVos = categoryService.listCategoryVoByColumns(Category::getId,
                Category::getCategoryName);
        return ResponseData.success(categoryVos);
    }

    /**
     * 获取全部字段分类列表
     */
    @GetMapping("/detail")
    public ResponseData detail() {
        return ResponseData.success(categoryService.listCategoryVo());
    }

    /**
     * 某个某个分类的全部字段
     */
    @GetMapping("/detail/{id}")
    public ResponseData detailById(@PathVariable("id") Long id) {
        CategoryVo categoryVo = PojoUtils.category.copy(categoryService.getById(id));
        return ResponseData.success(categoryVo);
    }

    /**
     * 添加分类
     */
    @PostMapping("/save")
    @WriteLog(module = "文章分类", operation = "添加文章分类")
    public ResponseData save(@RequestBody Category category) {
        if (categoryService.save(category)) {
            CategoryVo categoryVo = PojoUtils.category.copy(category);
            return ResponseData.success(categoryVo);
        } else {
            return ResponseData.build(ResponseCode.PARAMS_ERROR, null);
        }
    }

    /**
     * 删除空分类
     */
    @DeleteMapping("/empty")
    @WriteLog(module = "文章分类", operation = "删除文章分类")
    public ResponseData delete() {
        return ResponseData.success(categoryService.removeEmpty());
    }
}
