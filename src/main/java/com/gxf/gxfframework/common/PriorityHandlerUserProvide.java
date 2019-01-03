package com.gxf.gxfframework.common;

import com.gxf.gxfframework.interfaces.HSUserBeanHandler;
import com.gxf.gxfframework.interfaces.HSUserNotLoginHandler;
import java.util.List;

/**
 * @author Xiao Feng
 * 提供委托处理获取用户或处理用户不存在情况的提供类（即使容器存在多个处理实例，会获取最高优先级的委托类）
 * @创建时间: 2019/01/01
 */
public class PriorityHandlerUserProvide{

    private List<HSUserBeanHandler> hsUserBeanHandlers;

    private List<HSUserNotLoginHandler> hsUserNotLoginHandlers;

    public PriorityHandlerUserProvide(List<HSUserBeanHandler> hsUserBeanHandlers,List<HSUserNotLoginHandler> hsUserNotLoginHandlers){
        this.hsUserBeanHandlers = hsUserBeanHandlers;
        this.hsUserNotLoginHandlers = hsUserNotLoginHandlers;
    }

    public HSUserBeanHandler getHighPriorityHandlerUser(){
        return hsUserBeanHandlers.get(0);
    }

    public HSUserNotLoginHandler getHighPriorityHandlerNotLoginUser(){
        return hsUserNotLoginHandlers.get(0);
    }

}
