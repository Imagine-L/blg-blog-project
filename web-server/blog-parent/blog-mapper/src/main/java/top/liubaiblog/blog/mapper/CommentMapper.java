package top.liubaiblog.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.liubaiblog.blog.pojo.Comment;

import java.util.List;

/**
 * @author 13326
 * @description 针对表【ms_comment】的数据库操作Mapper
 * @createDate 2022-05-28 22:01:18
 * @Entity top.liubaiblog.blog.pojo.Comment
 */
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查看某个作者的所有评论，但是只有一级评论，
     * 如果此作者有二级评论，则获取到该二级评论的一级评论(这个一级评论可能是其他作者的)
     * 通过一级评论获取到所有的二级子评论即可
     */
    Page<Comment> selectByAuthorId(Page<Comment> page, @Param("authorId") Long authorId);

}




