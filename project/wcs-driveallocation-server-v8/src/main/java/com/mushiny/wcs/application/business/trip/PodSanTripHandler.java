package com.mushiny.wcs.application.business.trip;

import com.mushiny.wcs.application.business.common.CommonBusiness;
import com.mushiny.wcs.application.business.score.DrivePodScoreBusiness;
import com.mushiny.wcs.application.config.MapNodeConfig;
import com.mushiny.wcs.application.domain.*;
import com.mushiny.wcs.application.domain.enums.TripType;
import com.mushiny.wcs.application.repository.TripRepository;
import com.mushiny.wcs.application.service.impl.DriveAllocationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author:
 * @Description: Created by wangjianwei on 2017/10/21.
 */
@Component
public class PodSanTripHandler extends TripHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PodSanTripHandler.class);
    private static final String[] podScanType = new String[]{TripType.POD_SCAN.getName()

    };

    @Autowired
    public PodSanTripHandler(CommonBusiness commonBusiness, TripRepository tripRepository, DrivePodScoreBusiness drivePodScoreBusiness) {
        super(commonBusiness, tripRepository, drivePodScoreBusiness);
    }

    @Override
    public List<Trip> filterTrip(List<Trip> trips) {
        return filterHandlerTrip(trips, podScanType);
    }

    @Override
    public void handleTrip(List<Trip> trips, Section section) {

        if (CollectionUtils.isEmpty(getAvailableDrives(section.getId()))) {
            for (Trip trip : trips) {
                String paths = trip.getPodScanpath();
                Assert.isNull(paths, "trip  " + trip.getId() + " scanPath 不能为空");
                String[] pathsTemp = paths.split(",");
                if (!StringUtils.isEmpty(pathsTemp[0])) {
                    MapNode mapNode = MapNodeConfig.findSectionMapNode(Integer.parseInt(pathsTemp[0]), trip.getSectionId()).getCurrentMapNode();
                    WCSRobot temp = getMoveDriveTargetAddress(mapNode, getAvailableDrives(section.getId()));
                    if (!allocated.contains(temp.getRobotId())) {
                        trip.setDriveId(temp.getRobotId());
                        tripRepository.saveAndFlush(trip);
                        allocated.add(temp.getRobotId());
                    }


                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("trip {} scanPath 不和法---{}", trip.getId(), paths);
                    }
                }


            }

        }
    }

    /**
     * 获取小车停放位置
     */
    public WCSRobot getMoveDriveTargetAddress(MapNode baseMapNode, List<WCSRobot> WCSRobots) {


        WCSRobot minWCSRobot = null;
        boolean flag = true;
        for (WCSRobot wcsRobot : WCSRobots) {
            if (flag) {
                flag = false;
                minWCSRobot = wcsRobot;
                continue;
            }
            int nodeValue = Math.abs(wcsRobot.getxPosition() - baseMapNode.getxPosition())
                    + Math.abs(wcsRobot.getyPosition() - baseMapNode.getyPosition());
            int minValue = Math.abs(minWCSRobot.getxPosition() - baseMapNode.getxPosition())
                    + Math.abs(minWCSRobot.getyPosition() - baseMapNode.getyPosition());
            if (nodeValue < minValue) {
                minWCSRobot = wcsRobot;
            }
        }
        return minWCSRobot;
    }


}
