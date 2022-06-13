package top.liubaiblog.blog.test;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.liubaiblog.blog.mapper.ArticleMapper;
import top.liubaiblog.blog.pojo.Article;

/**
 * @author 留白
 * @description 测试类
 */
@Slf4j
@SpringBootTest
public class AppTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    @Disabled
    public void test() {
//        Page<Article> articlePage = new Page<>(1, 5);
//        articleMapper.selectByAuthor(articlePage, 1L, "commentCounts");
//        System.out.println(articlePage.getRecords());
    }


}
