package top.liubaiblog.blogadmin.vo;

import lombok.Data;

/**
 * @author 留白
 * @description 分页参数
 */
@Data
public class PageParam {

    private Integer currentPage;

    private Integer pageSize;

    private String queryString;

}
