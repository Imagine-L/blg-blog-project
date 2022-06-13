package top.liubaiblog.blog.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import top.liubaiblog.blog.pojo.Tag;
import top.liubaiblog.blog.vo.TagVo;

import java.util.List;

/**
* @author 13326
* @description 针对表【ms_tag】的数据库操作Service
* @createDate 2022-05-25 17:43:08
*/
public interface TagService extends IService<Tag> {

    /**
     * 根据文章id查询其标签
     */
    List<TagVo> listByArticleId(Long articleId);

    /**
     * 查询最热的标签
     */
    List<Tag> hotTags(Integer limit);

    /**
     * 获取部分标签
     */
    List<TagVo> listTagsByColumns(SFunction<Tag, ?>... columns);

    /**
     * 获取全部标签
     */
    List<TagVo> listTags();

    /**
     * 删除没有文章的空标签，返回删除后的id列表
     */
    List<String> removeEmpty();
}
