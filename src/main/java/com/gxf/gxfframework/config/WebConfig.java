package com.gxf.gxfframework.config;

import com.gxf.gxfframework.common.PriorityHandlerUserProvide;
import com.gxf.gxfframework.exception.UserNotLoginException;
import com.gxf.gxfframework.interceptor.UserBeanHandlerInterceptor;
import com.gxf.gxfframework.interfaces.HSUserBeanHandler;
import com.gxf.gxfframework.interfaces.HSUserNotLoginHandler;
import com.gxf.gxfframework.resolver.MediaTypeService;
import com.gxf.gxfframework.resolver.UserBeanHandlerMethodArgumentResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xiao Feng
 * 功能描述：自动配置
 * @创建时间: 2018/12/28
 */
@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE-1)
@ConditionalOnWebApplication
public class WebConfig implements ApplicationContextAware{

    private AnnotationConfigServletWebServerApplicationContext applicationContext;

    /**
     * jacksonView解析，ModelAndView使用此View视图，可以返回json格式
     * @return MappingJackson2JsonView
     */
    @Bean
    @ConditionalOnMissingBean(MappingJackson2JsonView.class)
    public MappingJackson2JsonView mappingJackson2JsonView(){
        return new MappingJackson2JsonView();
    }

    /**
     * 默认用户提供者处理类
     * @return HSUserBeanHandler
     */
    @Bean
    @ConditionalOnMissingBean(HSUserBeanHandler.class)
    @ConditionalOnBean(name = "hs-user-bean")
    public HSUserBeanHandler hsUserBeanHandler(){
        BeanDefinition beanDefinition = applicationContext.getBeanDefinition("hs-user-bean");
        return request->{
            return request.getSession().getAttribute(beanDefinition.getAttribute("hs-user-key").toString());
        };
    }

    /**
     * 默认的用户不存在处理类 默认策略 ：抛出异常
     * @return HSUserNotLoginHandler
     */
    @Bean
    @ConditionalOnMissingBean(HSUserNotLoginHandler.class)
    @ConditionalOnBean(name = "hs-user-bean")
    public HSUserNotLoginHandler hsUserNotLoginHandler(){
        return (request,response)->{
            throw new UserNotLoginException();
        };
    }

    /**
     * 用户不存在的处理拦截器
     * @return UserBeanHandlerInterceptor
     */
    @Bean
    @ConditionalOnBean(name = "hs-user-bean")
    public UserBeanHandlerInterceptor userBeanHandlerInterceptor(){
        return new UserBeanHandlerInterceptor();
    }

    @Bean
    @ConditionalOnBean(name = "hs-user-bean")
    public PriorityHandlerUserProvide priorityHandlerUserProvide(ObjectProvider<HSUserBeanHandler> hsUserBeanHandlerObjectProvider,
                                                                 ObjectProvider<HSUserNotLoginHandler> hsUserNotLoginHandlerObjectProvider){
        List<HSUserBeanHandler> hsUserBeanHandlers = new ArrayList<>();
        List<HSUserNotLoginHandler> hsUserNotLoginHandlers = new ArrayList<>();
        hsUserBeanHandlerObjectProvider.ifAvailable(hsUserBeanHandlers::add);
        hsUserNotLoginHandlerObjectProvider.ifAvailable(hsUserNotLoginHandlers::add);
        AnnotationAwareOrderComparator.sort(hsUserBeanHandlers);
        AnnotationAwareOrderComparator.sort(hsUserNotLoginHandlers);
        return new PriorityHandlerUserProvide(hsUserBeanHandlers,hsUserNotLoginHandlers);
    }

    @Bean
    @ConditionalOnBean(name = "hs-user-bean")
    public UserBeanHandlerMethodArgumentResolver userBeanHandlerMethodArgumentResolver(){
        Object bean = applicationContext.getBean("hs-user-bean");
        return new UserBeanHandlerMethodArgumentResolver(bean.getClass());
    }

    @Bean
    public MediaTypeService mediaTypeService(ObjectProvider<ContentNegotiationManager> negotiationManagerObjectProvider){
        return new MediaTypeService(negotiationManagerObjectProvider.getIfAvailable());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (AnnotationConfigServletWebServerApplicationContext) applicationContext;
    }

}
