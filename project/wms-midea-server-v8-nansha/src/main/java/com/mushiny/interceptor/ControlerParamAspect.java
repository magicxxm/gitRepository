package com.mushiny.interceptor;


import com.mushiny.wms.common.utils.JSONUtil;
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
public class ControlerParamAspect {
    private final static Logger LOGGER =  LoggerFactory.getLogger(ControlerParamAspect.class);
    public static final String POINT = "execution(* com.mushiny.wms.*.web.*.*(..)))";
    @Around(POINT)
    public Object timeAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 定义返回对象、得到方法需要的参数
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        StringBuffer sb=new StringBuffer();
        for(Object arg:args)
        {
            sb.append(arg);
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        LOGGER.info("web请求,方法名{} 参数{}",methodName,sb.toString());
        try {
            obj = joinPoint.proceed(args);
        }
        catch (Throwable e)
        {
            LOGGER.error(e.getMessage(),e);

            throw e;
        }
        // 获取执行的方法名
        LOGGER.info("web请求,方法名{} 返回{}",methodName, JSONUtil.toJSon(obj));

        return obj;
    }



}
