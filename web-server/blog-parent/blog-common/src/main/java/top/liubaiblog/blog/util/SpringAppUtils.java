package top.liubaiblog.blog.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 留白
 * @description 获取spring bean对象的工具类
 */
@Component
public class SpringAppUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringAppUtils.applicationContext = applicationContext;
    }

    public static Object getBean(Class<?> clazz) throws BeansException {
        return applicationContext.getBean(clazz);
    }

    public static Object getBean(String beanName, Class<?> clazz) {
        return applicationContext.getBean(beanName, clazz);
    }

}
