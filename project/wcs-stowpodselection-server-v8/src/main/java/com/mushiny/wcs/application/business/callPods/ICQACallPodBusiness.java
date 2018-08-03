package com.mushiny.wcs.application.business.callPods;

import com.mushiny.wcs.application.business.common.CommonBusiness;
import com.mushiny.wcs.application.business.common.SystemPropertyBusiness;
import com.mushiny.wcs.application.business.dto.CallPod;
import com.mushiny.wcs.application.business.dto.CallStation;
import com.mushiny.wcs.application.business.enums.StationType;
import com.mushiny.wcs.application.respository.PodRepository;
import com.mushiny.wcs.application.respository.PqaEnroutePodRepository;
import com.mushiny.wcs.application.respository.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/10/12.
 */
@Component
public class ICQACallPodBusiness extends  CommonCallPodBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(ICQACallPodBusiness.class);
    @Autowired
    public ICQACallPodBusiness(PodRepository podRepository,
                               SystemPropertyBusiness systemPropertyBusiness,
                               CommonBusiness commonBusiness,
                               TripRepository tripRepository,
                               PqaEnroutePodRepository pqaEnroutePodRepository) {
        super(podRepository,systemPropertyBusiness,commonBusiness,tripRepository,pqaEnroutePodRepository);

    }

    @Override
    public void setStationMaxPod(CallStation callStation){
        int stationMaxPod= systemPropertyBusiness.getBinCheckStationMaxPod(callStation.getWorkStation().getWarehouseId());
        callStation.setWorkStationMaxPod(stationMaxPod);
    }
    @Override
    public void setStationMaxWorkLoad(CallStation callStation){
        int maxWorkLoad=systemPropertyBusiness.getBinCheckStationMaxWorkLoad(callStation.getWorkStation().getWarehouseId());
        callStation.setWorkMaxWorkLoad(maxWorkLoad);
    }
    @Override
    public void setStationCycle(CallStation callStation){
        int cycle= systemPropertyBusiness.getBinCheckStationCycle(callStation.getWorkStation().getWarehouseId());
        callStation.setPodCycleTime(cycle);

    }
    @Override
    public boolean overMaxStationAvailablePod(CallStation callStation,CallPod callPod){
        boolean result=false;
        int  stationMaxPod= callStation.getWorkStaionMaxPod();
        int stationAvailablePod = commonBusiness.countWorkStationAvailablePod(callStation.getWorkStation());
        if(stationAvailablePod >= stationMaxPod){
            LOGGER.debug("当前工作站{}绑定的pod以达最大值,停止生成pod {} 调度单\n" +
                    "stationMaxPod={} stationAvailablePod={}",callStation.getWorkStation().getId(),callPod.getPod().getId(),stationMaxPod,stationAvailablePod);
            result=true;
        }
        return result;
    }
    @Override
    public boolean overMaxStationWork(CallStation callStation,CallPod callPod){
        boolean result=false;
        int systemMaxPsWorkLoad=callStation.getWorkMaxWorkLoad();
        int positionNum=commonBusiness.countPqaWorkStationAvailablePod(callStation.getWorkStation());
        int cycle=callStation.getPodCycleTime();
        int stationWorkLoad=positionNum*cycle;
        callStation.setWorkStationWorkLoad(stationWorkLoad);
        if(stationWorkLoad>systemMaxPsWorkLoad)
        {
            LOGGER.debug("当前工作站{}工作量为{},已近达到最大值 {} ,停止生成调度单",callStation.getWorkStation().getId(),stationWorkLoad,systemMaxPsWorkLoad);
            result=true;
        }
        return result;
    }

    @Override
    public StationType getStationType() {
        return StationType.ICQA;
    }
}
