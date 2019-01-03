package com.gxf.gxfframework.log;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Xiao Feng
 * @创建时间: 2019/01/02
 */
public class LogInfo {

    private String className;

    private String methodName;

    private boolean isError = false;

    private Object[] args;

    public LogInfo() {
    }

    public LogInfo(String className, String methodName,Object[] args) {
        this.className = className;
        this.methodName = methodName;
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}