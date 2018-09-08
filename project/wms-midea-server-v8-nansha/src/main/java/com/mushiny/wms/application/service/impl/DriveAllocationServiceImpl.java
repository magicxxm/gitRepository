package com.mushiny.wms.application.service.impl;

import com.mushiny.wms.application.business.ChargerAllocationBusiness;
import com.mushiny.wms.application.business.common.CommonBusiness;
import com.mushiny.wms.application.business.trip.ApplicationTripHandlerFactory;
import com.mushiny.wms.application.business.trip.PodRunTripHandler;
import com.mushiny.wms.application.business.trip.TripHandler;
import com.mushiny.wms.application.domain.Section;
import com.mushiny.wms.application.domain.Trip;
import com.mushiny.wms.application.domain.WCSRobot;
import com.mushiny.wms.application.domain.enums.TripState;
import com.mushiny.wms.application.domain.enums.TripType;
import com.mushiny.wms.application.repository.SectionRepository;
import com.mushiny.wms.application.repository.TripRepository;
import com.mushiny.wms.application.service.DriveAllocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DriveAllocationServiceImpl implements DriveAllocationService {
    private final SectionRepository sectionRepository;
    private final ChargerAllocationBusiness chargerAllocationBusiness;
    private static final Logger LOGGER = LoggerFactory.getLogger(DriveAllocationServiceImpl.class);
    private final CommonBusiness commonBusiness;
    private final TripRepository tripRepository;
    @Autowired
    public DriveAllocationServiceImpl(SectionRepository sectionRepository, ChargerAllocationBusiness chargerAllocationBusiness,CommonBusiness commonBusiness,TripRepository tripRepository) {

        this.sectionRepository = sectionRepository;
        this.chargerAllocationBusiness = chargerAllocationBusiness;
        this.commonBusiness=commonBusiness;
        this.tripRepository=tripRepository;
    }

    @Override
    public void runDriveAllocation() {
        // 获取所有SECTION

        List<Section> sections=new ArrayList<>();
         sections = sectionRepository.getAll();
        for (Section section : sections) {
            //生成小车充电任务

            doAllocation(section);


        }

    }

    private void doAllocation(Section section)
    {
        try{
            chargerAllocationBusiness.runChargerAllocation(section);
        }catch (Exception e)
        {
            LOGGER.error("生成充电任务失败"+e.getMessage(),e);
        }
        TripHandler tripHandler=ApplicationTripHandlerFactory.getInstance();
        List<Trip> trips = tripHandler.commonBusiness.getAllNewTrip(section.getId());
        tripHandler.allocated.clear();
        if (!CollectionUtils.isEmpty(trips)) {
            tripHandler.execute(trips, section);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("section {} {} 无调度单可分配车", section.getName(), section.getId());
            }
        }
        agvDriver(section.getId());



    }
    private void agvDriver(String sectionId){
        List<WCSRobot> robots= commonBusiness.getAvailableDrives2(sectionId);
        for(WCSRobot wr:robots)
        {
            try{
                Integer moveTargetAddress= commonBusiness.getMoveDriveTargetAddress3(sectionId,wr.getAddressCodeId());

                Trip moveTrip = new Trip();
                moveTrip.setTripType(TripType.MOVE_DRIVE.getName());
                moveTrip.setTripState(TripState.AVAILABLE.getName());
                moveTrip.setDriveId(wr.getRobotId());
                moveTrip.setEndAddress(moveTargetAddress);
                moveTrip.setSectionId(sectionId);
                moveTrip.setWarehouseId(wr.getWarehouseId());
                moveTrip= tripRepository.saveAndFlush(moveTrip);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("生成出库小车 {} 调走的调度单{}成功", wr.getRobotId(), moveTrip.getId());
                }
            }catch (Exception e)
            {
                LOGGER.error("生成出库小车失败"+e.getMessage(),e);
            }

        }

    }
}
