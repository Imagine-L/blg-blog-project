package top.liubaiblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.liubaiblog.blog.mapper.CategoryMapper;
import top.liubaiblog.blog.pojo.Category;
import top.liubaiblog.blog.service.ArticleService;
import top.liubaiblog.blog.service.CategoryService;
import top.liubaiblog.blog.util.PojoUtils;
import top.liubaiblog.blog.vo.CategoryVo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 13326
 * @description 针对表【ms_category】的数据库操作Service实现
 * @createDate 2022-05-27 20:50:44
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @SafeVarargs
    @Override
    public final List<CategoryVo> listCategoryVoByColumns(SFunction<Category, ?>... columns) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(columns);
        List<Category> list = list(wrapper);
        return PojoUtils.category.copyList(list);
    }

    @Override
    public List<CategoryVo> listCategoryVo() {
        List<Category> list = list();
        return PojoUtils.category.copyList(list);
    }

    @Override
    public List<String> removeEmpty() {
        // 获取全部已有文章分类id
        List<Long> useCategoryIds = articleService.getCategoryIdAll();
        // 获取所有分类Id
        List<Category> categories = this.list();
        // 过滤出未被使用的分类id
        List<String> unUseCategoryIds = categories.stream()
                .map(Category::getId)
                .filter(id -> !useCategoryIds.contains(id))
                .map(String::valueOf)
                .collect(Collectors.toList());
        // 删除未使用的分类id，并返回删除的id
        return removeBatchByIds(unUseCategoryIds) ? unUseCategoryIds : Collections.emptyList();
    }

}




