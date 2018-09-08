package com.mushiny.wms.application.service.impl;

import com.mushiny.wms.application.business.common.*;

import com.mushiny.wms.application.business.score.StationPodScore;
import com.mushiny.wms.application.config.RestTempConfig;
import com.mushiny.wms.application.domain.*;

import com.mushiny.wms.application.domain.enums.StationType;
import com.mushiny.wms.application.domain.enums.TripState;
import com.mushiny.wms.application.domain.enums.TripType;
import com.mushiny.wms.application.repository.*;
import com.mushiny.wms.application.service.EmptyPodCarryService;
import com.mushiny.wms.common.utils.EntityManagerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.Map;


/**
 * Created by Administrator on 2018/7/6.
 */

@Service
@Transactional
public class EmptyPodCarryServiceImpl implements EmptyPodCarryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmptyPodCarryService.class);
    private final SectionRepository sectionRepository;
    private final WorkStationRepository workStationRepository;
    private final TripRepository tripRepository;
    private final SystemPropertyBusiness systemPropertyBusiness;
    private final PodRepository podRepository;
    private final EntityManagerUtil entityManagerUtil;
    private final BuildEntityBusiness buildEntityBusiness;
    private final MdStationnodepositionRepository
    mdStationnodepositionRepository;
    @Autowired
    private CommonBusiness commonBusiness;
    @Autowired
    private  StationnodeRepository stationnodeRepository;
    private PodReserveUtil podReserveUtil;
    @Autowired
    private StationNodeBusiness stationNodeBusiness;
    @Autowired
    private RestTempConfig restTempConfig;
    @Autowired
    public EmptyPodCarryServiceImpl(SectionRepository sectionRepository,WorkStationRepository workStationRepository,TripRepository tripRepository,SystemPropertyBusiness systemPropertyBusiness,PodRepository podRepository,EntityManagerUtil entityManagerUtil,PodReserveUtil podReserveUtil,BuildEntityBusiness buildEntityBusiness,MdStationnodepositionRepository
    mdStationnodepositionRepository) {
        this.sectionRepository = sectionRepository;
        this.workStationRepository=workStationRepository;
        this.tripRepository=tripRepository;
        this.systemPropertyBusiness=systemPropertyBusiness;
        this.podRepository=podRepository;
        this.entityManagerUtil=entityManagerUtil;
        this.podReserveUtil=podReserveUtil;
        this.buildEntityBusiness=buildEntityBusiness;
        this.mdStationnodepositionRepository=mdStationnodepositionRepository;
    }

    @Override
    public void runEmptyPodCarry() {

        List<MdStationnodeposition> stations=mdStationnodepositionRepository.getCallPodStationPosition(StationType.INBOUND,Arrays.asList(new String[]{TripState.FINISHED.getName()}));

        if(CollectionUtils.isEmpty(stations))
        {
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("未找到入库工作站");
            }
            return;
        }

        for(MdStationnodeposition msp:stations)
        {
            execute(msp);
        }



    }


    private synchronized void execute(MdStationnodeposition stationPosition){
        String sectionId=stationPosition.getStationnode().getSectionId();
        String warehouseId =stationPosition.getStationnode().getWarehouseId();
       /* List<String> tripState=new ArrayList<>();
        tripState.add(TripState.FINISHED.getName());
        List<Trip> trips=tripRepository.getWorkStationNotFinishTrip(tripState,sectionId,stationPosition.getStationnode().getId(),warehouseId);
        int stationAvailablePod=CollectionUtils.isEmpty(trips)?0:trips.size();


        // 获取工作站容许绑定的最大POD数量
        int stationMaxPod = systemPropertyBusiness.getStowPodStationMaxPod(warehouseId);
        if(stationAvailablePod >= stationMaxPod){
            if(LOGGER.isDebugEnabled())
            {

                LOGGER.debug("工作站{}已经超过设置最大数停止生成 当前pod个数{}.系统设置允许最大{}",stationPosition.getStationnode().getName(),stationAvailablePod,stationMaxPod);
            }
            return;
        }
*/

        String sql="SELECT MD_POD.ID  " +
                " FROM MD_POD " +
                "INNER JOIN WD_NODE ON WD_NODE.ADDRESSCODEID=MD_POD.PLACEMARK AND WD_NODE.TYPE=1" +" INNER JOIN WD_MAP on WD_NODE.MAP_ID=WD_MAP.ID and WD_MAP.ACTIVE=1 "+
                " WHERE MD_POD.STATE='Available' AND MD_POD.PLACEMARK<>0 AND MD_POD.SECTION_ID=:sectionId AND MD_POD.ID NOT IN (SELECT coalesce(RCS_TRIP.POD_ID,'')  FROM RCS_TRIP WHERE RCS_TRIP.TRIP_STATE<>'Finish') and MD_POD.POD_INDEX not in ( SELECT DISTINCT WMS_INV_UNITLOAD.POD_INDEX FROM WMS_INV_UNITLOAD " +
                " where WMS_INV_UNITLOAD.WAREHOUSE_ID=:wareHouseId  and WMS_INV_UNITLOAD.ENTITY_LOCK=0)";

        Map param = new HashMap();
        param.put("sectionId", sectionId);
        param.put("wareHouseId",warehouseId);
        List<Map> pods = entityManagerUtil.executeNativeQuery2(sql,param);
        List<String> tt=new ArrayList<>();
        for(Map t:pods)
        {
            tt.add(""+t.get("ID"));
        }




        if(CollectionUtils.isEmpty(pods))
        {
            if(LOGGER.isDebugEnabled())
            {

                LOGGER.debug("仓库{} section {} 储位没有可用的空pod",warehouseId,sectionId);
            }
            return;
        }
        List<Pod> pod2=podRepository.getPodById(tt);
        List<StationPodScore> podScore=commonBusiness.getPodScore(pod2,stationPosition);

        podReserveUtil.reservePod(podScore.get(0).getPod());
        buildEntityBusiness.buildTrip(podScore.get(0).getPod(),podScore.get(0).getStationnodeposition(),null, TripType.CARRY_POD);


    }


}
