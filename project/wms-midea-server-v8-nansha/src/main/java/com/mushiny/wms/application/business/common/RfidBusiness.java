package com.mushiny.wms.application.business.common;

import com.mushiny.wms.application.config.RestTempConfig;
import com.mushiny.wms.application.domain.*;
import com.mushiny.wms.application.domain.enums.*;
import com.mushiny.wms.application.rabbitMq.RabbitMessageSender;
import com.mushiny.wms.application.repository.*;
import com.mushiny.wms.application.service.EmptyPodCarryService;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.Map;


/**
 * Created by Administrator on 2018/7/6.
 */
@Component
@Transactional
public class RfidBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(RfidBusiness.class);
    private final PodRepository podRepository;
    private final StationnodeRepository stationnodeRepository;
    private final BuildEntityBusiness buildEntityBusiness;
    private final TripRepository tripRepository;
    private final  SectionRepository sectionRepository;
    @Autowired
    private RabbitMessageSender rabbitMessageSender;
    private PodReserveUtil podReserveUtil;
    @Autowired
    private RestTempConfig restTempConfig;
    @Autowired
    private WmsInstructOutPositionRepository wminstructOutPositionRepository;
    @Autowired
    private WmsInvUnitLoadRepository wmsInvUnitLoadRepository;
    @Autowired
    private WmsInvStockunitRepository wmsInvStockunitRepository;
    @Autowired
    private MapRepository mapRepository;
    private  String sectionId="ec229eb7-7e2b-43a8-b1c7-91bd807e91cf";
    @Autowired
    public RfidBusiness(PodRepository podRepository, StationnodeRepository stationnodeRepository,PodReserveUtil podReserveUtil,BuildEntityBusiness buildEntityBusiness,TripRepository tripRepository,SectionRepository sectionRepository) {
        this.podReserveUtil=podReserveUtil;
        this.buildEntityBusiness=buildEntityBusiness;
        this.podRepository = podRepository;
        this.stationnodeRepository = stationnodeRepository;
        this.tripRepository=tripRepository;
        this.sectionRepository=sectionRepository;
    }


    public void autoRemove(){
        List<com.mushiny.wms.application.domain.Map> maps=mapRepository.findAllMap();

        if(!CollectionUtils.isEmpty(maps)&&maps.size()==1)
        {
            List<Pod> pods=podRepository.getStationPod(maps.get(0).getSectionId(),Arrays.asList(new Integer[]{StationType.OUTBOUNDAUTO,StationType.OUTBOUNDMANUL}));

            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("查找需要移除货架 {}",JSONUtil.toJSon(pods));
            }

            for(Pod pod:pods)
            {
                Map temp=new HashMap();
                temp.put("podIndex",pod.getPodIndex());
                temp.put("podAddress",0);
                saveRfidInfo(temp);
            }
        }else{
            LOGGER.error("查找到多个地图，停止移除货架 {}",JSONUtil.toJSon(maps));
        }

    }


    public Integer saveRfidInfo(Map pods)
    {
        Integer result=1;

        List<com.mushiny.wms.application.domain.Map> maps=mapRepository.findAllMap();

        if(!CollectionUtils.isEmpty(maps)&&maps.size()==1)
        {

            Integer podAddress=NumberUtils.parseNumber(""+pods.get("podAddress"),Integer.class);;
            Integer podIndex=NumberUtils.parseNumber(""+pods.get("podIndex"),Integer.class);

            LOGGER.debug("接收到RFIF信息为{}", JSONUtil.toJSon(pods));

            char[] podName=new char[]{'P','0','0','0','0','0','0','0'};


            if(!StringUtils.isEmpty(podIndex))
            {
                char[] podtemp= (podIndex+"").toCharArray();
                int beagin=podName.length;
                int charLen=podtemp.length;
                for(char t:podtemp)
                {
                    podName[beagin-charLen]=t;
                    beagin++;
                }

                Pod ext=podRepository.getByPodIndex2(podIndex);
                if(!ObjectUtils.isEmpty(ext))
                {
                    ext.setPlaceMark(podAddress);
                }else{
                    ext=new Pod();
                    ext.setName(new String(podName));
                    ext.setPodIndex(podIndex);
                    ext.setPodTypeId("168e0596-ef79-4661-ae3e-7d7f760d95cd");
                    ext.setZoneId("0134c83e-5b55-4706-a51c-01b671b18a7d");
                    ext.setSectionId(maps.get(0).getSectionId());
                    ext.setClientId("SYSTEM");
                    ext.setPlaceMark(podAddress);
                    ext.setxPos(19);
                    ext.setyPos(20);
                    ext.setState(PodStateEnum.RESERVED.getName());
                    ext.setToward(0);
                    ext.setWarehouseId("DEFAULT");
                    ext=podRepository.saveAndFlush(ext);
                }
                inventoryReduction(ext);
            }else{
                result=0;
            }
        }else{
            result=0;
        }



        return result;

    }



    public Integer workStationCall(String stationName,Integer iscall){

        return stationnodeRepository.workstationCall(stationName,iscall, sectionId);
    }

    public void OutBoundInstructCallBack(Integer podIndex)
    {
        if(!ObjectUtils.isEmpty(podIndex))
        {
            Pod ext=podRepository.getByPodIndex2(podIndex);
            if(!ObjectUtils.isEmpty(ext))
            {
                List<WmsInstructOutPosition> otP=  wminstructOutPositionRepository.getInstructOutPositionByRFIDPod(ext.getName());
                if(!CollectionUtils.isEmpty(otP)&&otP.size()==1)
                {

                    OutboundInstruct outboundInstruct=otP.get(0).getOutboundInstruct();
                    if(outboundInstruct.getBILL_TYPE().equalsIgnoreCase(TripType.LMGETMATERIAL.getName()))
                    {
                        String datetimeStock= DateTimeUtil.getDateFormat(new Date(),"yyyy-MM-dd HH:mm:ss");
                        Map  param=new HashMap();
                        param.put("ID",outboundInstruct.getMES_ID());
                        param.put("INV_ORG_ID",outboundInstruct.getINV_ORG_ID());
                        param.put("BILL_TYPE",outboundInstruct.getBILL_TYPE());
                        param.put("BILL_NO",outboundInstruct.getBILL_NO());
                        param.put("LABEL_NO",outboundInstruct.getLABEL_NO());
                        param.put("INV_CODE", outboundInstruct.getINV_CODE());
                        param.put("STOCK_QTY",outboundInstruct.getQTY());
                        param.put("STORAGE_NO_L",otP.get(0).getSTORAGE_NO_L());
                        param.put("CAR_NO",otP.get(0).getCAR_NO());
                        param.put("LOC_CODE",otP.get(0).getLOC_CODE());

                        param.put("DATETIME_STOCK",datetimeStock);
                        param.put("STATUS", InstructStatus.DISPATCH.getStatus());
                        restTempConfig.outBoundAck(param);
                        WmsInstructOutPosition iop=new WmsInstructOutPosition();
                        iop.setCAR_NO(otP.get(0).getCAR_NO());
                        iop.setDATETIME_STOCK(DateTimeUtil.strToTimeStamp(datetimeStock));
                        iop.setOutboundInstruct(outboundInstruct);
                        iop.setSTATUS(InstructStatus.DISPATCH.getStatus());
                        iop.setSTOCK_QTY(outboundInstruct.getQTY().toString());
                        iop=wminstructOutPositionRepository.saveAndFlush(iop);
                        if(LOGGER.isDebugEnabled())
                        {
                            LOGGER.debug("保存出库指令配送状态成功\n{}",JSONUtil.toJSon(iop));
                        }
                    }else{
                        if(LOGGER.isDebugEnabled())
                        {
                            LOGGER.debug("pod {} 对应的指令不是领料出库类型\n{}",ext.getName(),JSONUtil.toJSon(otP));
                        }
                    }

                }else{
                    if(LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug("pod {} 查询到多个出库指令\n{}",ext.getName(),JSONUtil.toJSon(otP));
                    }
                }
            }else{
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("系统不存在pod {}",podIndex);
                }
            }
        }
    }


    public void inventoryReduction(Pod pod){
        WmsInvUnitload wil= wmsInvUnitLoadRepository.getUnitLoadByPodIndex(pod.getPodIndex(), Constance.stockInUnit);
        if(!ObjectUtils.isEmpty(wil))
        {
            wil.setEntityLock(Constance.stockOutUnit);
            wmsInvUnitLoadRepository.saveAndFlush(wil);
            WmsInvStockunit isku=wmsInvStockunitRepository.getStockUnitByUnitLoadId(wil.getId(),Constance.stockInUnit);
            if(!ObjectUtils.isEmpty(isku))
            {
                isku.setEntityLock(Constance.stockOutUnit);
                wmsInvStockunitRepository.saveAndFlush(isku);
            }
        }
        Map param=new HashMap();
        param.put("ID",pod.getId());
        param.put("NAME",pod.getName());
        param.put("TOWARD","0");
        param.put("SECTION_ID",pod.getSectionId());
        param.put("PLACEMARK",pod.getPlaceMark());
        Section section=sectionRepository.getOne(pod.getSectionId());
        rabbitMessageSender.sendMessage(param,section.getName(),"WMS_WCS_POD_ADD_REMOVE");

    }


}
