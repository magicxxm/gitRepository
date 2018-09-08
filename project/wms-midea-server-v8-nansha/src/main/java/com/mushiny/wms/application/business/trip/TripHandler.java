package com.mushiny.wms.application.business.trip;

import com.mushiny.wms.application.business.common.CommonBusiness;
import com.mushiny.wms.application.business.dto.DrivePodScore;
import com.mushiny.wms.application.business.score.DrivePodScoreBusiness;
import com.mushiny.wms.application.domain.Section;
import com.mushiny.wms.application.domain.Trip;
import com.mushiny.wms.application.domain.WCSRobot;
import com.mushiny.wms.application.domain.enums.TripState;
import com.mushiny.wms.application.repository.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author:
 * @Description: Created by wangjianwei on 2017/10/21.
 */
public abstract class TripHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripHandler.class);
    public final CommonBusiness commonBusiness;
    public final TripRepository tripRepository;
    public final DrivePodScoreBusiness drivePodScoreBusiness;
    protected TripHandler handler = null;
    public static List<String> allocated = new ArrayList<>();

    public TripHandler(CommonBusiness commonBusiness, TripRepository tripRepository, DrivePodScoreBusiness drivePodScoreBusiness) {
        this.commonBusiness = commonBusiness;
        this.tripRepository = tripRepository;
        this.drivePodScoreBusiness = drivePodScoreBusiness;
    }

    public List<WCSRobot> getAvailableDrives(String sectionId) {
        List<WCSRobot> robots = commonBusiness.getAvailableDrives(sectionId);

        return robots;
    }

    public List<DrivePodScore> getDrivePodScores(Trip trip,
                                                 List<WCSRobot> robots) {
        List<DrivePodScore> podScores = drivePodScoreBusiness.getDrivePodScores(trip, robots);
        return podScores;
    }

    public boolean saveTrip(Trip trip, List<WCSRobot> robots) {

        boolean result = false;
        //新增加
       /* List<Trip> countworkStation=commonBusiness.checkingDriveCanUse(trip);
        if(!CollectionUtils.isEmpty(countworkStation)&&countworkStation.size()>5)
        {
            return false;
        }*/
        ///////

        List<DrivePodScore> drivePodScores = drivePodScoreBusiness.getDrivePodScores(trip, robots);
            int driverSize = robots.size();
            for (int m = 0; m < driverSize; m++) {
                final String driverId = drivePodScores.get(m).getDriveId();
                try {
                boolean driveCanUse = commonBusiness.checkingDriveCanUse(driverId, trip);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("可以给调度单{} 分配小车 {} {} ", trip.getId(), driverId, driveCanUse);
                }
                if (driveCanUse && !allocated.contains(driverId)) {
                    trip.setDriveId(driverId);
                    trip.setTripState(TripState.AVAILABLE.getName());
                    tripRepository.saveAndFlush(trip);
                    result = true;
                    allocated.add(driverId);
                    break;
                }
            }catch (Exception e)
            {
                LOGGER.error("给调度单"+trip.getId()+"分配车"+driverId+"失败"+e.getMessage(),e);
                throw e;
            }
    }


        return result;

    }

    public List<Trip> filterHandlerTrip(List<Trip> trips, String... tripTypes) {

        List<Trip> result = new ArrayList();
        List<String> tripTypeTemp = Arrays.asList(tripTypes);
        for (Trip trip : trips) {
            if (tripTypeTemp.contains(trip.getTripType())) {
                result.add(trip);
            }
        }
        return result;
    }

    public abstract List<Trip> filterTrip(List<Trip> trips);

    public abstract void handleTrip(List<Trip> trips, Section section);

    public TripHandler getHandler() {
        return handler;
    }

    public void setHandler(TripHandler handler) {
        this.handler = handler;
    }

    public void execute(List<Trip> trips, Section section) {
        List<Trip> allTrips = new ArrayList<>();
        allTrips.addAll(trips);
        List<Trip> canAllocationTrips = filterTrip(allTrips);
        allTrips.removeAll(canAllocationTrips);
        handleTrip(canAllocationTrips, section);
        if (!ObjectUtils.isEmpty(getHandler())) {
            getHandler().execute(allTrips, section);
        }

    }
}
