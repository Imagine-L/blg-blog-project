package top.liubaiblog.blog.threadlocal;

import top.liubaiblog.blog.vo.LoginUserVo;

/**
 * @author 留白
 * @description 用户的本地线程对象
 */
public final class UserThreadLocal {

    private UserThreadLocal() {}

    private final static ThreadLocal<LoginUserVo> LOCAL = new ThreadLocal<>();

    public static void put(LoginUserVo loginUserVo) {
        LOCAL.set(loginUserVo);
    }

    public static LoginUserVo get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}
