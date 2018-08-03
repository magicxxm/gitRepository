package com.mushiny.wcs.application.business.common;


import com.mushiny.wcs.application.domain.*;
import com.mushiny.wcs.application.domain.enums.TripState;
import com.mushiny.wcs.application.domain.enums.TripType;
import com.mushiny.wcs.application.repository.*;
import com.mushiny.wcs.common.exception.ApiException;
import com.mushiny.wcs.common.exception.ExceptionEnum;
import com.mushiny.wcs.common.utils.EntityManagerUtil;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.Map;

@Component
@Transactional
public class CommonBusiness {

    private final TripRepository tripRepository;
    private final PodRepository podRepository;
    private final WCSRobotRepository wcsRobotRepository;
    private final MapNodeRepository mapNodeRepository;
    private final MapRepository mapRepository;
    private final SystemPropertyBusiness systemPropertyBusiness;
    private final EntityManagerUtil entityManagerUtil;
    @Value("${mushiny.emptyRun.endAddress.enable}")
    private Boolean enable=true;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonBusiness.class);

    @Autowired
    public CommonBusiness(TripRepository tripRepository,
                          WCSRobotRepository wcsRobotRepository, MapNodeRepository mapNodeRepository, EntityManagerUtil entityManagerUtil, PodRepository podRepository, SystemPropertyBusiness systemPropertyBusiness, MapRepository mapRepository) {
        this.tripRepository = tripRepository;
        this.wcsRobotRepository = wcsRobotRepository;
        this.mapNodeRepository = mapNodeRepository;
        this.podRepository = podRepository;
        this.entityManagerUtil = entityManagerUtil;
        this.systemPropertyBusiness = systemPropertyBusiness;
        this.mapRepository = mapRepository;
    }

    /**
     * 获取可以去充电的小车,并且小车没有充电任务单
     */
    public List<WCSRobot> getRobotsCanCharged(Section section) {
        int maxLaveBattery = systemPropertyBusiness.getDriveOutChargerMinValue(section.getWarehouseId());
        int minLaveBattery = systemPropertyBusiness.getDriveInChargerMinValue(section.getWarehouseId());
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        tripStates.add(TripState.PROCESS.getName());
        String tripType = TripType.CHARGER_DRIVE.getName();
        return wcsRobotRepository.getRobotsCanCharged(tripType, tripStates, section.getId(), minLaveBattery, maxLaveBattery);
    }

    /**
     * 获取必须去充电的小车,并且小车没有充电任务单
     */
    public List<WCSRobot> getRobotsMustCharged(Section section) {
        int minLaveBattery = systemPropertyBusiness.getDriveInChargerMinValue(section.getWarehouseId());
        int minValueVoltage = systemPropertyBusiness.getRobotVoltageMinValue(section.getWarehouseId());
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        tripStates.add(TripState.PROCESS.getName());
        String tripType = TripType.CHARGER_DRIVE.getName();
        return wcsRobotRepository.getRobotsMustCharged(tripType, tripStates, section.getId(), minValueVoltage,minLaveBattery);
    }


    /**
     * 获取小车低于最小电压,并且小车没有充电任务单
     */
    public List<WCSRobot> getRobotsVoltageMinValue(Section section) {
        int minLaveBattery = systemPropertyBusiness.getRobotVoltageMinValue(section.getWarehouseId());
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        tripStates.add(TripState.PROCESS.getName());
        String tripType = TripType.CHARGER_DRIVE.getName();
        return wcsRobotRepository.getRobotsVoltageMinValue(tripType, tripStates, section.getId(), minLaveBattery);
    }

    /**
     * 判断休息区是否可用
     */
    public List<WCSRobot> getSleepAddrRobots(int addr,String sectionId) {

        return wcsRobotRepository.getSleepAddrRobots(addr, sectionId);
    }

    public MapNode getChargerNode(Charger charger) {
        com.mushiny.wcs.application.domain.Map map = mapRepository.getBySectionId(charger.getSectionId());
        if (map == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        MapNode chargerNode = mapNodeRepository.getByAddressCodeId(map.getId(), charger.getPlaceMark());
        if (chargerNode == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        return chargerNode;
    }

    public List<Trip> getAllocatedTrip(Section section) {
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        tripStates.add(TripState.PROCESS.getName());
        tripStates.add(TripState.LEAVING.getName());
        return tripRepository.getAllocatedTrip(section.getId(), tripStates);
    }

    public Integer getMoveDriveTargetAddress(Charger charger) {
        com.mushiny.wcs.application.domain.Map map = mapRepository.getBySectionId(charger.getSectionId());
        if (map == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        MapNode chargerNode = getChargerNode(charger);
        List<MapNode> mapNodes = mapNodeRepository.getByMapId(map.getId());
        if (mapNodes.isEmpty()) {
            return null;
        }
        MapNode minValueNode = null;
        boolean flag = true;
        for (MapNode mapNode : mapNodes) {
            //小车可能已经在休息区
            List<WCSRobot> sleepAddrRoots=getSleepAddrRobots(mapNode.getAddressCodeId(),map.getSectionId());
           //充电结束要去的休息区
            List<Trip> notFinishEmptRunTrip=tripRepository.getSleepAddrRobots(mapNode.getAddressCodeId(),map.getSectionId());
            // 小车的address 地址在休息或者emptRun的调度单没有finish调时 判断休息区有小车
            if(!CollectionUtils.isEmpty(sleepAddrRoots)|| !ObjectUtils.isEmpty(notFinishEmptRunTrip))
            {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("section {} 休息区{} 有小车{}\n 或者有未finish的EmptRunTrip{}", map.getSectionId(), mapNode.getAddressCodeId(), JSONUtil.toJSon(sleepAddrRoots),JSONUtil.toJSon(notFinishEmptRunTrip));
                }
                continue;
            }
            if (flag) {
                flag = false;
                minValueNode = mapNode;
                continue;
            }
            int nodeValue = Math.abs(mapNode.getxPosition() - chargerNode.getxPosition())
                    + Math.abs(mapNode.getyPosition() - chargerNode.getyPosition());
            int minValue = Math.abs(minValueNode.getxPosition() - chargerNode.getxPosition())
                    + Math.abs(minValueNode.getyPosition() - chargerNode.getyPosition());
            if (nodeValue < minValue) {
                minValueNode = mapNode;
            }
        }
        if(!ObjectUtils.isEmpty(minValueNode))
        {
            return minValueNode.getAddressCodeId();
        }else{
            return null;
        }

    }


    public Integer getMoveDriveTargetAddress2(Charger charger) {
        if(!enable)
        {
            return 0;
        }
        Integer result=null;
        String sql="SELECT WD_NODE.ADDRESSCODEID,WD_NODE.XPOSITION,WD_NODE.YPOSITION  FROM MD_POD INNER JOIN WD_NODE ON MD_POD.PLACEMARK=WD_NODE.ADDRESSCODEID WHERE MD_POD.ID NOT IN(SELECT DISTINCT IFNULL(RCS_TRIP.POD_ID,'') FROM RCS_TRIP WHERE RCS_TRIP.TRIP_STATE!='Finish' ) AND MD_POD.PLACEMARK NOT IN (SELECT WCS_ROBOT.ADDRESSCODEID FROM WCS_ROBOT ) AND MD_POD.PLACEMARK NOT IN (SELECT DISTINCT RCS_TRIP.END_ADDRESS FROM RCS_TRIP WHERE RCS_TRIP.TRIP_STATE!='Finish' AND RCS_TRIP.END_ADDRESS is not null ) AND MAP_ID=:mapId AND WD_NODE.TYPE=1 order by WD_NODE.ADDRESSCODEID";
        com.mushiny.wcs.application.domain.Map map = mapRepository.getBySectionId(charger.getSectionId());
        if (map == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        MapNode chargerNode = getChargerNode(charger);
        Map param = new HashMap();
        param.put("mapId", map.getId());
        List<Map> results = entityManagerUtil.executeNativeQuery(sql, param);
        Map minValueNode = null;
        boolean flag = true;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("section {}小车充电结束可以去的位置{}\n ",charger.getName(),JSONUtil.toJSon(results) );
        }

        Map<String,Map> cal=new HashMap<>();
        for(Map temp:results)
        {
            if(minValueNode==null)
            {
                minValueNode=temp;

            }else
            {
                int nodeValue = Math.abs(Integer.valueOf((int)temp.get("XPOSITION")) - chargerNode.getxPosition())
                        + Math.abs(Integer.valueOf((int)temp.get("YPOSITION")) - chargerNode.getyPosition());
                int minValue = Math.abs(Integer.valueOf((int)minValueNode.get("XPOSITION")) - chargerNode.getxPosition())
                        + Math.abs(Integer.valueOf((int)minValueNode.get("YPOSITION")) - chargerNode.getyPosition());
                if (nodeValue < minValue) {
                    minValueNode = temp;
                }

            }
        }

        if(minValueNode!=null)
        {
            result= Integer.valueOf((int)minValueNode.get("ADDRESSCODEID"));
        }
        return result;


    }
    /**
     * 获取小车调度单
     */
    public Trip getTripByWCSRobot(WCSRobot wcsRobot) {
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        tripStates.add(TripState.PROCESS.getName());
        return tripRepository.getByRobotId(wcsRobot.getRobotId(), tripStates);
    }


    /**
     * 获取充电桩调度单
     */
    public Trip getTripByCharger(Charger charger) {
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        tripStates.add(TripState.PROCESS.getName());
        return tripRepository.getByChargerId(charger.getId(), tripStates);
    }

    /**
     * 获取所有未分配小车的调度单
     */
    public List<Trip> getAllNewTrip(String sectionId) {
        List<String> tripTypes = new ArrayList<>();
        tripTypes.add(TripType.PICK_POD.getName());
        tripTypes.add(TripType.STOW_POD.getName());
        tripTypes.add(TripType.IBP_POD.getName());
        tripTypes.add(TripType.OBP_POD.getName());
        tripTypes.add(TripType.ICQA_POD.getName());
        tripTypes.add(TripType.POD_SCAN.getName());
        tripTypes.add(TripType.POD_RUN.getName());
        tripTypes.add(TripType.CHARGER_DRIVE.getName());
        String tripState = TripState.NEW.getName();
        return tripRepository.getAllNewTrips(tripTypes, tripState, sectionId);
    }

    public boolean canGenerateEmptRun(String sectionId,String driveId )
    {
        List<Trip> trips=getAllNotChargeTrips(sectionId,driveId);
        List<Trip> temp= getAllEmptyRunTrips(sectionId,driveId);
        List<Trip> newTrips=getAllNotChargeNewTrips(sectionId);
        List<Trip> chargeTrips=getRobotChargeTrip(sectionId,driveId);
        boolean result=ObjectUtils.isEmpty(temp)&&CollectionUtils.isEmpty(trips)&&CollectionUtils.isEmpty(chargeTrips);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("小车{}分配任务{}\n" +
                            "生成调走小车的任务{}\n" +
                            "新生成的调度单{}\n"+
                    "充电调度单{}\n"
                    , driveId,JSONUtil.toJSon(trips),JSONUtil.toJSon(temp),JSONUtil.toJSon(newTrips),JSONUtil.toJSon(chargeTrips));
        }
        if(!result)
        {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("小车不可以调走,已经生成掉走单或者已经分配任务或者充电单没有结束"
                        , driveId);
            }
        }else{
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("小车{}可以调走"
                        , driveId);
            }
        }
        return result;

    }
    /**
     * 获取所有未分配小车的调度单
     */
    public List<Trip> getAllNotChargeTrips(String sectionId,String driveId) {
        List<String> tripTypes = new ArrayList<>();
        tripTypes.add(TripType.CHARGER_DRIVE.getName());
        //String tripState = TripState.NEW.getName();
        String tripState = TripState.FINISHED.getName();
        return tripRepository.getAllNotChargeTrips(tripTypes, tripState, sectionId,driveId);
    }

    public List<Trip> getAllNotChargeNewTrips(String sectionId) {
        List<String> tripTypes = new ArrayList<>();
        tripTypes.add(TripType.CHARGER_DRIVE.getName());
        String tripState = TripState.NEW.getName();
        //String tripState = TripState.FINISHED.getName();
        return tripRepository.getAllNotChargenNewTrips(tripTypes, tripState, sectionId);
    }
    public List<Trip> getAllEmptyRunTrips(String sectionId,String dri)
    {
        List<String> tripState = new ArrayList<>();
        tripState.add(TripState.NEW.getName());
        tripState.add(TripState.AVAILABLE.getName());
        tripState.add(TripState.PROCESS.getName());
        tripState.add(TripState.LEAVING.getName());
         return  tripRepository.getAllEmptyRunTrips(dri,tripState,sectionId);

    }


    /**
     * 获取所有不存在任务的可用小车
     */
    public List<WCSRobot> getAvailableDrives(String sectionId) {
        List<String> tripStates = new ArrayList<>();
        //tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        tripStates.add(TripState.PROCESS.getName());
        tripStates.add(TripState.LEAVING.getName());
        // 新增加
        return wcsRobotRepository.getAvailableDrives(tripStates, sectionId);
    }

    /* public List<WCSRobot> getAvailableDrives(String sectionId){
         List<String> tripStates = new ArrayList<>();
         tripStates.add(TripState.NEW.getName());
         tripStates.add(TripState.AVAILABLE.getName());
         return wcsRobotRepository.getAvailableDrives(tripStates,sectionId);
     }*/
    public List<Trip> getTrip(String podId, String sectionId) {
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        tripStates.add(TripState.PROCESS.getName());
        tripStates.add(TripState.LEAVING.getName());
        List<Trip> result = tripRepository.getTripsByPodId(tripStates, podId, sectionId);
        return result;

    }


    public List<Trip> getRobotChargeTrip(String sectionId,String driveId)
    {
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        tripStates.add(TripState.PROCESS.getName());
        tripStates.add(TripState.LEAVING.getName());
        List<Trip> result = tripRepository.getRobotChargeTrip(sectionId, driveId, tripStates);
        return result;
    }

    /**
     *
     */
    public boolean tripsAddrcodeIdInPlaceMark(String placeMark, String sectionId) {
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.PROCESS.getName());
        List<Trip> trip = tripRepository.getTripsAddrcodeId(tripStates, placeMark, sectionId);
        return CollectionUtils.isEmpty(trip) ? false : true;
    }

    /**
     * 判断是否可分配车
     */
    public List<Pod> releasedTrargetPod(String tarAddrid, String sectionId) {
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.LEAVING.getName());
        tripStates.add(TripState.PROCESS.getName());
        List<Pod> pod = podRepository.getByTripTarAddrId(tripStates, tarAddrid, sectionId);
        return pod;
    }

    public List<Trip> checkingDriveCanUse(Trip trip) {
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.AVAILABLE.getName());
        // 最新修改，当trip为process时 也认为小车不可用
        tripStates.add(TripState.PROCESS.getName());
        tripStates.add(TripState.LEAVING.getName());
        List<Trip> result = tripRepository.getWorkStationTrip(tripStates, trip.getSectionId(),trip.getTripType(),trip.getWorkStationId());


        return result;

    }

    /**
     * 获取所有不存在任务的可用小车
     */
    public boolean checkingDriveCanUse(String robotId, Trip trip) {
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.AVAILABLE.getName());
        // 最新修改，当trip为process时 也认为小车不可用
        tripStates.add(TripState.PROCESS.getName());
        tripStates.add(TripState.LEAVING.getName());
        Trip result = tripRepository.getAvailableTrip(tripStates, trip.getId());
        WCSRobot wcsRobot = wcsRobotRepository.getAvailableDrive(tripStates, robotId);
        List<Map> emptyDetail=getEmptyRunEndAddressPod(trip.getId());
        //获取pod下的小车
        String wcsRobot2 = getRobotByPod(trip.getPodId());
        LOGGER.info("是否已经存在 pod{}调度单{}", trip.getPodId(), result != null);
        LOGGER.info("小车{} 是否可用{}", robotId, wcsRobot != null);
        LOGGER.info("pod{} 下的小车{}", trip.getPodId(), wcsRobot2);
        if(!CollectionUtils.isEmpty(emptyDetail))
        {
            LOGGER.info("调度单 {} pod {}  地址 {} 有充电小车过去", trip.getId(), emptyDetail.get(0).get("POD_INDEX"), emptyDetail.get(0).get("PLACEMARK"));
        }


        if ((wcsRobot2 == null && result == null && wcsRobot != null&&CollectionUtils.isEmpty(emptyDetail)) || (wcsRobot2 != null && wcsRobot2.equals(robotId))) {
            return true;
        } else {
            return false;
        }

        // return result==null&&wcsRobot != null;

    }

    public String getRobotByPod(String podId) {
        String wcsRobot = null;
        String sql = "select WCS_ROBOT.* from WCS_ROBOT " +
                "inner join MD_POD on MD_POD.PLACEMARK= WCS_ROBOT.ADDRESSCODEID and WCS_ROBOT.ADDRESSCODEID<>0 and " +
                "WCS_ROBOT.SECTION_ID=MD_POD.SECTION_ID and MD_POD.id=:podId ";
        Map param = new HashMap();
        param.put("podId", podId);
        List wcsRobots = entityManagerUtil.executeNativeQuery(sql, param);
        if (!CollectionUtils.isEmpty(wcsRobots)) {
            wcsRobot = (String) ((Map) wcsRobots.get(0)).get("ROBOT_ID");
        }
        return wcsRobot;

    }


    List<Map> getEmptyRunEndAddressPod(String tripId)
    {


        String sql = "SELECT MD_POD.POD_INDEX,MD_POD.PLACEMARK,T1.ID FROM MD_POD " +
                " INNER JOIN RCS_TRIP T1 ON T1.POD_ID=MD_POD.ID" +
                " WHERE T1.ID=:tripId AND MD_POD.PLACEMARK  IN (SELECT T2.END_ADDRESS FROM RCS_TRIP T2 WHERE  T2.TRIP_STATE!='FINISH' AND T2.TRIP_TYPE='EMPTYRUN'" +
                " AND T2.END_ADDRESS IS NOT NULL )";

        Map param = new HashMap();
        param.put("tripId", tripId);
        List trips = entityManagerUtil.executeNativeQuery(sql, param);
        return trips;

    }

    /*public boolean checkingDriveCanUse(String robotId){
        List<String> tripStates = new ArrayList<>();
        tripStates.add(TripState.NEW.getName());
        tripStates.add(TripState.AVAILABLE.getName());
        WCSRobot wcsRobot = wcsRobotRepository.getAvailableDrive(tripStates,robotId);
        return wcsRobot != null;
    }*/
}
