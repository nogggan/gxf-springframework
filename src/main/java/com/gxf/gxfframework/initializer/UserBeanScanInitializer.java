package com.gxf.gxfframework.initializer;

import com.gxf.gxfframework.annotaion.UserBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Xiao Feng
 * 功能描述：扫描配置类上面是否标注UserBean注解，@SpringBootConfiguration上标注的优先级比@Configuration高
 * @创建时间: 2018/12/28
 * {@link com.gxf.gxfframework.annotaion.UserBean} 扫描
 */
public class UserBeanScanInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

    private static final String CLASS_SCAN_PATH = "classpath:/**/*.class";

    private static final String INDEX_OF_KEY = "classes";

    private static final String LAST_OF_KEY = ".";

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        List<Class<?>> candidateClassesList = scanClassPath();
        if(null!=candidateClassesList && !candidateClassesList.isEmpty()){
            registerBeanDefinition(beanFactory, candidateClassesList);
        }
    }

    /**
     * 扫描类路径，获取标注了UserBean注解的类
     * @return  List<Class<?>>
     */
    private List<Class<?>> scanClassPath() {
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = resourcePatternResolver.getResources(CLASS_SCAN_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(null!=resources && resources.length>0){
            return getCandidateClass(resources);
        }
        return null;
    }


    /**
     * 注册bean
     * @param beanFactory beanFactory
     * @param candidateClassesList 候选的class集合
     */
    private void registerBeanDefinition(DefaultListableBeanFactory beanFactory, List<Class<?>> candidateClassesList) {
        Class<?> highPriorityClass = getHighPriorityClass(candidateClassesList);
        if(null!=highPriorityClass){
            UserBean annotation = highPriorityClass.getAnnotation(UserBean.class);
            RootBeanDefinition beanDefinition = new RootBeanDefinition();
            beanDefinition.setScope("singleton");
            beanDefinition.setBeanClass(annotation.userClass());
            beanDefinition.setAttribute("hs-user-key",annotation.userKey());
            beanFactory.registerBeanDefinition("hs-user-bean",beanDefinition);
        }
    }

    /**
     * 获取优先级最高的UserBean注解
     * @param candidateClassesList 候选的class集合
     * @return Class<?>
     */
    private Class<?> getHighPriorityClass(List<Class<?>> candidateClassesList) {
        int size = candidateClassesList.size();
        int firstConfigurationIndex = 0;
        boolean isFirst = true;
        for(int i=0;i<size;i++){
            Class<?> aClass = candidateClassesList.get(i);
            if(aClass.isAnnotationPresent(SpringBootApplication.class)){
                return aClass;
            }
            if(isFirst){
                firstConfigurationIndex = i;
                isFirst = false;
            }
        }
        return candidateClassesList.get(firstConfigurationIndex);
    }

    /**
     * 获取候选的class
     * @param resources 资源
     * @return List<Class<?>>
     */
    private List<Class<?>> getCandidateClass(Resource[] resources){
        List<Class<?>> candidateClasses = new ArrayList<>();
        if(null!=resources && resources.length>0){
            Stream.of(resources).filter(UserBeanScanInitializer::isUserBeanAnnotationPresent).forEach(resource -> {
                try {
                    URL url = resource.getURL();
                    String classUrl = parseClassUrl(url);
                    Class<?> aClass = UserBeanScanInitializer.class.getClassLoader().loadClass(classUrl);
                    candidateClasses.add(aClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return candidateClasses;
    }

    /**
     * 判断配置类是否标注 {@link com.gxf.gxfframework.annotaion.UserBean}注解
     * @param resource 资源
     * @return boolean
     */
    private static boolean isUserBeanAnnotationPresent(Resource resource){
        URL url = null;
        try {
            url = resource.getURL();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(null!=url){
            String urlStr = parseClassUrl(url);
            try {
                Class<?> aClass = UserBeanScanInitializer.class.getClassLoader().loadClass(urlStr);
                if((aClass.isAnnotationPresent(SpringBootApplication.class) || aClass.isAnnotationPresent(Configuration.class))
                        && (aClass.isAnnotationPresent(UserBean.class)))
                    return true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    /**
     * 解析url
     * @param url url
     * @return String
     */
    private static String parseClassUrl(URL url) {
        String urlStr = url.toString();
        int startIndex = urlStr.indexOf(INDEX_OF_KEY);
        int lastIndex = urlStr.lastIndexOf(LAST_OF_KEY);
        startIndex = startIndex+INDEX_OF_KEY.length()+1;
        urlStr = urlStr.substring(startIndex,lastIndex).replaceAll("/",LAST_OF_KEY);
        return urlStr;
    }


}
