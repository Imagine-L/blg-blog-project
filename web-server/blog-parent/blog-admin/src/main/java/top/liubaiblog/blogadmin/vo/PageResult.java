package top.liubaiblog.blogadmin.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 留白
 * @description 返回的页面结果
 */
@Data
public class PageResult<T> {
    private List<T> list;

    private Long total;
}
