package com.mushiny.wcs.application.business.trip;

import com.mushiny.wcs.application.business.common.CommonBusiness;
import com.mushiny.wcs.application.business.common.SystemPropertyBusiness;
import com.mushiny.wcs.application.business.score.DrivePodScoreBusiness;
import com.mushiny.wcs.application.domain.Charger;
import com.mushiny.wcs.application.domain.Section;
import com.mushiny.wcs.application.domain.Trip;
import com.mushiny.wcs.application.domain.WCSRobot;
import com.mushiny.wcs.application.domain.enums.TripState;
import com.mushiny.wcs.application.domain.enums.TripType;
import com.mushiny.wcs.application.repository.ChargerRepository;
import com.mushiny.wcs.application.repository.TripRepository;
import com.mushiny.wcs.application.repository.WCSRobotRepository;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/11/8.
 */
@Component
public class ChargerDriveTripHandler extends TripHandler {
    private static final String[] chargerDrive = new String[]{TripType.CHARGER_DRIVE.getName()};
    private static final Logger LOGGER = LoggerFactory.getLogger(ChargerDriveTripHandler.class);
    private final SystemPropertyBusiness systemPropertyBusiness;
    private final WCSRobotRepository wcsRobotRepository;
    private final ChargerRepository chargerRepository;

    public ChargerDriveTripHandler(CommonBusiness commonBusiness, TripRepository tripRepository, DrivePodScoreBusiness drivePodScoreBusiness, SystemPropertyBusiness systemPropertyBusiness, WCSRobotRepository wcsRobotRepository, ChargerRepository chargerRepository) {
        super(commonBusiness, tripRepository, drivePodScoreBusiness);
        this.systemPropertyBusiness = systemPropertyBusiness;
        this.wcsRobotRepository = wcsRobotRepository;
        this.chargerRepository = chargerRepository;
    }

    @Override
    public List<Trip> filterTrip(List<Trip> trips) {
        return filterHandlerTrip(trips, chargerDrive);
    }

    /**
     * 生成小车移动到指定位置的调度单
     */
    private void buildMoveRobotTrip(Charger charger, WCSRobot wcsRobot) {
        // 生成调度单把充电桩前的小车开走
        Integer moveTargetAddress = commonBusiness.getMoveDriveTargetAddress(charger);
        if (moveTargetAddress == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("section {} 无可用的休息区---小车{}", wcsRobot.getSectionId(), wcsRobot.getRobotId());
            }
            return;
        }
        // 给该充电桩绑定新的小车充电任务,生成调度单
        Trip moveTrip = new Trip();
        moveTrip.setTripType(TripType.MOVE_DRIVE.getName());
        moveTrip.setTripState(TripState.AVAILABLE.getName());
        moveTrip.setDriveId(wcsRobot.getRobotId());
        moveTrip.setEndAddress(moveTargetAddress);
        moveTrip.setSectionId(charger.getSectionId());
        moveTrip.setWarehouseId(charger.getWarehouseId());
        tripRepository.saveAndFlush(moveTrip);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("生成小车 {} 调走的调度单{}成功", wcsRobot.getRobotId(), moveTrip.getId());
        }
    }

    @Override
    public void handleTrip(List<Trip> trips, Section section) {

       /* int driveChargerFull = systemPropertyBusiness
                .getDriveChargerFullValue(section.getWarehouseId());
*/
        for (Trip trip : trips) {
            trip.setTripState(TripState.AVAILABLE.getName());
            tripRepository.saveAndFlush(trip);
        }
       /* List<WCSRobot> chargeRobots=wcsRobotRepository.getWCSRobotByCharger(section.getId());
        if(!CollectionUtils.isEmpty(chargeRobots))
        {
            for(WCSRobot robot:chargeRobots)
            {
                Charger availableCharger=chargerRepository.getChargersByPlaceMark(section.getId(),robot.getAddressCodeId());
                // 判断小车电量是否达到系统最大值
                if(ObjectUtils.isEmpty(availableCharger)){
                    if(LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug("跟据小车{}的位置{}获取充电桩失败",robot.getRobotId(),robot.getAddressCodeId());
                    }
                    continue;
                }
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("小车 {} 在充电桩{}充电, 当前电量{}, 要求最大电量{}  ",robot.getRobotId(),availableCharger.getName(),robot.getLaveBattery(),driveChargerFull);
                }

                if (robot.getLaveBattery() >= driveChargerFull) {
                    // 生成把当前位置小车开走的调度单调度单
                    buildMoveRobotTrip(availableCharger, robot);
                }
            }
        }*/

    }


}

