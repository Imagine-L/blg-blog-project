package top.liubaiblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.liubaiblog.blog.annotation.WriteLog;
import top.liubaiblog.blog.pojo.Tag;
import top.liubaiblog.blog.service.TagService;
import top.liubaiblog.blog.util.PojoUtils;
import top.liubaiblog.blog.vo.ResponseCode;
import top.liubaiblog.blog.vo.TagVo;
import top.liubaiblog.blog.vo.param.ResponseData;

/**
 * @author 留白
 * @description
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 最热标签
     */
    @GetMapping("/hot")
    public ResponseData hot(@RequestParam(required = false, defaultValue = "6") Integer limit) {
        return ResponseData.success(tagService.hotTags(limit));
    }

    /**
     * 全部标签
     */
    @GetMapping
    public ResponseData tags() {
        return ResponseData.success(tagService.listTagsByColumns(Tag::getId, Tag::getTagName));
    }

    /**
     * 全部详细标签
     */
    @GetMapping("/detail")
    public ResponseData detail() {
        return ResponseData.success(tagService.listTags());
    }

    /**
     * 某个详细的标签
     */
    @GetMapping("/detail/{id}")
    public ResponseData detailById(@PathVariable("id") Long id) {
        TagVo tag = PojoUtils.tag.copy(tagService.getById(id));
        return ResponseData.success(tag);
    }

    /**
     * 添加标签
     */
    @WriteLog(module = "文章标签", operation = "添加文章标签")
    @PostMapping("/save")
    public ResponseData save(@RequestBody Tag tag) {
        if (tagService.save(tag)) {
            TagVo tagVo = PojoUtils.tag.copy(tag);
            return ResponseData.success(tagVo);
        } else {
            return ResponseData.build(ResponseCode.PARAMS_ERROR, null);
        }
    }

    /**
     * 删除空标签
     */
    @WriteLog(module = "文章标签", operation = "删除没有文章引用的空标签")
    @DeleteMapping("/empty")
    public ResponseData delete() {
        return ResponseData.success(tagService.removeEmpty());
    }

}
