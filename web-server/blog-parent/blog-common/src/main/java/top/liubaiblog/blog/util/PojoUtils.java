package top.liubaiblog.blog.util;

import org.springframework.beans.BeanUtils;
import top.liubaiblog.blog.pojo.SysUser;
import top.liubaiblog.blog.vo.LoginUserVo;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 留白
 * @description 对pojo类进行处理的工具类
 */
public class PojoUtils {

    /**
     * 将源对象中的属性拷贝到目标对象中，并且可以对目标对象执行后续处理
     *
     * @param sourceBean  源对象
     * @param targetClass 目标对象
     * @param function    对目标对象执行后续处理
     */
    public <S, T> T copy(S sourceBean, Class<T> targetClass, Function<T, T> function) {
        try {
            Constructor<T> constructor = targetClass.getDeclaredConstructor();
            T targetBean = constructor.newInstance();
            BeanUtils.copyProperties(sourceBean, targetBean);
            return function.apply(targetBean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对源对象列表拷贝到目标对象列表中，并执行后续处理
     *
     * @param sourceList  源对象列表
     * @param targetClass 目标对象
     * @param function    对每个目标对象执行的处理
     */
    public <S, T> List<T> copyList(List<S> sourceList, Class<T> targetClass, Function<T, T> function) {
        try {
            Constructor<T> constructor = targetClass.getDeclaredConstructor();
            return sourceList.stream().map(source -> {
                T targetBean = null;
                try {
                    targetBean = constructor.newInstance();
                    BeanUtils.copyProperties(source, targetBean);
                    return function.apply(targetBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return targetBean;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        PojoUtils pojoUtils = new PojoUtils();
        SysUser sysUser = new SysUser();
        sysUser.setAccount("jack");
        sysUser.setAvatar("https://www.baidu.com");
        sysUser.setNickname("杰克");
        LoginUserVo user = pojoUtils.copy(sysUser, LoginUserVo.class, (target) -> {
            target.setBrowser("chrome");
            target.setSystem("windows10");
            return target;
        });
        System.out.println(user);
    }

}
