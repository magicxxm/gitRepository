package com.mushiny.wms.config;

import com.mushiny.wms.common.exception.BaseException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeCheckAspect {
    private final static Logger log =  LoggerFactory.getLogger(TimeCheckAspect.class);
    public static final String POINT = "execution(* com.mushiny.wms.internaltool.web.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)";
    private static final long ONE_MINUTE = 10;
    @Around(POINT)
    public Object timeAround(ProceedingJoinPoint joinPoint) {
        // 定义返回对象、得到方法需要的参数
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();

        try {
            obj = joinPoint.proceed(args);
        } catch (RuntimeException e) {
            throw e;
        }catch (Throwable e){
            log.error(e.getMessage());
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
            log.warn("-----" + methodName + " 方法执行耗时："  +diffTime +" ms");
        }
    }
}
