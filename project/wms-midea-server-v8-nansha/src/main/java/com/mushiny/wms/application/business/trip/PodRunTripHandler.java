package com.mushiny.wms.application.business.trip;

import com.mushiny.wms.application.business.common.CommonBusiness;
import com.mushiny.wms.application.business.score.DrivePodScoreBusiness;
import com.mushiny.wms.application.domain.Section;
import com.mushiny.wms.application.domain.Trip;
import com.mushiny.wms.application.domain.WCSRobot;
import com.mushiny.wms.application.domain.enums.TripType;
import com.mushiny.wms.application.repository.TripRepository;
import com.mushiny.wms.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/11/8.
 */
@Component
public class PodRunTripHandler extends TripHandler {
    private static final String[] podRunType = new String[]{TripType.POD_RUN.getName(),TripType.ANNTO_MVIN.getName(),TripType.ANNTOMVOUT.getName(),TripType.CARRY_POD.getName(),TripType.FINISHESWAREHOUSING.getName(),TripType.LMGETMATERIAL.getName()};
    private static final Logger LOGGER = LoggerFactory.getLogger(PodRunTripHandler.class);

    public PodRunTripHandler(CommonBusiness commonBusiness, TripRepository tripRepository, DrivePodScoreBusiness drivePodScoreBusiness) {
        super(commonBusiness, tripRepository, drivePodScoreBusiness);
    }

    @Override
    public List<Trip> filterTrip(List<Trip> trips) {
        return filterHandlerTrip(trips, podRunType);
    }


    @Override
    public void handleTrip(List<Trip> trips, Section section) {
        List<WCSRobot> roboots = getAvailableDrives(section.getId());
        if (LOGGER.isDebugEnabled()) {

            LOGGER.debug("开始处理podRun调度单,当前可用的车为{}", JSONUtil.toJSon(roboots));
        }
        if (roboots.isEmpty()) {

            return;
        }
        for (Trip trip : trips) {
            saveTrip(trip, roboots);
        }


    }
}
