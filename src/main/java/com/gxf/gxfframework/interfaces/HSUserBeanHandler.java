package com.gxf.gxfframework.interfaces;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取用户实例
 */
public interface HSUserBeanHandler{

    Object get(HttpServletRequest request);

}
