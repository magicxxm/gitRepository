package com.mushiny.wms.application.config;

import com.mushiny.wms.application.domain.InboundInstruct;
import com.mushiny.wms.application.domain.OutboundInstruct;
import com.mushiny.wms.application.domain.Pod;
import com.mushiny.wms.application.domain.enums.InstructStatus;
import com.mushiny.wms.application.repository.InboundInstructRepository;
import com.mushiny.wms.application.repository.OutboundInstructRepository;
import com.mushiny.wms.application.service.EmptyPodCarryService;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.EntityManagerUtil;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;

import java.util.Map;

/**
 * Created by Administrator on 2018/7/9.
 */
@Configuration
public class RestTempConfig  {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTempConfig.class);
    @Value("${midea.webApi.instructIn.callBack}")
    private String instructInCallBack;
    @Value("${midea.webApi.instructOut.callBack}")
    private String instructOutCallBack;

    @Value("${midea.webApi.RFID.callBack}")
    private String rFIDCallBack;
    @Value("${midea.webApi.invMitemLabelSyn}")
    private String invMitemLabelSyn;
    @Value("${midea.webApi.sfcMitemSyn}")
    private String sfcMitemSyn;
    @Value("${mushiny.debug}")
    private Boolean debug=false;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private  EntityManagerUtil entityManagerUtil;

    @Bean
    public RestTemplate createRestTemplate()
    {
        RestTemplateBuilder rb=new RestTemplateBuilder();
        return rb.build();
    }




    public String sfcMitemSyn(Map param){
        LOGGER.debug("请求地址{} 参数{}",sfcMitemSyn,JSONUtil.mapToJSon(param));
        String respose=restTemplate.getForObject(sfcMitemSyn,String.class,param);
        LOGGER.debug("请求地址{} 参数{} 返回{}",sfcMitemSyn,JSONUtil.mapToJSon(param),respose);
        return respose;
    }
    public String invMitemLabelSyn(Map param){
        long begain=System.currentTimeMillis();
        LOGGER.debug("请求地址{} 参数{}",invMitemLabelSyn,JSONUtil.mapToJSon(param));
        String respose=restTemplate.getForObject(invMitemLabelSyn,String.class,param);
        long end =System.currentTimeMillis();
        int time=(int)((end-begain)/1000);
        LOGGER.debug("返回用时 {}",time,invMitemLabelSyn,JSONUtil.mapToJSon(param),respose);
        return respose;
    }
    public Map inBoundBindPodAck(Map param)
    {
        LOGGER.debug("请求地址{} 参数{}",rFIDCallBack,JSONUtil.mapToJSon(param));
        Map respose=null;
        if(!debug)
        {
             respose= restTemplate.getForObject(rFIDCallBack,Map.class,param);
        }else{
            respose=new HashMap();
            respose.put("IsSucess",true);
            respose.put("ErrorCode",0);
            respose.put("ErrorMessage",null);
        }
        LOGGER.debug("请求地址{} 返回{}",rFIDCallBack,JSONUtil.mapToJSon(respose));
        return respose;
    }



    public Map inBoundAck(Map data)
    {

        LOGGER.debug("请求地址{} 参数{}",instructInCallBack,JSONUtil.mapToJSon(data));
        Map respose=null;
        if(!debug)
        {
            respose=restTemplate.getForObject(instructInCallBack,Map.class,data);
        }else{
            respose=new HashMap();
            respose.put("IsSucess",true);
            respose.put("ErrorCode",0);
            respose.put("ErrorMessage",null);
        }

        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("调用入库指令回传接口{} \n参数{} \n返回结果{}",instructInCallBack, JSONUtil.mapToJSon(data),JSONUtil.mapToJSon(respose));
        }

        return respose;
    }

    public Map outBoundAck(Map data)
    {
        LOGGER.debug("请求地址{} 参数{}",instructOutCallBack,JSONUtil.mapToJSon(data));
        Map respose=null;
        if(!debug)
        {
            respose=restTemplate.getForObject(instructOutCallBack,Map.class,data);
        }else{
            respose=new HashMap();
            respose.put("IsSucess",true);
            respose.put("ErrorCode",0);
            respose.put("ErrorMessage",null);
        }
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("调用出库指令回传接口{} \n参数{} \n返回结果{}",instructOutCallBack, JSONUtil.mapToJSon(data),JSONUtil.mapToJSon(respose));
        }
        return respose;
    }





}
