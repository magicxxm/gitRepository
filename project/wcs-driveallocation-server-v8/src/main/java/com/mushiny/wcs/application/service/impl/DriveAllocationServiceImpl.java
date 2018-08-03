package com.mushiny.wcs.application.service.impl;

import com.mushiny.wcs.application.business.ChargerAllocationBusiness;
import com.mushiny.wcs.application.business.trip.ApplicationTripHandlerFactory;
import com.mushiny.wcs.application.business.trip.TripHandler;
import com.mushiny.wcs.application.domain.Section;
import com.mushiny.wcs.application.domain.Trip;
import com.mushiny.wcs.application.repository.SectionRepository;
import com.mushiny.wcs.application.service.DriveAllocationService;
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

    @Autowired
    public DriveAllocationServiceImpl(SectionRepository sectionRepository, ChargerAllocationBusiness chargerAllocationBusiness) {

        this.sectionRepository = sectionRepository;
        this.chargerAllocationBusiness = chargerAllocationBusiness;

    }

    @Override
    public void runDriveAllocation() {
        // 获取所有SECTION

        List<Section> sections=new ArrayList<>();
         sections = sectionRepository.getAll();
        for (Section section : sections) {
            //生成小车充电任务
            try{
                chargerAllocationBusiness.runChargerAllocation(section);
            }catch (Exception e)
            {
                LOGGER.error("生成充电任务失败"+e.getMessage(),e);
            }
                TripHandler tripHandler = ApplicationTripHandlerFactory.getInstance();
                List<Trip> trips = tripHandler.commonBusiness.getAllNewTrip(section.getId());
                tripHandler.allocated.clear();
                if (!CollectionUtils.isEmpty(trips)) {
                    tripHandler.execute(trips, section);
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("section {} {} 无调度单可分配车", section.getName(), section.getId());
                    }
                }



        }

    }
}
