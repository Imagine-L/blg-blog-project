package top.liubaiblog.blog.vo.param;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 留白
 * @description 更新用户的参数
 */
@Data
public class UserParam {
    private String nickname;
    private String address;
    private String email;
    private String description;
    private String info;
    private MultipartFile avatar;
}
