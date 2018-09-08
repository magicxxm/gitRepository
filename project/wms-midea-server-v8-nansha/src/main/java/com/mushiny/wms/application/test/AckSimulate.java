package com.mushiny.wms.application.test;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2018/7/26.
 */
@Component
public class AckSimulate {

    public Map ceateParam(String instructId,String tripType,String instructStatus ,String podNames )
    {
        char[] podName=new char[]{'P','0','0','0','0','0','0','0'};

        int podIndex=new Random().nextInt(1000);
        char[] podtemp = (podIndex + "").toCharArray();
        int beagin = podName.length;
        int charLen = podtemp.length;
        for (char t : podtemp) {
            podName[beagin - charLen] = t;
            beagin++;
        }
        Map result=new HashMap();
        result.put("instructId",instructId);
        result.put("tripType",tripType);
        result.put("instructStatus",instructStatus);
        result.put("driverId",new Random().nextInt(100));
        result.put("targetAddress",new Random().nextInt(1000));
        result.put("sourceAddress",new Random().nextInt(1000));
        if(StringUtils.isEmpty(podNames))
        {
            result.put("podIndex",new String(podName));
        }else{
            result.put("podIndex",podNames);
        }

        return result;
    }
}
