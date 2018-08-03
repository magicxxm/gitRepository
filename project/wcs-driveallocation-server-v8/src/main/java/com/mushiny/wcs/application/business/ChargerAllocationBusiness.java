package com.mushiny.wcs.application.business;

import com.mushiny.wcs.application.business.common.CommonBusiness;
import com.mushiny.wcs.application.business.common.SystemPropertyBusiness;
import com.mushiny.wcs.application.business.dto.RobotChargerScore;
import com.mushiny.wcs.application.business.score.RobotChargerScoreBusiness;
import com.mushiny.wcs.application.domain.*;
import com.mushiny.wcs.application.domain.enums.ChargeType;
import com.mushiny.wcs.application.domain.enums.TripState;
import com.mushiny.wcs.application.domain.enums.TripType;
import com.mushiny.wcs.application.repository.ChargerRepository;
import com.mushiny.wcs.application.repository.TripRepository;
import com.mushiny.wcs.application.repository.WCSRobotRepository;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class ChargerAllocationBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChargerAllocationBusiness.class);
    private final CommonBusiness commonBusiness;
    private final SystemPropertyBusiness systemPropertyBusiness;
    private final RobotChargerScoreBusiness robotChargerScoreBusiness;
    private final ChargerRepository chargerRepository;
    private final TripRepository tripRepository;
    private final WCSRobotRepository wcsRobotRepository;
    @Value("${mushiny.robot3.begain.index}")
    private Integer robot3Are=50;
    private List<String> gloableTemp=new ArrayList();
    @Autowired
    public ChargerAllocationBusiness(CommonBusiness commonBusiness,
                                     ChargerRepository chargerRepository,
                                     TripRepository tripRepository,
                                     WCSRobotRepository wcsRobotRepository,
                                     SystemPropertyBusiness systemPropertyBusiness,
                                     RobotChargerScoreBusiness robotChargerScoreBusiness) {
        this.commonBusiness = commonBusiness;
        this.chargerRepository = chargerRepository;
        this.tripRepository = tripRepository;
        this.wcsRobotRepository = wcsRobotRepository;
        this.systemPropertyBusiness = systemPropertyBusiness;
        this.robotChargerScoreBusiness = robotChargerScoreBusiness;

    }

    public void runChargerAllocation(Section section) {
        // 获取所有正常充电桩
        List<Charger> availableChargers = chargerRepository.getAvailableChargers(section.getId());
        if (availableChargers.isEmpty()) {
            LOGGER.info("section  {} 无可用的充电桩,充电桩出现故障", section.getName());
            return;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("section  {} 充电桩{}", section.getName(), JSONUtil.toJSon(availableChargers));
        }
        List<Charger> chargers = new ArrayList<>();
        for (Charger availableCharger : availableChargers) {
            // 获取充电桩当前位置的小车
            WCSRobot fullLaveBatteryRobot = wcsRobotRepository
                    .getByAddressCodeId(availableCharger.getPlaceMark(), availableCharger.getSectionId());

            // 检查充电桩前是否有小车
            if (fullLaveBatteryRobot == null) {
                // 检查充电桩是否已经存在调度单
                Trip trip = commonBusiness.getTripByCharger(availableCharger);
                if (trip == null) {
                    chargers.add(availableCharger);
                }
            } else {
                int driveChargerFull = systemPropertyBusiness
                        .getDriveChargerFullValue(availableCharger.getWarehouseId());
                int robotVoltageMaxValue=systemPropertyBusiness.getRobotVoltageMaxValue(availableCharger.getWarehouseId());
                int robotVoltageMinValue=systemPropertyBusiness.getRobotVoltageMinValue(availableCharger.getWarehouseId());

                // 判断小车电量是否达到系统最大值
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("section  {} 充电桩{} 下小车 {} 在充电", section.getName(), availableCharger.getName(), fullLaveBatteryRobot.getRobotId());

                    LOGGER.debug("小车 {} 当前电量{} 要求最大电量{} 当前电压{} 最小电压{} 最小电压{} ", fullLaveBatteryRobot.getRobotId(), fullLaveBatteryRobot.getLaveBattery(), driveChargerFull,fullLaveBatteryRobot.getVoltage(),robotVoltageMinValue,robotVoltageMaxValue);
                }


                if(fullLaveBatteryRobot.getVoltage()!=null&&fullLaveBatteryRobot.getVoltage()>=robotVoltageMaxValue)
                {
                    LOGGER.debug("小车 {} 电压{} 大于 系统设置最大电压{}", fullLaveBatteryRobot.getRobotId(), fullLaveBatteryRobot.getVoltage(), robotVoltageMaxValue);

                    boolean canBuild=commonBusiness.canGenerateEmptRun(section.getId(),fullLaveBatteryRobot.getRobotId());
                    if(canBuild)
                    {

                        buildMoveRobotTrip(availableCharger, fullLaveBatteryRobot);
                    }


                }else{


                    if (fullLaveBatteryRobot.getLaveBattery() >= driveChargerFull&&fullLaveBatteryRobot.getVoltage()>robotVoltageMinValue&&fullLaveBatteryRobot.getVoltage()<robotVoltageMaxValue) {
                        // 检查是否有新调度单-包括非充电调度单和emptyRun调度单
                        boolean canBuild=commonBusiness.canGenerateEmptRun(section.getId(),fullLaveBatteryRobot.getRobotId());
                        if (canBuild) {
                            // 生成把当前位置小车开走的调度单调度单
                            if (LOGGER.isDebugEnabled()) {

                                LOGGER.debug("小车 {} 可以调走", fullLaveBatteryRobot.getRobotId());
                            }
                            buildMoveRobotTrip(availableCharger, fullLaveBatteryRobot);
                            buildMustMoveRobotTrip(section,fullLaveBatteryRobot,availableCharger);
                        }
                    }else{
                        // 生成把当前位置小车开走的调度单调度单
                        if (LOGGER.isDebugEnabled()) {

                            LOGGER.debug("小车 {} 电压不满足调走条件", fullLaveBatteryRobot.getRobotId());
                        }
                    }
                    //查询是否有必须去充电的小车


                }

            }
        }
        // 检查是否存在可用充电桩
        if (chargers.isEmpty()) {
            return;
        }
        // 检查是否存在必须去充电的小车
        List<WCSRobot> robots = commonBusiness.getRobotsMustCharged(section);


        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("section  {} 必须去充电的小车{}", section.getName(), JSONUtil.toJSon(robots));

        }
        List<Charger> chargerType2=new ArrayList<>();
        List<Charger> chargerType3=new ArrayList<>();

        List<WCSRobot> type2Robot=new ArrayList<>();
        List<WCSRobot> type3Robot=new ArrayList<>();

        for(Charger cg:chargers)
        {
            if(cg.getChargerType()==ChargeType.MUSHINY_2.getType()||ChargeType.MEIDI.getType()==cg.getChargerType())
            {
                chargerType2.add(cg);
            }else{
                chargerType3.add(cg);
            }
        }

        for(WCSRobot wr:robots)
        {

            if(Integer.parseInt(wr.getRobotId())>robot3Are)
            {
                type3Robot.add(wr);
            }else {
                type2Robot.add(wr);
            }
        }
        buildMustChargerTrip(type2Robot,chargerType2);
        buildMustChargerTrip(type3Robot,chargerType3);
        // 检查是否存在可以去充电的小车
        robots = commonBusiness.getRobotsCanCharged(section);
        type2Robot.clear();
        type3Robot.clear();
        for(WCSRobot wr:robots)
        {

            if(Integer.parseInt(wr.getRobotId())>robot3Are)
            {
                type3Robot.add(wr);
            }else {
                type2Robot.add(wr);
            }
        }
        buildCanChargerTrip(type2Robot,chargerType2,section);
        buildCanChargerTrip(type3Robot,chargerType3,section);


    }

    private void buildCanChargerTrip(List<WCSRobot> robots ,List<Charger> chargers,Section section)
    {
        if (!CollectionUtils.isEmpty(robots)&&!CollectionUtils.isEmpty(chargers)) {
            List<RobotChargerScore> robotChargerScores = robotChargerScoreBusiness
                    .getScores(chargers, robots, section.getWarehouseId());
            robotChargerScores.sort(Comparator.comparing(RobotChargerScore::getScore));
            List<String> usedRobotIds = new ArrayList<>();
            List<String> usedChargerIds = new ArrayList<>();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("需要充电的小车排序\n {} ", JSONUtil.toJSon(robotChargerScores));
            }
            for (RobotChargerScore robotChargerScore : robotChargerScores) {
                //如果调度单还没有分配车时 ,先不去充电
              /*  if (CollectionUtils.isEmpty(commonBusiness.getAllocatedTrip(section))) {
                    continue;
                }*/
                Optional<String> usedPodId = usedChargerIds.stream()
                        .filter(str -> str.equalsIgnoreCase(robotChargerScore.getCharger().getId()))
                        .findFirst();
                if (usedPodId.isPresent()) {
                    continue;
                }
                Optional<String> usedRobotId = usedRobotIds.stream()
                        .filter(str -> str.equalsIgnoreCase(robotChargerScore.getRobot().getId()))
                        .findFirst();
                if (usedRobotId.isPresent()) {
                    continue;
                }
                buildChargerAllocationTrip(robotChargerScore.getCharger(), robotChargerScore.getRobot());
                usedChargerIds.add(robotChargerScore.getCharger().getId());
                usedRobotIds.add(robotChargerScore.getRobot().getId());
            }
        }
    }

    private void buildMustChargerTrip(List<WCSRobot> robots ,List<Charger> chargers)
    {

        if (!CollectionUtils.isEmpty(robots)&&!CollectionUtils.isEmpty(chargers)) {
            for (WCSRobot robot : robots) {
                //获取距离最近的充电桩
                BigDecimal chargerValue = BigDecimal.ZERO;
                Charger robotCharger = null;
                boolean chargerFlag = false;

                for (Charger charger : chargers) {

                    MapNode mapNode = commonBusiness.getChargerNode(charger);
                    BigDecimal value = BigDecimal.valueOf(
                            Math.abs(robot.getxPosition() - mapNode.getxPosition())
                                    + Math.abs(robot.getyPosition() - mapNode.getyPosition()));
                    if (!chargerFlag) {
                        chargerFlag = true;
                        chargerValue = value;
                        robotCharger = charger;
                    } else {
                        if (value.compareTo(chargerValue) < 0) {
                            chargerValue = value;
                            robotCharger = charger;
                        }
                    }
                }
                if (chargerFlag) {
                    buildChargerAllocationTrip(robotCharger, robot);
                    chargers.remove(robotCharger);
                    if (chargers.isEmpty()) {
                        return ;
                    }
                }
            }
        }
    }
    /**
     * 生成小车充电调度单
     */
    private void buildChargerAllocationTrip(Charger charger, WCSRobot robot) {
        // 给该充电桩绑定新的小车充电任务,生成调度单

        Trip trip = new Trip();
        trip.setTripType(TripType.CHARGER_DRIVE.getName());
        trip.setTripState(TripState.NEW.getName());
        trip.setChargerId(charger.getId());
        trip.setDriveId(robot.getRobotId());
        trip.setSectionId(charger.getSectionId());
        trip.setWarehouseId(charger.getWarehouseId());
        trip=tripRepository.saveAndFlush(trip);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("生成小车{} 调度单 {} 小车电量 {} ", robot.getRobotId(), trip.getId(), robot.getLaveBattery());
        }
    }

    /**
     * 生成小车移动到指定位置的调度单
     */
    private void buildMoveRobotTrip(Charger charger, WCSRobot wcsRobot) {
        // 生成调度单把充电桩前的小车开走
        Integer moveTargetAddress = commonBusiness.getMoveDriveTargetAddress2(charger);
        if (moveTargetAddress == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("section {} 小车{}无可用的休息区", wcsRobot.getSectionId(), wcsRobot.getRobotId());
            }
            return;
        }

        Trip moveTrip = new Trip();
        moveTrip.setTripType(TripType.MOVE_DRIVE.getName());
        moveTrip.setTripState(TripState.AVAILABLE.getName());
        moveTrip.setDriveId(wcsRobot.getRobotId());
        moveTrip.setEndAddress(moveTargetAddress);
        moveTrip.setSectionId(charger.getSectionId());
        moveTrip.setWarehouseId(charger.getWarehouseId());
        moveTrip= tripRepository.saveAndFlush(moveTrip);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("生成小车 {} 调走的调度单{}成功", wcsRobot.getRobotId(), moveTrip.getId());
        }
    }
    private void buildMustMoveRobotTrip(Section section, WCSRobot wcsRobot,Charger availableCharger ) {
        List<WCSRobot> canUse=wcsRobotRepository.driverCanUse(wcsRobot.getRobotId(),section.getId());
        if(!CollectionUtils.isEmpty(canUse))
        {
            List<WCSRobot> robots = commonBusiness.getRobotsMustCharged(section);
            if (!ObjectUtils.isEmpty(robots)) {
                for(WCSRobot wr:robots)
                {
                    gloableTemp.add(wr.getRobotId());
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("{}存在必须去充电的小车{} 将充电小车{}调走", section.getName(),JSONUtil.toJSon(gloableTemp));
                }
                gloableTemp.clear();
                LOGGER.info("开始生成强制调走小车开始");
                buildMoveRobotTrip(availableCharger, wcsRobot);
                LOGGER.info("强制调走小车结束");
            }else{
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("{}没有必须去充电的小车,不生成调度单", section.getName());
                }
            }

        }else{
            LOGGER.info("不需要强制调走{}时小车状态{}",wcsRobot.getRobotId(),JSONUtil.toJSon(wcsRobot));
        }
    }
}
