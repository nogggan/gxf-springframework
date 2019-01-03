package com.gxf.gxfframework.config;

import com.gxf.gxfframework.interceptor.UserBeanHandlerInterceptor;
import com.gxf.gxfframework.resolver.UserBeanHandlerMethodArgumentResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Xiao Feng
 * 功能描述：根据情况注册解析器
 * @创建时间: 2018/12/29
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(WebConfig.class)
public class HandlerConfig implements WebMvcConfigurer{

    private UserBeanHandlerMethodArgumentResolver userBeanHandlerMethodArgumentResolver;

    private UserBeanHandlerInterceptor userBeanHandlerInterceptor;

    public HandlerConfig(ObjectProvider<UserBeanHandlerMethodArgumentResolver> userBeanHandlerMethodArgumentResolverObjectProvider,
                         ObjectProvider<UserBeanHandlerInterceptor> userBeanHandlerInterceptorObjectProvider){
        userBeanHandlerMethodArgumentResolver = userBeanHandlerMethodArgumentResolverObjectProvider.getIfAvailable();
        userBeanHandlerInterceptor = userBeanHandlerInterceptorObjectProvider.getIfAvailable();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        if(userBeanHandlerMethodArgumentResolver!=null){
            resolvers.add(userBeanHandlerMethodArgumentResolver);
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if(userBeanHandlerInterceptor!=null){
            registry.addInterceptor(userBeanHandlerInterceptor);
        }
    }

}
