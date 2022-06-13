package top.liubaiblog.blog.mapper;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.liubaiblog.blog.pojo.Tag;

/**
* @author 13326
* @description 针对表【ms_tag】的数据库操作Mapper
* @createDate 2022-05-25 17:43:08
* @Entity top.liubaiblog.blog.pojo.Tag
*/
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据文章id查询其标签
     */
    List<Tag> selectByArticleId(Long articleId);

    /**
     * 查询最热的标签id列表(对应文章乐队)
     */
    List<Long> selectByHotIds(Integer limit);

    /**
     * 查询所有tag的id
     */
    List<Long> selectTagIdAll();
}




