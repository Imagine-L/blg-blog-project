package top.liubaiblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.liubaiblog.blog.mapper.TagMapper;
import top.liubaiblog.blog.pojo.Tag;
import top.liubaiblog.blog.service.TagService;
import top.liubaiblog.blog.util.PojoUtils;
import top.liubaiblog.blog.vo.TagVo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 13326
 * @description 针对表【ms_tag】的数据库操作Service实现
 * @createDate 2022-05-25 17:43:08
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    @Override
    public List<TagVo> listByArticleId(Long articleId) {
        List<Tag> tags = getBaseMapper().selectByArticleId(articleId);
        return PojoUtils.tag.copyList(tags);
    }

    @Override
    @Cacheable(cacheNames = "hotTags")
    public List<Tag> hotTags(Integer limit) {
        // 查询最热的id
        List<Long> ids = getBaseMapper().selectByHotIds(limit);
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        // 根据最热的id查询tag对象(这样做可以解耦)
        return getBaseMapper().selectBatchIds(ids);
    }

    @SafeVarargs
    @Override
    public final List<TagVo> listTagsByColumns(SFunction<Tag, ?>... columns) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(columns);
        List<Tag> list = list(wrapper);
        return PojoUtils.tag.copyList(list);
    }

    @Override
    public List<TagVo> listTags() {
        List<Tag> list = list();
        return PojoUtils.tag.copyList(list);
    }

    @Override
    public List<String> removeEmpty() {
        // 获取已被文章使用的标签
        List<Long> useTagIds = getBaseMapper().selectTagIdAll();
        // 获取全部标签
        List<Tag> tags = this.list();
        // 获取未被使用的标签
        List<String> unUseTagIds = tags.stream()
                .map(Tag::getId)
                .filter(id -> !useTagIds.contains(id))
                .map(String::valueOf)
                .collect(Collectors.toList());
        // 删除未使用的标签，并返回删除的标签
        return removeBatchByIds(unUseTagIds) ? unUseTagIds : Collections.emptyList();
    }
}




