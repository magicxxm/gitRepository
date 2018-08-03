package com.mushiny.wcs.application.business.rabbitMq;

import com.mushiny.wcs.application.service.CommonService;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Created by Laptop-8 on 2017/7/19.
 */

@Component
public class RabbitMqReceiver {
    private static Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);
    private final CommonService commonServiceImpl;
    @Autowired
    public RabbitMqReceiver(CommonService commonService){
        this.commonServiceImpl=commonService;

    }
    @RabbitListener(queues = "WMS.ICQA.POD.QUEUE")
    public void receiveMapMessage(String message) {
        logger.info("工作站推送过来是消息:" + message);
        try{
            Map<String,Object> datas= JSONUtil.jsonToMap(message);
            commonServiceImpl.execute(datas);
        }catch (Exception e)
        {
            logger.error(e.getMessage(),e);
        }



    }



}
