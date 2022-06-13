package top.liubaiblog.blog.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import top.liubaiblog.blog.pojo.Category;
import top.liubaiblog.blog.vo.CategoryVo;

import java.util.List;

/**
* @author 13326
* @description 针对表【ms_category】的数据库操作Service
* @createDate 2022-05-27 20:50:44
*/
public interface CategoryService extends IService<Category> {

    /**
     * 获取文章分类列表部分字段，并转换为vo对象
     */
    List<CategoryVo> listCategoryVoByColumns(SFunction<Category, ?>... columns);

    /**
     * 获取文章分类列表全部字段，并转换为vo对象
     */
    List<CategoryVo> listCategoryVo();

    /**
     * 删除空分类
     * @return
     */
    List<String> removeEmpty();
}
