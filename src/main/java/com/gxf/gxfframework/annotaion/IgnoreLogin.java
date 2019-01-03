package com.gxf.gxfframework.annotaion;

import java.lang.annotation.*;

/**
 * @author Xiao Feng
 * 功能描述：标注此注解，拦截器不会拦截
 * @创建时间: 2018/12/29
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreLogin {
}
