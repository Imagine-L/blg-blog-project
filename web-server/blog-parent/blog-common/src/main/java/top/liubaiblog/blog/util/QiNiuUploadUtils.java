package top.liubaiblog.blog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import top.liubaiblog.blog.properties.QiNiuCloudProperties;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * @author 留白
 * @description 七牛云文件上传工具类
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "qiniu", name = "enable", havingValue = "true")
@Import(QiNiuCloudProperties.class)
public class QiNiuUploadUtils {

    @Autowired
    private QiNiuCloudProperties properties;

    /**
     * 文章上传
     *
     * @param inputStream  文件输入流
     * @param key          七牛云中存储的文件名，默认不指定key的情况下，以文件内容的hash值作为文件名
     * @param respConsumer 对响应结果做处理
     * @param errConsumer  如果出现相关异常，则会使用此接口处理，未出现异常，此方法不会被调用
     */
    public boolean upload(InputStream inputStream, String key, Consumer<Response> respConsumer, Consumer<QiniuException> errConsumer) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        // 上传文件的管理器
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传
        String accessKey = properties.getAccessKey();
        String secretKey = properties.getSecretKey();
        String bucket = properties.getBucket();
        // 根据凭证生成token
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(inputStream, key, upToken, null, null);
            if (respConsumer != null) {
                // 解析上传成功的结果
                respConsumer.accept(response);
            }
            return true;
        } catch (QiniuException ex) {
            if (errConsumer != null) {
                errConsumer.accept(ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 对上面文件上传方法做的简化
     *
     * @param inputStream 文件输入流
     * @param key         七牛云中存储的文件名
     */
    public boolean upload(InputStream inputStream, String key) {
        // 默认的响应处理
        Consumer<Response> respConsumer = resp -> {
            ObjectMapper objectMapper = new ObjectMapper();
            DefaultPutRet putRet = null;
            try {
                putRet = objectMapper.readValue(resp.bodyString(), DefaultPutRet.class);
                log.info("============= <upload successful> =============");
                log.info("file key -> {}", putRet.key);
                log.info("url -> {}/{}", properties.getUrl(), putRet.key);
                log.info("============= <upload end> =============");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        // 默认的异常处理
        Consumer<QiniuException> errConsumer = ex -> {
            Response r = ex.response;
            log.error("============= <upload failed> =============");
            log.error("response statusCode -> {}", r.statusCode);
            log.error("response error -> {}", r.error);
            log.error("============= <upload end> =============");
        };
        return this.upload(inputStream, key, respConsumer, errConsumer);
    }

    public QiNiuCloudProperties getProperties() {
        return properties;
    }

}
