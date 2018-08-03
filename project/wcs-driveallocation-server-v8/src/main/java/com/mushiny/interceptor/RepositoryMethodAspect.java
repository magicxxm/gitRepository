package com.mushiny.interceptor;

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
public class RepositoryMethodAspect {
    private final static Logger LOGGER =  LoggerFactory.getLogger(RepositoryMethodAspect.class);
    public static final String POINT = "execution(* com.mushiny.wcs.application.repository.*.*(..))";
    private static final long ONE_MINUTE = 10;
    @Around(POINT)
    public Object timeAround(ProceedingJoinPoint joinPoint) {
        // 定义返回对象、得到方法需要的参数
        Object obj = null;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        LOGGER.info("开始执行查询["+methodName+"]");
        try {
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            LOGGER.error("统计某方法执行耗时环绕通知出错", e);
        }
        return obj;
    }
}
