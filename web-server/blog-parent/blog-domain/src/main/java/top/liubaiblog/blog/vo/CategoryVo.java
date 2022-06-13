package top.liubaiblog.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author 留白
 * @description 分类表示对象
 */
@Data
public class CategoryVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String avatar;

    private String categoryName;

    private String description;

}
