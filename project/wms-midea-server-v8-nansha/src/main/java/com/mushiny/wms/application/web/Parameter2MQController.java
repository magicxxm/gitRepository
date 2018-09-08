package com.mushiny.wms.application.web;

import com.mushiny.wms.application.config.RestTempConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by harbor Kang on 2018/8/7.
 */
@RestController
@RequestMapping(value = "/nansha/parameter")
public class Parameter2MQController {
    @Autowired
    private RestTempConfig restTempConfig;

    @PostMapping(value = "/ADK-MES-03")
    @ResponseBody
    //自制件入库信息回传
    public Map mes_ads_03(@RequestBody Map param) {
        System.out.println("Parameter2MQController.ads_mes_03");
        Map map = restTempConfig.inBoundAck(param);
        System.out.println("03result:"+map);
        return map;
    }

    @PostMapping(value = "/ADK-MES-04")
    @ResponseBody
    //自制件出库指令回传
    public Map mes_ads_04(@RequestBody Map param) {
        System.out.println("Parameter2MQController.ads_mes_04");
        return restTempConfig.outBoundAck(param);
    }

    @PostMapping(value = "/ADK-MES-05")
    @ResponseBody
    //工站RFID信号回传
    public Map mes_ads_05(@RequestBody Map param) {
        System.out.println("Parameter2MQController.ads_mes_05");
        return restTempConfig.inBoundBindPodAck(param);
    }
}
