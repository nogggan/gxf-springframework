package com.gxf.gxfframework.annotaion;

import java.lang.annotation.*;

/**
 * @author Xiao Feng
 * 功能描述：参数列表标注此注解，可以注入当前用户
 * @创建时间: 2018/12/29
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
}
