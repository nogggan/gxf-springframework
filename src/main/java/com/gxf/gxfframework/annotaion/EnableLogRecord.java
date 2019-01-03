package com.gxf.gxfframework.annotaion;

import com.gxf.gxfframework.log.GlobalLogAopRecord;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Xiao Feng
 * @创建时间: 2019/01/02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableAspectJAutoProxy
@Import({GlobalLogAopRecord.class})
public @interface EnableLogRecord {

}
