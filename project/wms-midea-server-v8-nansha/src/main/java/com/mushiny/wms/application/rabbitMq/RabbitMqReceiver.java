package com.mushiny.wms.application.rabbitMq;

import com.mushiny.wms.application.config.RestTempConfig;
import com.mushiny.wms.application.domain.*;
import com.mushiny.wms.application.domain.enums.InstructStatus;
import com.mushiny.wms.application.domain.enums.TripType;
import com.mushiny.wms.application.repository.*;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Laptop-8 on 2017/7/19.
 */

@Component
public class RabbitMqReceiver {
    private static Logger LOGGER = LoggerFactory.getLogger(RabbitMqReceiver.class);
    @Autowired
    private  RestTempConfig restTempConfig;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private MdStationnodepositionRepository mdStationnodepositionRepository;
    @Autowired
    private InboundInstructRepository inboundInstructRepository;
    @Autowired
    private OutboundInstructRepository outboundInstructRepository;
    @Autowired
    private WmsInstructInPositionRepository wmsInstructInPositionRepository;
    @Autowired
    private WmsInstructOutPositionRepository wmsInstructOutPositionRepository;
    @Autowired
    private PodRepository podRepository;
   // @RabbitListener(queues = QueueConfig.InstructAckQueue)
 /* @Scheduled(fixedDelay=4000,initialDelay = 10000)*/
    public void test(Map param){
        Map message=new HashMap();
        String tripType= ""+param.get("tripType");
        String tripId=""+param.get("tripId");
        message.put("tripType",tripType);
        message.put("tripId",tripId);
        if(TripType.ANNTO_MVIN.getName().equalsIgnoreCase(tripType)||TripType.FINISHESWAREHOUSING.getName().equalsIgnoreCase(tripType))
        {
            message.put("driverId",""+param.get("driverId"));
            message.put("instructStatus",""+param.get("instructStatus"));
            message.put("targetAddress",""+param.get("targetAddress"));
        }else if(TripType.ANNTOMVOUT.getName().equalsIgnoreCase(tripType)||TripType.LMGETMATERIAL.getName().equalsIgnoreCase(tripType)) {
            message.put("driverId",""+param.get("driverId"));
            message.put("instructStatus",""+param.get("instructStatus"));
            message.put("sourceAddress",""+param.get("sourceAddress"));
            message.put("podIndex",""+param.get("podIndex"));


        }else if(TripType.CARRY_POD.getName().equalsIgnoreCase(tripType))
        {
            message.put("driverId",""+param.get("driverId"));
            message.put("podIndex",""+param.get("podIndex"));
        }
        receiveMapMessage(message);
    }
    public Map createInstructCallBackParam(String tripType,Object instruct,Map addParam){
        Map param=new HashMap();

        if(TripType.ANNTO_MVIN.getName().equalsIgnoreCase(tripType)||TripType.FINISHESWAREHOUSING.getName().equalsIgnoreCase(tripType))
        {
            InboundInstruct inboundInstruct=(InboundInstruct)instruct;
            param.put("ID",inboundInstruct.getMES_ID());
            param.put("INV_ORG_ID",inboundInstruct.getINV_ORG_ID());
            param.put("BILL_TYPE",inboundInstruct.getBILL_TYPE());
            param.put("BILL_NO",inboundInstruct.getBILL_NO());
            param.put("LABEL_NO",inboundInstruct.getLABEL_NO());
            param.put("INV_CODE", inboundInstruct.getINV_CODE());
            param.put("LOC_CODE",addParam.get("targetAddress"));
            param.put("CAR_NO",addParam.get("driverId"));

            param.put("DATETIME_STOCK",addParam.get("datetimeStock"));
            param.put("STATUS",addParam.get("instructStatus") );
        }else if(TripType.ANNTOMVOUT.getName().equalsIgnoreCase(tripType)||TripType.LMGETMATERIAL.getName().equalsIgnoreCase(tripType))
        {
            OutboundInstruct outboundInstruct=(OutboundInstruct)instruct;

            param.put("ID",outboundInstruct.getMES_ID());
            param.put("INV_ORG_ID",outboundInstruct.getINV_ORG_ID());
            param.put("BILL_TYPE",outboundInstruct.getBILL_TYPE());
            param.put("BILL_NO",outboundInstruct.getBILL_NO());
            param.put("LABEL_NO",outboundInstruct.getLABEL_NO());
            param.put("INV_CODE", outboundInstruct.getINV_CODE());
            param.put("STOCK_QTY",outboundInstruct.getQTY());
            param.put("STORAGE_NO_L",addParam.get("podName"));
            param.put("CAR_NO",addParam.get("driverId"));
            param.put("LOC_CODE",addParam.get("sourceAddress"));

            param.put("DATETIME_STOCK",addParam.get("datetimeStock") );
            param.put("STATUS",addParam.get("instructStatus") );
        }else{

        }

        return param;
    }

   @RabbitListener(queues = QueueConfig.InstructAckQueue)
    public void receiveMapMessage(Map message) {
        try{
            LOGGER.info("工作站推送过来是消息:" + JSONUtil.toJSon(message));

            String tripId=message.get("tripId")+"";
            String tripType=message.get("tripType")+"";
            Trip trip=tripRepository.getTripByID(tripId);
            MdStationnodeposition position=mdStationnodepositionRepository.getMdStationnodepositionById(trip.getMdNodePosition());
            TripType tt=TripType.parserTripType(tripType);

            if(tt==TripType.ANNTO_MVIN||tt==TripType.FINISHESWAREHOUSING)
            {
                String instructStatus=message.get("instructStatus")+"";

                String driverId=message.get("driverId")+"";
                String targetAddress=message.get("targetAddress")+"";

                InboundInstruct inboundInstruct=inboundInstructRepository.getInstruByTripId(tripId);
                if(!ObjectUtils.isEmpty(inboundInstruct))
                {
                    Map param=new HashMap();
                    param.put("targetAddress",targetAddress);
                    param.put("driverId",driverId);

                    param.put("instructStatus",instructStatus);
                    String datetimeStock= DateTimeUtil.getDateFormat(new Date(),"yyyy-MM-dd HH:mm:ss");
                    param.put("datetimeStock",datetimeStock);

                   Map callParam= createInstructCallBackParam(tripType,inboundInstruct,param);
                    restTempConfig.inBoundAck(callParam);
                    WmsInstructInPosition iip=new WmsInstructInPosition();
                    iip.setInboundInstruct(inboundInstruct);
                    iip.setCAR_NO(driverId);
                    iip.setDATETIME_STOCK(DateTimeUtil.strToTimeStamp(datetimeStock));
                    iip.setLOC_CODE(targetAddress);
                    iip.setSTATUS(instructStatus);
                    iip=wmsInstructInPositionRepository.saveAndFlush(iip);
                    if(LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug("保存入库指令状态成功\n{}",JSONUtil.toJSon(iip));
                    }
                }else{
                    LOGGER.error("未找到trip {} 对应的指令",tripId);
                }

            }
            else if(tt==TripType.ANNTOMVOUT||tt==TripType.LMGETMATERIAL)
            {
                String instructStatus=message.get("instructStatus")+"";
                String driverId=""+message.get("driverId");
                String sourceAddress=""+message.get("sourceAddress");
                String podName=podRepository.getPodById(message.get("podIndex")+"").getName();
                OutboundInstruct outboundInstruct=outboundInstructRepository.getInstruByTripId(tripId);
                if(!ObjectUtils.isEmpty(outboundInstruct))
                {
                    Map  param=new HashMap();
                    String datetimeStock= DateTimeUtil.getDateFormat(new Date(),"yyyy-MM-dd HH:mm:ss");
                    param.put("podName",podName);
                    param.put("driverId",driverId);
                    param.put("sourceAddress",sourceAddress);

                    param.put("datetimeStock",datetimeStock);
                    param.put("instructStatus",instructStatus);
                    Map callParam= createInstructCallBackParam(tripType,outboundInstruct,param);
                    restTempConfig.outBoundAck(callParam);
                    WmsInstructOutPosition iop=new WmsInstructOutPosition();
                    iop.setCAR_NO(driverId);
                    iop.setDATETIME_STOCK(DateTimeUtil.strToTimeStamp(datetimeStock));
                    iop.setOutboundInstruct(outboundInstruct);
                    iop.setSTATUS(instructStatus);
                    iop.setSTOCK_QTY(outboundInstruct.getQTY().toString());
                    iop.setLOC_CODE(sourceAddress);
                    iop=wmsInstructOutPositionRepository.saveAndFlush(iop);
                    if(LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug("保存出库指令状态成功\n{}",JSONUtil.toJSon(iop));
                    }

                }else{
                    LOGGER.error("未找到trip {} 对应的指令",tripId);
                }

            }else if(tt==TripType.CARRY_POD)
            {
                Map reposeParam=new HashMap();
                reposeParam.put("INV_ORG_ID","200124");
                reposeParam.put("WORKCENTER_CODE",position.getStationnode().getName());
                Pod pod=podRepository.getPodById(message.get("podIndex")+"");

                if("left".equalsIgnoreCase(position.getDirection()))
                {
                    reposeParam.put("STORAGE_NO_L",pod.getName());
                    reposeParam.put("STORAGE_NO_R", "");
                    reposeParam.put("DATETIME_UPDATED", DateTimeUtil.getDateFormat(new Date(),"yyyy-MM-dd HH:mm:ss"));
                }else{
                    reposeParam.put("STORAGE_NO_R",pod.getName());
                    reposeParam.put("STORAGE_NO_L", "");
                    reposeParam.put("DATETIME_UPDATED", DateTimeUtil.getDateFormat(new Date(),"yyyy-MM-dd HH:mm:ss"));
                }
                restTempConfig.inBoundBindPodAck(reposeParam);
            }
        }catch (Exception e)
        {
            LOGGER.error(e.getMessage(),e);
        }



    }


    public void receiveMapMessage2(Map message) {
        try{
            LOGGER.info("模拟测试推送过来是消息:" + JSONUtil.toJSon(message));

            String instructId=message.get("instructId")+"";
            String tripType=message.get("tripType")+"";
            TripType tt=TripType.parserTripType(tripType);

            if(tt==TripType.ANNTO_MVIN||tt==TripType.FINISHESWAREHOUSING)
            {
                String instructStatus=message.get("instructStatus")+"";

                String driverId=message.get("driverId")+"";
                String targetAddress=message.get("targetAddress")+"";

                InboundInstruct inboundInstruct=inboundInstructRepository.getInstruById(instructId);
                if(!ObjectUtils.isEmpty(inboundInstruct))
                {
                    Map param=new HashMap();
                    param.put("targetAddress",targetAddress);
                    param.put("driverId",driverId);

                    param.put("instructStatus",instructStatus);
                    String datetimeStock= DateTimeUtil.getDateFormat(new Date(),"yyyy-MM-dd HH:mm:ss");
                    param.put("datetimeStock",datetimeStock);

                    Map callParam= createInstructCallBackParam(tripType,inboundInstruct,param);
                    restTempConfig.inBoundAck(callParam);
                    WmsInstructInPosition iip=new WmsInstructInPosition();
                    iip.setInboundInstruct(inboundInstruct);
                    iip.setCAR_NO(driverId);
                    iip.setDATETIME_STOCK(DateTimeUtil.strToTimeStamp(datetimeStock));
                    iip.setLOC_CODE(targetAddress);
                    iip.setSTATUS(instructStatus);
                    iip=wmsInstructInPositionRepository.saveAndFlush(iip);
                    if(LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug("保存入库指令状态成功\n{}",JSONUtil.toJSon(iip));
                    }
                }else{
                    LOGGER.error("未找到trip {} 对应的指令",instructId);
                }

            }
            else if(tt==TripType.ANNTOMVOUT||tt==TripType.LMGETMATERIAL)
            {
                String instructStatus=message.get("instructStatus")+"";
                String driverId=""+message.get("driverId");
                String sourceAddress=""+message.get("sourceAddress");
                String podName=message.get("podIndex")+"";
                OutboundInstruct outboundInstruct=outboundInstructRepository.getInstruByInstructId(instructId);
                if(!ObjectUtils.isEmpty(outboundInstruct))
                {
                    Map  param=new HashMap();
                    String datetimeStock= DateTimeUtil.getDateFormat(new Date(),"yyyy-MM-dd HH:mm:ss");
                    param.put("podName",podName);
                    param.put("driverId",driverId);
                    param.put("sourceAddress",sourceAddress);

                    param.put("datetimeStock",datetimeStock);
                    param.put("instructStatus",instructStatus);
                    Map callParam= createInstructCallBackParam(tripType,outboundInstruct,param);
                    restTempConfig.outBoundAck(callParam);
                    WmsInstructOutPosition iop=new WmsInstructOutPosition();
                    iop.setCAR_NO(driverId);
                    iop.setDATETIME_STOCK(DateTimeUtil.strToTimeStamp(datetimeStock));
                    iop.setOutboundInstruct(outboundInstruct);
                    iop.setSTATUS(instructStatus);
                    iop.setSTOCK_QTY(outboundInstruct.getQTY().toString());
                    iop=wmsInstructOutPositionRepository.saveAndFlush(iop);
                    if(LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug("保存出库指令状态成功\n{}",JSONUtil.toJSon(iop));
                    }

                }else{
                    LOGGER.error("未找到trip {} 对应的指令",instructId);
                }

            }
        }catch (Exception e)
        {
            LOGGER.error(e.getMessage(),e);
        }

    }



}
