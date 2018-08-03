package com.mushiny.wcs.application.business.callPods;

import com.mushiny.wcs.application.business.common.CommonBusiness;
import com.mushiny.wcs.application.business.common.SystemPropertyBusiness;
import com.mushiny.wcs.application.business.dto.CallPod;
import com.mushiny.wcs.application.business.dto.CallStation;
import com.mushiny.wcs.application.domain.Pod;
import com.mushiny.wcs.application.domain.PqaEnroutePod;
import com.mushiny.wcs.application.domain.Trip;
import com.mushiny.wcs.application.domain.TripPosition;
import com.mushiny.wcs.application.domain.enums.PodStateEnum;
import com.mushiny.wcs.application.domain.enums.TripState;
import com.mushiny.wcs.application.respository.PodRepository;
import com.mushiny.wcs.application.respository.PqaEnroutePodRepository;
import com.mushiny.wcs.application.respository.TripRepository;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/10/12.
 */
@Component
public abstract class CommonCallPodBusiness implements CallPodBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonCallPodBusiness.class);
    final PodRepository podRepository;
    final TripRepository tripRepository;
    final SystemPropertyBusiness systemPropertyBusiness;
    final CommonBusiness commonBusiness;
    final PqaEnroutePodRepository pqaEnroutePodRepository;

    @Autowired
    public CommonCallPodBusiness(PodRepository podRepository,
                                 SystemPropertyBusiness systemPropertyBusiness,
                                 CommonBusiness commonBusiness,
                                 TripRepository tripRepository,
                                 PqaEnroutePodRepository pqaEnroutePodRepository) {
        this.podRepository = podRepository;
        this.systemPropertyBusiness = systemPropertyBusiness;
        this.commonBusiness = commonBusiness;
        this.tripRepository = tripRepository;
        this.pqaEnroutePodRepository = pqaEnroutePodRepository;
    }

    @Override
    public synchronized boolean  callPods(CallStation callStation, CallPod callPod) {
        boolean result = true;
        initCallStation(callStation);
        if (canGenerate(callStation, callPod)) {

            buildTrips(callStation, callPod);
            //保存工作站pod的工作量记录
            saveEnroutepodRecord(callStation, callPod);

        } else {
            result = false;
        }
        return result;
    }

    private void initCallStation(CallStation callStation) {
        setStationMaxPod(callStation);
        setStationMaxWorkLoad(callStation);
        setStationCycle(callStation);
    }

    public abstract void setStationMaxPod(CallStation callStation);

    public abstract void setStationMaxWorkLoad(CallStation callStation);

    public abstract void setStationCycle(CallStation callStation);

    public abstract boolean overMaxStationAvailablePod(CallStation callStation, CallPod callPod);

    public abstract boolean overMaxStationWork(CallStation callStation, CallPod callPod);

    public  boolean isResevedPod(Pod pod) {
        boolean reserved = false;

        Pod podTemp = podRepository.findOne(pod.getId());

        if (!podTemp.getState().equalsIgnoreCase(PodStateEnum.AVAILABLE.getName())) {
            LOGGER.debug("pod {} 已近被占用,状态不是AVAILABLE,", pod.getId());
            reserved = true;
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("pod {} 可用", pod.getId());
            }
            reservedPod(pod);

        }
        return reserved;
    }

    private boolean canGenerate(CallStation callStation, CallPod callPod) {

        if (overMaxStationAvailablePod(callStation, callPod)) {
            return false;
        }
        if (overMaxStationWork(callStation, callPod)) {
            return false;
        }
        if (isResevedPod(callPod.getPod())) {
            return false;
        }
        return true;
    }
    private boolean reservedPod(Pod pod) {

        return commonBusiness.reservePod(pod);
    }

    private void buildTrips(CallStation callStation, CallPod callPod) {
        // 生成POD的调度单
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始生成调度单 pod {} faces {}", callPod.getPod().getId(), JSONUtil.toJSon(callPod.getPodFaces()));
        }
        final Pod pod = callPod.getPod();
        Trip trip = new Trip();
        trip.setTripType(callStation.getStationType().getName());
        trip.setTripState(TripState.NEW.getName());
        trip.setPodId(pod.getId());
        trip.setWorkStationId(callStation.getWorkStation().getId());
        trip.setSectionId(pod.getSectionId());
        trip.setWarehouseId(pod.getWarehouseId());
        int positionNo = 0;
        // 获取POD旋转度数
        for (String face : callPod.getPodFaces()) {
            TripPosition tripPosition = new TripPosition();
            tripPosition.setWarehouseId(pod.getWarehouseId());
            tripPosition.setPositionNo(positionNo);
            tripPosition.setTripState(TripState.AVAILABLE.getName());
            tripPosition.setSectionId(pod.getSectionId());
            tripPosition.setPodUsingFace(face);
            trip.addPosition(tripPosition);
            positionNo++;
        }

        // 保存调度单
        tripRepository.saveAndFlush(trip);


    }

    private boolean saveEnroutepodRecord(CallStation callStation, CallPod callPod) {
        PqaEnroutePod pqaEnroutePod = new PqaEnroutePod();
        pqaEnroutePod.setWarehouseId(callStation.getWorkStation().getWarehouseId());
        pqaEnroutePod.setRouteType(callStation.getWorkStation().getTypeId());
        pqaEnroutePod.setStationId(callStation.getWorkStation().getId());
        pqaEnroutePod.setPodId(callPod.getPod().getId());
        pqaEnroutePod.setWorkLoad(new BigDecimal(callStation.getWorkStationWorkLoad()));
        PqaEnroutePod result = pqaEnroutePodRepository.save(pqaEnroutePod);
        return !ObjectUtils.isEmpty(result);

    }

}
