package top.liubaiblog.blog.util;

import eu.bitwalker.useragentutils.UserAgent;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import top.liubaiblog.blog.pojo.*;
import top.liubaiblog.blog.service.*;
import top.liubaiblog.blog.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 留白
 * @description 操作pojo对象的工具类
 */
public final class PojoUtils {

    private PojoUtils() {
    }

    public final static ArticleUtils article = new ArticleUtils();
    public final static TagUtils tag = new TagUtils();
    public final static SysUserUtils sysUser = new SysUserUtils();
    public final static ArticleBodyUtils articleBody = new ArticleBodyUtils();
    public final static CommentUtils comment = new CommentUtils();
    public final static CategoryUtils category = new CategoryUtils();
    private final static TagService tagService;
    private final static SysUserService userService;
    private final static CommentService commentService;
    private final static ArticleService articleService;
    private final static ArticleBodyService articleBodyService;
    private final static CategoryService categoryService;

    static {
        tagService = (TagService) SpringAppUtils.getBean(TagService.class);
        userService = (SysUserService) SpringAppUtils.getBean(SysUserService.class);
        articleService = (ArticleService) SpringAppUtils.getBean(ArticleService.class);
        categoryService = (CategoryService) SpringAppUtils.getBean(CategoryService.class);
        articleBodyService = (ArticleBodyService) SpringAppUtils.getBean(ArticleBodyService.class);
        commentService = (CommentService) SpringAppUtils.getBean(CommentService.class);
    }

    public static class ArticleUtils {

        private ArticleUtils() {
        }

        /**
         * 将一个文章兑现转换为表示层对象(pojo -> vo)
         */
        public ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
            ArticleVo articleVo = new ArticleVo();
            // spring提供的bean工具类，可以把第一个形参对象的相同属性拷贝到第二个形参对象
            BeanUtils.copyProperties(article, articleVo);
            articleVo.setCreateDate(new DateTime(articleVo.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
            if (isTag) {
                // 获取标签名
                articleVo.setTags(tagService.listByArticleId(article.getId()));
            }
            if (isAuthor) {
                // 获取作者名和作者头像
                SysUser sysUser = userService.getByArticleId(article.getId());
                if (sysUser != null) {
                    UserVo author = new UserVo();
                    BeanUtils.copyProperties(sysUser, author);
                    articleVo.setAuthor(author);
                }
            }
            if (isBody) {
                // 获取文章体
                ArticleBodyVo articleBody = articleBodyService.getByArticleId(article.getId());
                articleVo.setBody(articleBody);
            }
            if (isCategory) {
                // 获取文章分类
                Category category = categoryService.getById(article.getCategoryId());
                CategoryVo categoryVo = new CategoryVo();
                BeanUtils.copyProperties(category, categoryVo);
                articleVo.setCategory(categoryVo);
            }
            return articleVo;
        }

        /**
         * 将文章列表转换为表示层对象列表形式(pojo -> vo)
         */
        public List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
            return records.stream()
                    .map(article -> PojoUtils.article.copy(article, isTag, isAuthor, false, false))
                    .collect(Collectors.toList());
        }

        /**
         * 将文章列表转换为表示层对象列表形式(pojo -> vo)
         */
        public List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
            return records.stream()
                    .map(article -> PojoUtils.article.copy(article, isTag, isAuthor, isBody, isCategory))
                    .collect(Collectors.toList());
        }
    }

    public static class TagUtils {

        private TagUtils() {
        }

        /**
         * 将一个标签对象兑现转换为表示层对象(pojo -> vo)
         */
        public TagVo copy(Tag tag) {
            TagVo tagVo = new TagVo();
            // spring提供的bean工具类，可以把第一个形参对象的相同属性拷贝到第二个形参对象
            BeanUtils.copyProperties(tag, tagVo);
            return tagVo;
        }

        /**
         * 将标签对象列表转换为表示层对象列表形式(pojo -> vo)
         */
        public List<TagVo> copyList(List<Tag> records) {
            return records.stream()
                    .map(PojoUtils.tag::copy)
                    .collect(Collectors.toList());
        }
    }

    public static class SysUserUtils {

        private SysUserUtils() {
        }

        /**
         * 将一个系统对象兑现转换为表示层对象(pojo -> vo)
         */
        public LoginUserVo copy(SysUser sysUser, boolean parseRequest) {
            LoginUserVo loginUserVo = new LoginUserVo();
            // spring提供的bean工具类，可以把第一个形参对象的相同属性拷贝到第二个形参对象
            BeanUtils.copyProperties(sysUser, loginUserVo);
            // 获取当前线程的请求完善loginUser
            if (parseRequest) {
                HttpRequestUtils requestUtils = (HttpRequestUtils) SpringAppUtils.getBean(HttpRequestUtils.class);
                HttpServletRequest request = requestUtils.getLocalRequest();
                if (Objects.isNull(request)) return loginUserVo;
                UserAgent userAgent = requestUtils.getUserAgent();
                loginUserVo.setRemoteIp(request.getRemoteAddr());
                loginUserVo.setBrowser(userAgent.getBrowser().getName());
                loginUserVo.setSystem(userAgent.getOperatingSystem().getName());
            }
            return loginUserVo;
        }
    }

    public static class ArticleBodyUtils {

        private ArticleBodyUtils() {
        }

        public ArticleBodyVo copy(ArticleBody articleBody) {
            ArticleBodyVo articleBodyVo = new ArticleBodyVo();
            articleBodyVo.setContent(articleBody.getContent());
            return articleBodyVo;
        }
    }

    public static class CommentUtils {
        private CommentUtils() {
        }

        public CommentVo copy(Comment comment) {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);
            // 格式化评论时间
            commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
            // 获取评论作者
            SysUser sysAuthor = userService.getById(comment.getAuthorId());
            UserVo author = new UserVo();
            BeanUtils.copyProperties(sysAuthor, author);
            commentVo.setAuthor(author);
            // 获取评论的所有子评论
            List<CommentVo> children = commentService.getChildren(comment.getId());
            commentVo.setChildrens(children);
            // 如果level大于1，表示为子评论，还需要查询自评论时评论给谁的
            if (comment.getLevel() > 1) {
                SysUser sysToUser = userService.getById(comment.getToUid());
                UserVo toUser = new UserVo();
                BeanUtils.copyProperties(sysToUser, toUser);
                commentVo.setToUser(toUser);
            }
            return commentVo;
        }

        public List<CommentVo> copyList(List<Comment> comments) {
            return comments.stream()
                    .map(this::copy)
                    .collect(Collectors.toList());
        }
    }

    public static class CategoryUtils {
        private CategoryUtils() {
        }

        public CategoryVo copy(Category category) {
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category, categoryVo);
            return categoryVo;
        }

        public List<CategoryVo> copyList(List<Category> records) {
            return records.stream()
                    .map(this::copy)
                    .collect(Collectors.toList());
        }
    }

}
