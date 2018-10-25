package com.mushiny.wms.application.service.impl;

import com.mushiny.wms.application.business.common.BuildEntityBusiness;
import com.mushiny.wms.application.business.common.PodReserveUtil;
import com.mushiny.wms.application.business.common.StationNodeBusiness;
import com.mushiny.wms.application.business.common.SystemPropertyBusiness;
import com.mushiny.wms.application.config.RestTempConfig;
import com.mushiny.wms.application.domain.*;
import com.mushiny.wms.application.domain.enums.InstructStatus;
import com.mushiny.wms.application.domain.enums.StationType;
import com.mushiny.wms.application.domain.enums.TripState;
import com.mushiny.wms.application.domain.enums.TripType;
import com.mushiny.wms.application.rabbitMq.RabbitMqReceiver;
import com.mushiny.wms.application.redis.RedisUtil;
import com.mushiny.wms.application.repository.*;
import com.mushiny.wms.application.service.InboundTripService;
import com.mushiny.wms.application.test.AckSimulate;
import com.mushiny.wms.common.utils.DateTimeUtil;

import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/6.
 */

@Service

public class InboundTripServiceImpl implements InboundTripService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InboundTripServiceImpl.class);
    @Value("${mushiny.test}")
    private boolean test=false;
    @Autowired
    private AckSimulate ackSimulate;
    @Autowired
    private RabbitMqReceiver rabbitMqReceiver;
    private final SectionRepository sectionRepository;
    private final WorkStationRepository workStationRepository;
    private final InboundInstructRepository inboundInstructRepository;
    private final SystemPropertyBusiness systemPropertyBusiness;
    private final TripRepository tripRepository;
    private final PodRepository podRepository;
    private final BuildEntityBusiness buildEntityBusiness;
    private PodReserveUtil podReserveUtil;
    private final WmsInvUnitLoadRepository wmsInvUnitLoadRepository;
    private final  SfcMitemRepository sfcMitemRepository;
    private final   WmsInvStockunitRepository wmsInvStockunitRepository;
    private final  MdStationnodepositionRepository
            mdStationnodepositionRepository;
    private final RedisUtil redisUtil;
    private final Object lock=new Object();
    @Autowired
    private StationNodeBusiness stationNodeBusiness;
    @Autowired
    private WmsInstructInPositionRepository wmsInstructInPositionRepository;
    private  Map value=null;
    @Autowired
    private RestTempConfig restTempConfig;
    @Autowired
    public InboundTripServiceImpl(SectionRepository sectionRepository,WorkStationRepository workStationRepository,InboundInstructRepository inboundInstructRepository,SystemPropertyBusiness systemPropertyBusiness,TripRepository tripRepository, PodRepository podRepository,PodReserveUtil podReserveUtil,BuildEntityBusiness buildEntityBusiness,WmsInvUnitLoadRepository wmsInvUnitLoadRepository,
   SfcMitemRepository sfcMitemRepository,WmsInvStockunitRepository wmsInvStockunitRepository, MdStationnodepositionRepository mdStationnodepositionRepository,RedisUtil redisUtil)   {
        this.sectionRepository = sectionRepository;
        this.workStationRepository=workStationRepository;
        this.inboundInstructRepository=inboundInstructRepository;
        this.systemPropertyBusiness=systemPropertyBusiness;
        this.tripRepository=tripRepository;
        this.podRepository=podRepository;
        this.podReserveUtil=podReserveUtil;
        this.buildEntityBusiness=buildEntityBusiness;
        this.wmsInvUnitLoadRepository=wmsInvUnitLoadRepository;
        this.sfcMitemRepository=sfcMitemRepository;
        this.wmsInvStockunitRepository=wmsInvStockunitRepository;
        this.mdStationnodepositionRepository=mdStationnodepositionRepository;
        this.redisUtil=redisUtil;
    }

    @Transactional
    private void execute(InboundInstruct is) {

        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("开始执行入库指令\n{}",JSONUtil.toJSon(is));
        }

        String podName="";

        if(!StringUtils.isEmpty(is.getSTORAGE_NO_L())&&StringUtils.isEmpty(is.getSTORAGE_NO_R()))
        {
            podName=is.getSTORAGE_NO_L();
        }else if(!StringUtils.isEmpty(is.getSTORAGE_NO_R())&&StringUtils.isEmpty(is.getSTORAGE_NO_L()))
        {
            podName=is.getSTORAGE_NO_R();
        }else{
            LOGGER.error("接受到入库指令STORAGE_NO_L STORAGE_NO_R 货架为空,停止调度{}",JSONUtil.toJSon(is));
          //  redisUtil.put("InboundInstruct",is.getId(),value.put("error","InboundInstruct ID "+is.getId() +"STORAGE_NO_L " +is.getSTORAGE_NO_L()+"STORAGE_NO_R"+ is.getSTORAGE_NO_R()+" 同时存在,不允许"));
            return;
        }



        if(test)
        {

            Map param= ackSimulate.ceateParam(is.getId(),is.getBILL_TYPE(),InstructStatus.ACCEPT.getStatus(),podName);
            rabbitMqReceiver.receiveMapMessage2(param);
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            param= ackSimulate.ceateParam(is.getId(),is.getBILL_TYPE(),InstructStatus.RUNNING.getStatus(),podName);

            try {
                TimeUnit.SECONDS.sleep(10);
                rabbitMqReceiver.receiveMapMessage2(param);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            param= ackSimulate.ceateParam(is.getId(),is.getBILL_TYPE(),InstructStatus.STOCKIN.getStatus(),podName);
            try {
                TimeUnit.SECONDS.sleep(10);
                rabbitMqReceiver.receiveMapMessage2(param);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        Pod pod= podRepository.getByPodName(podName);

        if(ObjectUtils.isEmpty(pod))
        {

            LOGGER.error("无货架{}",podName);
           // redisUtil.put("InboundInstruct",is.getId(),value.put("error"," 无货架"+podName));

            return;
        }
        String stationName=is.getWORKCENTER_CODE();
        List<MdStationnodeposition> stations=mdStationnodepositionRepository.getStationPosition(StationType.INBOUND,pod.getPlaceMark(),stationName);

        if(CollectionUtils.isEmpty(stations))
        {
            LOGGER.error("处理入库指令 {}出错, 货架{} 地址{} 不在工作站{}位置",is.getId(),pod.getPodIndex(),pod.getPlaceMark(),stationName);
           // redisUtil.put("InboundInstruct",is.getId(),value.put("error"," 货架"+pod.getPodIndex() +"货架地址"+pod.getPlaceMark()+"不在工作站"+stationName+"位置"));

            return;
        }
        synchronized (lock)
        {
            String datetimeStock= DateTimeUtil.getDateFormat(new Date(),"yyyy-MM-dd HH:mm:ss");
            WmsInstructInPosition iip=new WmsInstructInPosition();
            iip.setInboundInstruct(is);
            iip.setCAR_NO("");
            iip.setDATETIME_STOCK(DateTimeUtil.strToTimeStamp(datetimeStock));
            iip.setLOC_CODE(pod.getPodIndex()+"");
            iip.setSTATUS(InstructStatus.ACCEPT.getStatus());

            List<InboundInstruct> iis=inboundInstructRepository.getInstru(Arrays.asList(new String[]{InstructStatus.CANCEL.getStatus()}),is.getId());
            boolean cancel=!CollectionUtils.isEmpty(iis);
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("指令是否取消{} {}",cancel,JSONUtil.toJSon(iis));
            }
            if(!cancel)
            {
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("开始生成入库指令id{}的记录",is.getId());
                }
                WmsInvUnitload invLoad= wmsInvUnitLoadRepository.getUnitLoadByPodIndex(pod.getPodIndex(),Constance.avable);
                if(ObjectUtils.isEmpty(invLoad))
                {
                    invLoad=new WmsInvUnitload();
                    invLoad.setPodIndex(pod.getPodIndex());
                    invLoad.setInboundInstructId(is.getId());
                    //重量的获取
                    invLoad.setStationName(stationName);
                    invLoad.setEntityLock(Constance.avable);
                    invLoad.setWarehouseId(pod.getWarehouseId());
                    invLoad=wmsInvUnitLoadRepository.saveAndFlush(invLoad);
                    List<SfcMitem> sfcMitems= sfcMitemRepository.getSfcMitemByName(is.getMITEM_CODE());
                    WmsInvStockunit skunit=new WmsInvStockunit();
                    skunit.setClientId("SYSTEM");
                    skunit.setWarehouseId(pod.getWarehouseId());
                    skunit.setAmount(is.getQTY());
                    skunit.setState("Inventory");
                    skunit.setEntityLock(Constance.stockInUnit);
                    skunit.setUnitloadId(invLoad.getId());

                    skunit.setItemdataId(ObjectUtils.isEmpty(sfcMitems)?"":sfcMitems.get(0).getId());
                    wmsInvStockunitRepository.saveAndFlush(skunit);
                }else{
                    LOGGER.error("保存入库指令 {} 库存失败,已经存在对应的库存unitLoad{}",is.getId(),invLoad.getId());
                   // redisUtil.put("InboundInstruct","info","已经存在对应的库存unitLoad"+invLoad.getId());
                    return;
                }
                podReserveUtil.reservePod(pod);
                Trip trip=buildEntityBusiness.buildTrip(pod,null,is, TripType.parserTripType(is.getBILL_TYPE()));



                Map param=new HashMap();
                param.put("ID",is.getMES_ID());
                param.put("INV_ORG_ID",is.getINV_ORG_ID());
                param.put("BILL_TYPE",is.getBILL_TYPE());
                param.put("BILL_NO",is.getBILL_NO());
                param.put("LABEL_NO",is.getLABEL_NO());
                param.put("INV_CODE", is.getINV_CODE());
                param.put("CAR_NO","");
                param.put("LOC_CODE",pod.getPodIndex());
                param.put("STATUS",InstructStatus.ACCEPT.getStatus());
                param.put("DATETIME_STOCK",datetimeStock);
                Map ack= restTempConfig.inBoundAck(param);

                iip.setSTATUS(InstructStatus.ACCEPT.getStatus());
                iip=wmsInstructInPositionRepository.saveAndFlush(iip);
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("保存入库指令CREATE状态成功\n{}",JSONUtil.toJSon(iip));
                }

//                value.put("tripId",trip.getId());
//                value.put("message","success");
//                value.put("ack",ack);
//                value.put("instructPosition",iip);
//                redisUtil.put("InboundInstruct",is.getId(),value);
            }else{
                //redisUtil.put("InboundInstruct",is.getId(),value.put("error"," 指令id "+is.getId()+"被取消"));
                LOGGER.debug(" 指令id "+is.getId()+"被取消");

            }


        }



    }


    @Override
    public void buildTrip() {

        value=new HashMap();
        List<InboundInstruct> iis=inboundInstructRepository.getAllNotCreateTripInstru(Arrays.asList(new String[]{InstructStatus.ACCEPT.getStatus(),InstructStatus.CANCEL.getStatus(),InstructStatus.RUNNING.getStatus(),InstructStatus.STOCKIN.getStatus()}));

        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("查询到需要调度的入库指令\n{}",JSONUtil.toJSon(iis));
        }


        if(!CollectionUtils.isEmpty(iis))
        {
            int len=iis.size();
            List<InboundInstruct> executeInstruct=null;
            if(len>5)
            {
                executeInstruct=iis.subList(0,4);
            }else{
                executeInstruct=iis.subList(0,len);
            }
            for(InboundInstruct ii:executeInstruct)
            {
                try{
                    execute(ii);
                }catch (Exception e)
                {
                    LOGGER.error("处理入库指令出错{}",JSONUtil.toJSon(ii));
                    LOGGER.error(e.getMessage(),e);
                }

            }


        }else{
            //redisUtil.put("InboundInstruct","info","没有需要调度的入库指令");
            LOGGER.debug("没有需要调度的入库指令");
        }





    }


    @Override
    public  boolean cancelInstruct(InboundInstruct instruct,String status){
        Boolean result=false;
        synchronized (lock)
        {
            List<InboundInstruct> iis=inboundInstructRepository.getInstru(Arrays.asList(new String[]{InstructStatus.ACCEPT.getStatus(),InstructStatus.STOCKIN.getStatus(),InstructStatus.RUNNING.getStatus()}),instruct.getId());
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("入库指令id{} 已经生成{}",instruct.getId(),JSONUtil.toJSon(iis));
            }
            if(CollectionUtils.isEmpty(iis))
            {
                String datetimeStock= DateTimeUtil.getDateFormat(new Date(),"yyyy-MM-dd HH:mm:ss");
                WmsInstructInPosition iip=new WmsInstructInPosition();
                iip.setInboundInstruct(instruct);
                iip.setCAR_NO("");
                iip.setDATETIME_STOCK(DateTimeUtil.strToTimeStamp(datetimeStock));

                iip.setSTATUS(InstructStatus.CANCEL.getStatus());
                iip=wmsInstructInPositionRepository.saveAndFlush(iip);
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("生成入库指令id{} 取消明细{}",instruct.getId(),JSONUtil.toJSon(iip));
                }
                result=true;
            }


        }
        return result;
    }
}
