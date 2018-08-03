package com.mushiny.wcs.application.timer;

import com.mushiny.wcs.application.domain.ReceiveStation;
import com.mushiny.wcs.application.domain.StowStation;
import com.mushiny.wcs.application.service.PodSelectionService;
import com.mushiny.wcs.application.service.StationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DriveAllocationTimer {
    private static final Logger LOG = LoggerFactory.getLogger(DriveAllocationTimer.class);

    private final PodSelectionService podSelectionService;
    private final StationService stationService;

    @Autowired
    public DriveAllocationTimer(PodSelectionService podSelectionService,
                                StationService stationService) {
        this.podSelectionService = podSelectionService;
        this.stationService = stationService;
    }
    @Scheduled(fixedDelay=5000)
    public void runDriveAllocation() {
        // 获取所有收货工作站
        List<ReceiveStation> receiveStations = stationService.getAllReceiveStations();
        for(ReceiveStation receiveStation : receiveStations){
            try {
                podSelectionService.callReceiveStationPods(receiveStation);
            }catch (Exception e){

                LOG.error(e.getMessage(),e);
            }
        }
        // 获取所有上架工作站
        List<StowStation> stowStations = stationService.getAllStowStations();
        for(StowStation stowStation : stowStations){
            try {
                podSelectionService.callStowStationPods(stowStation);
            }catch (Exception e){

                LOG.error(e.getMessage(),e);
            }
        }
    }

    public interface Task{
        void run();


    }
}
