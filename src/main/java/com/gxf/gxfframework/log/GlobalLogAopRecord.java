package com.gxf.gxfframework.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StopWatch;

/**
 * @author Xiao Feng
 * @创建时间: 2019/01/02
 */
@Aspect
@Slf4j
public class GlobalLogAopRecord {

    @Pointcut("(@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping))")
    public void controllerPointCut(){}

    @Pointcut("@annotation(com.gxf.gxfframework.annotaion.LogRecord)")
    public void servicePointcut(){}

    @Around("(controllerPointCut() || servicePointcut())")
    public Object logRecord(ProceedingJoinPoint joinPoint){
        Object result = null;
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Object[] args = joinPoint.getArgs();
        StopWatch stopWatch = new StopWatch("【类】"+targetName+"--【方法】"+methodName);
        stopWatch.start();
        LogInfo logInfo = new LogInfo(targetName,methodName,args);
        try {
            result = joinPoint.proceed();
            stopWatch.stop();
        } catch (Throwable throwable) {
            logInfo.setError(true);
            stopWatch.stop();
            log.error(this.prettyPrint(methodSignature,logInfo,stopWatch));
            throwable.printStackTrace();
        }
        if(!logInfo.isError())
            log.info(this.prettyPrint(methodSignature,logInfo,stopWatch));
        return result;
    }

    private String prettyPrint(MethodSignature methodSignature,LogInfo logInfo,StopWatch stopWatch){
        StringBuilder sb = new StringBuilder(this.summary(stopWatch));
        sb.append("\n");
        if(!logInfo.isError())
            sb.append("正常执行\n");
        else
            sb.append("发生异常\n");
        sb.append("-----------------------------------------------------\n");
        sb.append("【所在类】    ").append(logInfo.getClassName()).append("\n");
        sb.append("-----------------------------------------------------\n");
        sb.append("【执行方法】   ").append(String.format("%s(%s)", logInfo.getMethodName(), getParamDeclared(methodSignature))).append("\n");
        sb.append("-----------------------------------------------------\n");
        sb.append("【参数值】    ").append(String.format("[%s]", this.serializerArgs(logInfo.getArgs()))).append("\n");
        sb.append("-----------------------------------------------------\n");
        sb.append("【执行时间】   ").append(stopWatch.getTotalTimeSeconds()).append("\n");
        sb.append("-----------------------------------------------------\n");
        sb.append("记录完成\n");
        return sb.toString();
    }

    private String summary(StopWatch stopWatch) {
        return stopWatch.shortSummary();
    }

    private String serializerArgs(Object[] args){
        StringBuilder sb = new StringBuilder();
        int size = args.length;
        for(int i=0;i<size;i++){
            if(i!=0)
                sb.append(",");
            sb.append(args[i]);
        }
        return sb.toString();
    }

    private String getParamDeclared(MethodSignature methodSignature) {
        String[] parameterNames = methodSignature.getParameterNames();
        Class[] parameterTypes = methodSignature.getParameterTypes();
        StringBuilder stringBuilder = new StringBuilder();
        final int size = parameterNames.length;
        for(int i=0;i<size;i++){
            if(i!=0)
                stringBuilder.append(",");
            Class<?> paramType = parameterTypes[i];
            stringBuilder.append(paramType.getSimpleName()).append(" ").append(parameterNames[i]);
        }
        return stringBuilder.toString();
    }

}
