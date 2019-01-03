package com.gxf.gxfframework.interceptor;

import com.gxf.gxfframework.annotaion.IgnoreLogin;
import com.gxf.gxfframework.common.PriorityHandlerUserProvide;
import com.gxf.gxfframework.interfaces.HSUserBeanHandler;
import com.gxf.gxfframework.interfaces.HSUserNotLoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Xiao Feng
 * 功能描述：用户登录拦截器（标注了UserBean的情况下生效）
 *      不关心如何获取用户信息，只关心委托者返回的用户对象是否为空，松耦合的拦截
 *      提示：所有方法都会被拦截(SpringBoot的错误处理不拦截)，如果有些业务方法不需要拦截。需在方法上标注 {@link IgnoreLogin}
 * @创建时间: 2018/12/29
 */
public class UserBeanHandlerInterceptor implements HandlerInterceptor{

    private static final String SPRING_BOOT_ERROR_CONTROLLER_NAME = BasicErrorController.class.getName();

    private static final String SPRING_BOOT_ERROR_MAPPING_METHOD1 = "errorHtml";

    private static final String SPRING_BOOT_ERROR_MAPPING_METHOD2 = "error";

    @Autowired
    private PriorityHandlerUserProvide priorityHandlerUserProvide;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (this.canEqual(handler)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (!isSpringBootErrorHandler(handlerMethod)) {
                Method method = handlerMethod.getMethod();
                Object userObject = delegatingObtain(request);
                if (null == userObject && !isIgnoreLogin(method)) {
                    delegatingHandler(request, response);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isIgnoreLogin(Method method) {
        return method.isAnnotationPresent(IgnoreLogin.class);
    }

    private boolean isSpringBootErrorHandler(HandlerMethod handlerMethod) {
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBeanType().getName();
        return SPRING_BOOT_ERROR_CONTROLLER_NAME.equals(className) && (SPRING_BOOT_ERROR_MAPPING_METHOD1.equals(methodName)
                    || SPRING_BOOT_ERROR_MAPPING_METHOD2.equals(methodName));
    }

    private void delegatingHandler(HttpServletRequest request, HttpServletResponse response) {
        HSUserNotLoginHandler hsUserNotLoginHandler = priorityHandlerUserProvide.getHighPriorityHandlerNotLoginUser();
        hsUserNotLoginHandler.handlerNotLoginUser(request,response);
    }

    private Object delegatingObtain(HttpServletRequest request) {
        HSUserBeanHandler hsUserBeanHandler = priorityHandlerUserProvide.getHighPriorityHandlerUser();
        return hsUserBeanHandler.get(request);
    }

    private boolean canEqual(Object handler) {
        return handler instanceof HandlerMethod;
    }
}
