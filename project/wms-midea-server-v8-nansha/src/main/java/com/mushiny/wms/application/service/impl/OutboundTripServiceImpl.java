package com.mushiny.wms.application.service.impl;

import com.mushiny.wms.application.business.common.BuildEntityBusiness;
import com.mushiny.wms.application.business.common.CommonBusiness;
import com.mushiny.wms.application.business.common.PodReserveUtil;
import com.mushiny.wms.application.business.common.SystemPropertyBusiness;
import com.mushiny.wms.application.business.score.StationPodScore;
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
import com.mushiny.wms.application.service.OutboundTripService;
import com.mushiny.wms.application.test.AckSimulate;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.EntityManagerUtil;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/6.
 */

@Service

public class OutboundTripServiceImpl implements OutboundTripService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OutboundTripServiceImpl.class);
    @Value("${mushiny.test}")
    private boolean test;
    @Autowired
    private AckSimulate ackSimulate;
    @Autowired
    private RabbitMqReceiver rabbitMqReceiver;
    private final SectionRepository sectionRepository;
    private final WorkStationRepository workStationRepository;
    private final OutboundInstructRepository outboundInstructRepository;
    private final SystemPropertyBusiness systemPropertyBusiness;
    private final TripRepository tripRepository;
    private final PodRepository podRepository;
    private final BuildEntityBusiness buildEntityBusiness;
    private PodReserveUtil podReserveUtil;
    private final EntityManagerUtil entityManagerUtil;
    private final  MdStationnodepositionRepository mdStationnodepositionRepository;
    private  Map value=null;
    private StationnodeRepository stationnodeRepository;
    @Autowired
    private RestTempConfig restTempConfig;
    @Autowired
    private CommonBusiness commonBusiness;
    @Autowired
    private WmsInstructOutPositionRepository wmsInstructOutPositionRepository;
    private final Object lock=new Object();
    private final RedisUtil redisUtil;
    private transient Map hasGenTrip=new HashMap();
    private transient Map cancelInstrue=new ConcurrentHashMap();
    @Autowired
    public OutboundTripServiceImpl(SectionRepository sectionRepository, WorkStationRepository workStationRepository,
                                   OutboundInstructRepository outboundInstructRepository, SystemPropertyBusiness systemPropertyBusiness,
                                   TripRepository tripRepository, PodRepository podRepository,EntityManagerUtil entityManagerUtil,
                                   PodReserveUtil podReserveUtil, BuildEntityBusiness buildEntityBusiness,
                                   MdStationnodepositionRepository mdStationnodepositionRepository,RedisUtil redisUtil,StationnodeRepository stationnodeRepository)   {
        this.sectionRepository = sectionRepository;
        this.workStationRepository=workStationRepository;
        this.outboundInstructRepository=outboundInstructRepository;
        this.systemPropertyBusiness=systemPropertyBusiness;
        this.tripRepository=tripRepository;
        this.podRepository=podRepository;
        this.podReserveUtil=podReserveUtil;
        this.buildEntityBusiness=buildEntityBusiness;
        this.entityManagerUtil=entityManagerUtil;
        this.mdStationnodepositionRepository=mdStationnodepositionRepository;
        this.redisUtil=redisUtil;
        this.stationnodeRepository=stationnodeRepository;
    }

    @Transactional
    private void execute(OutboundInstruct outboundInstruct){

        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("开始处理出库指令{}",JSONUtil.toJSon(outboundInstruct));
        }
        if(test)
        {

            Map param=ackSimulate.ceateParam(outboundInstruct.getId(),outboundInstruct.getBILL_TYPE(),InstructStatus.ACCEPT.getStatus(),null);
            try {
                rabbitMqReceiver.receiveMapMessage2(param);
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            param=ackSimulate.ceateParam(outboundInstruct.getId(),outboundInstruct.getBILL_TYPE(),InstructStatus.RUNNING.getStatus(),null);
            try {
                TimeUnit.SECONDS.sleep(10);
                rabbitMqReceiver.receiveMapMessage2(param);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            param=ackSimulate.ceateParam(outboundInstruct.getId(),outboundInstruct.getBILL_TYPE(),InstructStatus.STOCKIN.getStatus(),null);
            try {
                TimeUnit.SECONDS.sleep(10);
                rabbitMqReceiver.receiveMapMessage2(param);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        String lineCode="";
        if("ANNTOMVOUT".equalsIgnoreCase(outboundInstruct.getBILL_TYPE()))
        {
            lineCode=outboundInstruct.getWORKCENTER_CODE();
        }else{
            lineCode=outboundInstruct.getLINE_CODE();
        }

        List<MdStationnodeposition> stations=mdStationnodepositionRepository.getIdleStationPosition(Arrays.asList(new String[]{TripState.FINISHED.getName()}),lineCode);

        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("查询到产线{}空闲的停止点{}",lineCode,JSONUtil.toJSon(stations));
        }
        if(CollectionUtils.isEmpty(stations))
        {
            LOGGER.error("未出库指令产线LINE_CODE{}对应的工作站 {} {}",lineCode,JSONUtil.toJSon(outboundInstruct));
           // redisUtil.put("OutboundInstruct",outboundInstruct.getId(),value.put("error","未找到出库指令产线LINE_CODE"+lineCode+" 对应的工作站"));

          //  return;
        }else{
        List<String> tripState=new ArrayList<>();
        tripState.add(TripState.FINISHED.getName());
        String sql= "SELECT MD_POD.ID FROM WMS_INBOUND_INSTRUCT \n" +
                "INNER JOIN WMS_OUTBOUND_INSTRUCT ON WMS_INBOUND_INSTRUCT.LABEL_NO=WMS_OUTBOUND_INSTRUCT.LABEL_NO  "+
                "INNER JOIN WMS_INV_UNITLOAD ON WMS_INV_UNITLOAD.INBOUND_INSTRUCT_ID=WMS_INBOUND_INSTRUCT.ID\n" +
                "INNER JOIN MD_POD ON MD_POD.POD_INDEX=WMS_INV_UNITLOAD.POD_INDEX where WMS_INBOUND_INSTRUCT.LABEL_NO=:labelNo  " +
                " and WMS_OUTBOUND_INSTRUCT.ID not in  (SELECT DISTINCT coalesce(WMS_INSTRUCT_OUT_POSITION.INSTRUCT_ID,'') from  WMS_INSTRUCT_OUT_POSITION  ) "+
                " and WMS_INV_UNITLOAD.ENTITY_LOCK=0";

        java.util.Map param = new HashMap();
       // param.put("moName", outboundInstruct.getMO_NAME());
        param.put("labelNo", outboundInstruct.getLABEL_NO());


        List<Map> pods = entityManagerUtil.executeNativeQuery2(sql,param);

        if(CollectionUtils.isEmpty(pods))
        {

            LOGGER.error("出库指令{} 工单号{} 没有库存纪录",outboundInstruct.getId(),outboundInstruct.getMO_NAME());
          //  redisUtil.put("OutboundInstruct",outboundInstruct.getId(),value.put("error","出库指令"+" 工单号 "+outboundInstruct.getMO_NAME() +" 配送卡号"+outboundInstruct.getLABEL_NO()+"没有库存纪录"));

          //  return;
        }else if(pods.size()>1) {
            LOGGER.error("出库指令{} 配送卡号{}工单号{} 查找到了多个库存纪录\n{}",outboundInstruct.getId(),outboundInstruct.getLABEL_NO(),outboundInstruct.getMO_NAME(), JSONUtil.toJSon(pods));
          //  value.put("error","出库指令"+" 工单号 "+outboundInstruct.getMO_NAME() +" 配送卡号"+outboundInstruct.getLABEL_NO()+"有多个库存");
          //  value.put("pods",pods);
          //  redisUtil.put("OutboundInstruct",outboundInstruct.getId(),value);
           // return;
        }else {
        LOGGER.debug("出库指令{} 配送卡号{}满足出库条件",outboundInstruct.getId(),outboundInstruct.getLABEL_NO());
        Pod savePod=podRepository.findOne((String)pods.get(0).get("ID"));
        List<StationPodScore> stationScore=commonBusiness.getStationPodScore(stations,savePod);

        synchronized (lock)
        {
            String datetimeStock= DateTimeUtil.getDateFormat(new Date(),"yyyy-MM-dd HH:mm:ss");
            WmsInstructOutPosition iop=new WmsInstructOutPosition();
            iop.setCAR_NO("");
            iop.setDATETIME_STOCK(DateTimeUtil.strToTimeStamp(datetimeStock));
            iop.setOutboundInstruct(outboundInstruct);
            iop.setSTOCK_QTY(outboundInstruct.getQTY().toString());
            StationPodScore sps=stationScore.get(0);
            Stationnode sn=sps.getStationnodeposition().getStationnode();
               /* if(!sn.getCallPod()) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("工作站{}停止呼叫", sn.getName());
                    }
                    return;
                }*/
            String sectionId=sn.getSectionId();
            String wareHouseId=sn.getWarehouseId();
            List<Trip> trips=tripRepository.getWorkStationNotFinishTrip(tripState,sectionId,sps.getStationnodeposition().getId(),wareHouseId);

            Pod pod= podRepository.getWorkStationTrip(sps.getStationnodeposition().getNode().getAddressCodeId(),sectionId);
            if(!CollectionUtils.isEmpty(trips)||!ObjectUtils.isEmpty(pod))
            {
                LOGGER.error("Stationnode id{} 出库工作站{} 地址{} pod {} 有未完成的调度单 \n{},\n未分配指令{} ",sn.getId(),sn.getName(),sps.getStationnodeposition().getNode().getAddressCodeId(),JSONUtil.toJSon(pod),JSONUtil.toJSon(trips),JSONUtil.toJSon(pod));

              //  value.put("error","Stationnode id "+ sn.getId()+"出库工作站"+sn.getName()+"地址"+sps.getStationnodeposition().getNode().getAddressCodeId()+"有pod "+pod.getPodIndex()+"有未完成的调度单 "+JSONUtil.toJSon(trips));
             //   value.put("pod",pod);
             //   value.put("trip",trips);
              //  redisUtil.put("OutboundInstruct",outboundInstruct.getId(),value);
            }else{
                List<OutboundInstruct> iis=outboundInstructRepository.getInstru(Arrays.asList(new String[]{InstructStatus.CANCEL.getStatus()}),outboundInstruct.getId());
                boolean cancel=!CollectionUtils.isEmpty(iis);
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("出库指令是否取消{} {}",cancel,JSONUtil.toJSon(iis));
                }
                if(!cancel) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("开始生成出库指令id{}的记录", outboundInstruct.getId());
                    }
                    podReserveUtil.reservePod(savePod);
                    Trip trip = buildEntityBusiness.buildTrip(savePod, sps.getStationnodeposition(), outboundInstruct, TripType.parserTripType(outboundInstruct.getBILL_TYPE()));
                    Map param2 = new HashMap();

                    param2.put("ID", outboundInstruct.getMES_ID());
                    param2.put("INV_ORG_ID", outboundInstruct.getINV_ORG_ID());
                    param2.put("BILL_TYPE", outboundInstruct.getBILL_TYPE());
                    param2.put("BILL_NO", outboundInstruct.getBILL_NO());
                    param2.put("LABEL_NO", outboundInstruct.getLABEL_NO());
                    param2.put("INV_CODE", outboundInstruct.getINV_CODE());

                    param2.put("STOCK_QTY", outboundInstruct.getQTY());

                    param2.put("DATETIME_STOCK", datetimeStock);

                    param2.put("LOC_CODE", "");
                    param2.put("STORAGE_NO_L", "");
                    param2.put("CAR_NO", "");
                    param2.put("STATUS", InstructStatus.ACCEPT.getStatus());
                    Map ack= restTempConfig.outBoundAck(param2);
                    hasGenTrip.put(outboundInstruct.getId(), trip.getId());
                    iop.setSTATUS(InstructStatus.ACCEPT.getStatus());

                    iop = wmsInstructOutPositionRepository.saveAndFlush(iop);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("接收到出库指令,修改状态为ACCEPT\n 指令{},\n取消后信息{}", outboundInstruct.getId(), JSONUtil.toJSon(iop));
                    }
//                    value.put("tripId",trip.getId());
//                    value.put("message","success");
//                    value.put("ack",ack);
//                    value.put("outstructPosition",iop);
//                    redisUtil.put("OutboundInstruct",outboundInstruct.getId(),value);
                }else{
//                    value.clear();
//                    value.put("error","出库指令已经取消");
//                    redisUtil.put("OutboundInstruct",outboundInstruct.getId(),value);
                    LOGGER.debug("出库指令已经取消");
                }
            }
        }
        }
        }
    }


    @Override
    public void buildTrip() {
//需要考虑已经生成i
        //获取所有的出库工作站
        List<Stationnode> stationnodes = stationnodeRepository.getAllOutStation();
        LOGGER.debug("总共产线出库工作站"+stationnodes.size()+"个");
        for(Stationnode stationnode:stationnodes){
        LOGGER.debug("当前操作的出库工作站是----->"+stationnode.getName());
        List<OutboundInstruct> iis=outboundInstructRepository.getAllNotCreateTripInstru(Arrays.asList(new String[]{InstructStatus.ACCEPT.getStatus(),InstructStatus.RUNNING.getStatus(),InstructStatus.CANCEL.getStatus(),InstructStatus.STOCKOUT.getStatus()}),stationnode.getName());
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("查询到需要调度的出库指令\n{}",JSONUtil.toJSon(iis));
        }
        value=new HashMap();

        if(!CollectionUtils.isEmpty(iis))
        {
            int len=iis.size();
            List<OutboundInstruct> executeInstruct=null;
            if(len>5)
            {
                executeInstruct=iis.subList(0,4);
            }else{
                executeInstruct=iis.subList(0,len);
            }
            for(OutboundInstruct ii:executeInstruct)
            {
                try{
                    execute(ii);
                }catch (Exception e)
                {
                    LOGGER.error("处理出库指令出错{}",JSONUtil.toJSon(ii));
                    LOGGER.error(e.getMessage(),e);
                }


            }
        }else{
          //  redisUtil.put("OutboundInstruct","info","没有需要调度的出库指令");
            LOGGER.debug("没有需要调度的出库指令");
        }
        }
    }
    @Override
    public  boolean cancelInstruct(OutboundInstruct instruct,String status){
        Boolean result=false;
        synchronized (lock)
        {
            String datetimeStock= DateTimeUtil.getDateFormat(new Date(),"yyyy-MM-dd HH:mm:ss");
            WmsInstructOutPosition iop=new WmsInstructOutPosition();
            iop.setCAR_NO("");
            iop.setDATETIME_STOCK(DateTimeUtil.strToTimeStamp(datetimeStock));
            iop.setOutboundInstruct(instruct);
            iop.setSTATUS(InstructStatus.CANCEL.getStatus());
            List<OutboundInstruct> iis=outboundInstructRepository.getInstru(Arrays.asList(new String[]{InstructStatus.ACCEPT.getStatus(),InstructStatus.STOCKIN.getStatus(),InstructStatus.RUNNING.getStatus()}),instruct.getId());
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("入库指令id{} 已经生成{}",instruct.getId(),JSONUtil.toJSon(iis));
            }
            if(CollectionUtils.isEmpty(iis))
            {
                iop=wmsInstructOutPositionRepository.saveAndFlush(iop);
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("生成出库指令id{} 取消明细{}",instruct.getId(),JSONUtil.toJSon(iop));
                }
                result=true;
            }
        }
        return result;
    }

}
