package com.mushiny.wcs.application.service.impl;

import com.mushiny.wcs.application.business.PodSelectionBusiness;
import com.mushiny.wcs.application.business.common.SystemPropertyBusiness;
import com.mushiny.wcs.application.domain.*;
import com.mushiny.wcs.application.exception.CustomException;
import com.mushiny.wcs.application.respository.EnRoutePodRepository;
import com.mushiny.wcs.application.service.PodSelectionService;
import com.mushiny.wcs.common.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Transactional
public class PodSelectionServiceImpl implements PodSelectionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PodSelectionServiceImpl.class);

    private final EnRoutePodRepository enRoutePodRepository;
    private final SystemPropertyBusiness systemPropertyBusiness;
    private final PodSelectionBusiness podSelectionBusiness;

    @Autowired
    public PodSelectionServiceImpl(EnRoutePodRepository enRoutePodRepository,
                                   SystemPropertyBusiness systemPropertyBusiness,
                                   PodSelectionBusiness podSelectionBusiness) {
        this.enRoutePodRepository = enRoutePodRepository;
        this.systemPropertyBusiness = systemPropertyBusiness;
        this.podSelectionBusiness = podSelectionBusiness;
    }

    @Override
    public void callReceiveStationPods(ReceiveStation receiveStation) {
        callStationPod(receiveStation,null);
    }

    @Override
    public void callStowStationPods(StowStation stowStation) {
        callStationPod(null,stowStation);
    }

    /**
     * 给工作站分配POD
     */
    private void callStationPod(ReceiveStation receiveStation, StowStation stowStation){
        WorkStation workStation;
        long stationAvailablePod;
        List<StorageLocationType> storageLocationTypes;
        if(receiveStation != null){
            workStation = receiveStation.getWorkStation();
            stationAvailablePod = enRoutePodRepository.countByStationId(receiveStation.getId());
            storageLocationTypes = receiveStation.getStorageLocationTypes();
        }else if(stowStation != null){
            workStation = stowStation.getWorkStation();
            stationAvailablePod = enRoutePodRepository.countByStationId(stowStation.getId());
            storageLocationTypes = stowStation.getStorageLocationTypes();
        }else {
            throw new ApiException(CustomException.EX_SPS_STATION_NOT_FOUND.toString());
        }
        if(!workStation.isCallPod()){
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("工作站{}停止呼叫",workStation.getName());
            }
            return;
        }
        String warehouseId = workStation.getWarehouseId();
        // 获取工作站容许绑定的最大POD数量
        int stationMaxPod = systemPropertyBusiness.getStowPodStationMaxPod(warehouseId);
        // 检查是否需要POD
        if(stationAvailablePod >= stationMaxPod){
            if(LOGGER.isDebugEnabled())
            {
                String stationId= ObjectUtils.isEmpty(receiveStation)?stowStation.getId():receiveStation.getId();
                LOGGER.debug("工作站{} 当前pod{}.允许最大{}",stationId,stationAvailablePod,stationMaxPod);
            }
            return;
        }
        // 检查改工作站下是否已经配置了StorageLocationTypes
        if(storageLocationTypes.isEmpty()){
            throw new ApiException(CustomException.EX_SPS_STATION_BIN_TYPES_IS_EMPTY.toString());
        }
        int podAmount = (int) (stationMaxPod - stationAvailablePod);
        podSelectionBusiness.callStowPods(stowStation,receiveStation,storageLocationTypes,workStation,podAmount);

    }
}
