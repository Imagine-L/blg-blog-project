package top.liubaiblog.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author 留白
 * @description 评论用户视图层对象
 */
@Data
public class UserVo {

    private String nickname;

    private String avatar;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
}
