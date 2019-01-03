package com.gxf.gxfframework.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户未登录的处理 容器提供默认实现，如果需要自己处理，实现该接口，导入容器即可
 */
public interface HSUserNotLoginHandler {

    void handlerNotLoginUser(HttpServletRequest request, HttpServletResponse response);

}
