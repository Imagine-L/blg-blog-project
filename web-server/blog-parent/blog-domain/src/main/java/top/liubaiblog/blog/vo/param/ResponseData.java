package top.liubaiblog.blog.vo.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.liubaiblog.blog.vo.ResponseCode;

/**
 * @author 留白
 * @description 响应数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {

    private boolean success;    // 是否成功
    private Integer code;       // 状态码
    private String msg;         // 响应消息
    private Object data;        // 响应数据

    /**
     * 成功响应
     */
    public static ResponseData success(Object data) {
        return new ResponseData(true, 200, "success", data);
    }

    /**
     * 失败响应
     */
    public static ResponseData fail(int code, String msg) {
        return new ResponseData(false, code, msg, null);
    }

    /**
     * 构造一个响应信息
     */
    public static ResponseData build(ResponseCode responseCode, Object data) {
        return new ResponseData(responseCode.getCode() == 200,
                responseCode.getCode(),
                responseCode.getMsg(),
                data);
    }
}
