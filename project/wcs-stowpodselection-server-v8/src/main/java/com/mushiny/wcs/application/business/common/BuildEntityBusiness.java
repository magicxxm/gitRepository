package com.mushiny.wcs.application.business.common;

import com.mushiny.wcs.application.domain.*;
import com.mushiny.wcs.application.domain.enums.PodStateEnum;
import com.mushiny.wcs.application.domain.enums.TripState;
import com.mushiny.wcs.application.domain.enums.TripType;
import com.mushiny.wcs.application.respository.EnRoutePodRepository;
import com.mushiny.wcs.application.respository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BuildEntityBusiness {

    @Autowired
    private EnRoutePodRepository enRoutePodRepository;
    @Autowired
    private TripRepository tripRepository;


    /**
     * 构建调度单
     */
    public void buildTrip(Pod pod,
                          WorkStation workStation,
                          List<String> availableFaces){
        // 生成POD的调度单
        Trip trip = new Trip();
        trip.setTripType(TripType.STOW_POD.getName());
        trip.setTripState(TripState.NEW.getName());
        trip.setPodId(pod.getId());
        trip.setWorkStationId(workStation.getId());
        trip.setSectionId(workStation.getSectionId());
        trip.setWarehouseId(workStation.getWarehouseId());
        // 生成POD调度单中POD面的明细
        int count = 0;
        for(String availableFace : availableFaces){
            // 获取POD旋转度数
            TripPosition tripPosition = new TripPosition();
            tripPosition.setSectionId(pod.getSectionId());
            tripPosition.setWarehouseId(pod.getWarehouseId());
            tripPosition.setPositionNo(count++);
            tripPosition.setTripState(TripState.AVAILABLE.getName());
            tripPosition.setPodUsingFace(availableFace);
            trip.addPosition(tripPosition);
        }
        // 保存调度单
        tripRepository.saveAndFlush(trip);
    }
    /**
     * 构建在途POD
     */
    public void buildEnRoutePod(Pod pod,
                                StowStation stowStation){
        EnRoutePod enRoutePod = new EnRoutePod();
        enRoutePod.setPodId(pod.getId());
        enRoutePod.setStowStationId(stowStation.getId());
        enRoutePod.setWarehouseId(pod.getWarehouseId());
        enRoutePodRepository.saveAndFlush(enRoutePod);
    }

    /**
     * 构建在途POD
     */
    public void buildEnRoutePod(Pod pod,
                                ReceiveStation receiveStation){
        EnRoutePod enRoutePod = new EnRoutePod();
        enRoutePod.setPodId(pod.getId());
        enRoutePod.setReceiveStationId(receiveStation.getId());
        enRoutePod.setWarehouseId(pod.getWarehouseId());
        enRoutePodRepository.saveAndFlush(enRoutePod);
    }
}
