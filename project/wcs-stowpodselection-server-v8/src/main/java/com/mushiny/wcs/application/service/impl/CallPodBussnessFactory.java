package com.mushiny.wcs.application.service.impl;

import com.mushiny.wcs.application.business.callPods.CallPodBusiness;
import com.mushiny.wcs.application.business.enums.StationType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/10/12.
 */
@Component
public class CallPodBussnessFactory  implements ApplicationContextAware {
    private static Map<StationType,CallPodBusiness> callPodBusiness;
    private static org.springframework.context.ApplicationContext ApplicationContext;
    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        this.ApplicationContext=applicationContext;


        Map<String,CallPodBusiness> temp=applicationContext.getBeansOfType(CallPodBusiness.class);
        callPodBusiness=new HashMap<>();
        temp.forEach((key,value)->callPodBusiness.put(value.getStationType(),value));
    }
    public static <T extends CallPodBusiness> T getCallPodBusiness(StationType stationType){
        return (T)callPodBusiness.get(stationType);
    }
}
