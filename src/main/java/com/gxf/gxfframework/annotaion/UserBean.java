package com.gxf.gxfframework.annotaion;

import java.lang.annotation.*;

/**
 * 默认提供从session里面获取用户信息，如果需要自定义，只需把{@link com.gxf.gxfframework.interfaces.HSUserBeanHandler}的实现类 导入容器(提供
 *      默认的从session里面获取用户实例)
 * 如果需要处理用户不存在的情况下的处理情况，只需把{@link com.gxf.gxfframework.interfaces.HSUserNotLoginHandler}的实现类导入容器(提供默认的抛异常的实现)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserBean {

    /**
     * 存储用户信息的key，这个key只对默认策略有效（Session获取用户信息）。如果自定义{@link com.gxf.gxfframework.interfaces.HSUserBeanHandler},这个key无效。
     * @return key
     */
    String userKey();

    /**
     * 用户domain的class类型
     */
    Class<?> userClass();

}
