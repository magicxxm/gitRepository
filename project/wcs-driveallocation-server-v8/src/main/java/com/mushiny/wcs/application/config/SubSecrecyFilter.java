package com.mushiny.wcs.application.config;

import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by Administrator on 2018/5/2.
 */
public abstract  class SubSecrecyFilter {
    public static Object dofilter(MethodInvocation Invocation) throws Throwable{
        System.out.println("将要执行方法："+Invocation.getMethod().getName());
        Object obj=Invocation.proceed();
        System.out.println(Invocation.getMethod().getName()+"已经被执行");
        return obj;
    }
}
