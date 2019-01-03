package com.gxf.gxfframework.annotaion;

import java.lang.annotation.*;

/**
 * @author Xiao Feng
 * @创建时间: 2019/01/02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LogRecord {
}
