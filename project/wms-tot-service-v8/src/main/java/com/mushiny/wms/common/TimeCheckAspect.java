package com.mushiny.wms.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * @author:
 * @Description: Created by wjw on 2017/8/20.
 */

@Aspect
@Component
public class TimeCheckAspect {
    private final static Logger LOGGER =  LoggerFactory.getLogger(TimeCheckAspect.class);
    public static final String POINT = "execution(* com.mushiny.wms.*.*.web.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)";
    private static final long ONE_MINUTE = 100;
    @Around(POINT)
    public Object timeAround(ProceedingJoinPoint joinPoint) {
        // 定义返回对象、得到方法需要的参数
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();

        try {
            obj = joinPoint.proceed(args);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Throwable e) {
            LOGGER.error("统计某方法执行耗时环绕通知出错", e);
        }

        // 获取执行的方法名
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();

        // 打印耗时的信息
        this.printExecTime(methodName, startTime, endTime);

        return obj;
    }

    /**
     * 打印方法执行耗时的信息，如果超过了一定的时间，才打印
     * @param methodName
     * @param startTime
     * @param endTime
     */
    private void printExecTime(String methodName, long startTime, long endTime) {
        long diffTime = endTime - startTime;

        if (diffTime > ONE_MINUTE) {
            LOGGER.warn("-----" + methodName + " 方法执行耗时："  +diffTime +" ms");
        }
    }

}
