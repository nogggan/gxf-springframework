package com.gxf.gxfframework.resolver;

import com.gxf.gxfframework.annotaion.LoginUser;
import com.gxf.gxfframework.annotaion.UserBean;
import com.gxf.gxfframework.common.PriorityHandlerUserProvide;
import com.gxf.gxfframework.interfaces.HSUserBeanHandler;
import com.gxf.gxfframework.interfaces.HSUserNotLoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Xiao Feng
 * @创建时间: 2018/12/29
 */
public class UserBeanHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private Class<?> clazz;

    @Autowired
    private PriorityHandlerUserProvide priorityHandlerUserProvide;

    public UserBeanHandlerMethodArgumentResolver(Class<?> clazz){
        this.clazz = clazz;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType()==clazz && isLoginUserAnnotation(methodParameter);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //获取最高优先级的解析器
        HSUserBeanHandler hsUserBeanHandler = priorityHandlerUserProvide.getHighPriorityHandlerUser();
        Object result = hsUserBeanHandler.get(nativeWebRequest.getNativeRequest(HttpServletRequest.class));
        if(null==result){
            //处理登录为空的情况
            HSUserNotLoginHandler hsUserNotLoginHandler = priorityHandlerUserProvide.getHighPriorityHandlerNotLoginUser();
            HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
            HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
            hsUserNotLoginHandler.handlerNotLoginUser(request,response);
        }
        return result;
    }

    private boolean isLoginUserAnnotation(MethodParameter methodParameter) {
        return methodParameter.getParameter().isAnnotationPresent(LoginUser.class);
    }
}
