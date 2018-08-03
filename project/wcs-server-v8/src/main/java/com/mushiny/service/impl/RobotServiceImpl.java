package com.mushiny.service.impl;

import com.mushiny.beans.*;
import com.mushiny.beans.enums.AddressType;
import com.mushiny.beans.order.ChargerDriveOrder;
import com.mushiny.beans.order.Order;
import com.mushiny.beans.order.StationPodOrder;
import com.mushiny.beans.order.WcsPath;
import com.mushiny.business.PodManager;
import com.mushiny.business.RobotManager;
import com.mushiny.business.WareHouseManager;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import com.mushiny.service.RobotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 小车相关对外提供的服务
 * 1、调度单生成
 * 2、小车路径服务
 * 3、手动调度小车
 * 4、....
 */
@Service
public class RobotServiceImpl implements RobotService {
    private final static Logger logger = LoggerFactory.getLogger(RobotServiceImpl.class);

    @Autowired
    private RobotManager robotManager;
    @Autowired
    private PodManager podManager;
    @Autowired
    private WareHouseManager wareHouseManager;

    @Override
    public void drivePod(String podId, String addressId,String sectionId) {
          this.robotManager.autoDrivePod(podId,addressId,sectionId);
    }

    @Override
    public void drive(long robotId, String addressId,String sectionId) {
            this.robotManager.drive(robotId,addressId);
    }

    @Override
    public String releasePod(String sectionId, String workStationId, String podName, boolean forceRelease) {
        return this.robotManager.releasePod(sectionId,workStationId,podName);
    }

    /**
     * 获取排队的新POD
     * 工作站都是调这个
     * @param sectionId
     * @param workStationId
     * @return
     */
    @Override
    public String callNewPod(String sectionId, String workStationId) {
        return this.robotManager.callNewPod(sectionId,workStationId);
    }

    @Override
    public void driveRobotCarryPod(long robotId, String podId, String addrCodeId, String sectionId) {
        this.robotManager.drivePodByRobot(robotId,podId,addrCodeId,null);
    }

    @Override
    public void autoAssignAdnDrivePod(String podId, String sectionId) {
        this.robotManager.autoAssignDrivePod(podId,sectionId);
    }

    @Override
    public String podStatus(String sectionId) {
        List<Pod> pods = this.podManager.getPodListBySection(sectionId);
        String json = JsonUtils.list2Json(pods);
        return json;
    }

    @Override
    public String updateRobotStatus(String sectionId, String robotId, Integer robotStatus, Boolean canWork,Boolean cancel) {
        Robot robot = this.robotManager.getRobotById(Long.parseLong(robotId));
        if(!Objects.equals(robot.getSectionId(),sectionId)){
            return "小车不在同一个Section:"+robotId+" VS "+sectionId;
        }
        robot.setStatus(robotStatus);
        robot.setAvaliable(canWork);
        if(cancel){
            logger.debug("抛弃当前调度单");
            robot.setCurOrder(null);
            Pod pod;
            if((pod = robot.getPod()) != null){
                pod.setLockedBy(0L);//释放锁定的POD
            }
            this.robotManager.ON_WCS_RCS_CLEAR_ALLPATH(robot);
        }
        logger.debug("robot状态修改:robot:"+robot);
        return robot.toString();
    }

    @Override
    public String checkRobotStatus(String sectionId, String robotId) {
        logger.debug("checkRobotStatus入参：sectionId="+sectionId+", robotId="+robotId);
        Robot robot = this.robotManager.getRobotById(Long.parseLong(robotId));
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        Map reMap = new LinkedHashMap();
        if(!Objects.equals(robot.getSectionId(),section.getSection_id())){
            reMap.put("isSuccess", "false");
            reMap.put("reInfo", "小车不在同一个Section:"+robotId+" VS "+sectionId);
            return JsonUtils.map2Json(reMap);
        }
        //return robot.toString();
        reMap.put("isSuccess", "true");
        Map robotMap = new LinkedHashMap();
        robotMap.put("robotId", robot.getId());
        robotMap.put("robotAddress", robot.getAddressId());
        robotMap.put("robotAvailable", robot.isAvaliable());
        robotMap.put("robotStatus", robot.getStatus());
        robotMap.put("robotLoginTime", CommonUtils.convert2String(new Date(robot.getLoginTime()), "YYYY-MM-dd HH:mm:ss"));
        robotMap.put("robotBatteryNumber", robot.getBatteryNumber());
        robotMap.put("robotLaveBattery", robot.getLaveBattery());
        robotMap.put("robotVoltage", robot.getVoltage());
        robotMap.put("robotFullChargeFlag", robot.getFullChargeFlag());
        robotMap.put("robotChargeNum", robot.getChargeNum());
        robotMap.put("robotFullChargeTime", CommonUtils.convert2String(new Date(robot.getFullChargeTime()), "YYYY-MM-dd HH:mm:ss"));
        robotMap.put("robotSectionId", robot.getSectionId());
        robotMap.put("robotIsChargingRetry", robot.getIsChargingRetry());
        robotMap.put("robotChargingRetryNum", robot.getChargingRetryNum());
        robotMap.put("robotStartLaveBattery", robot.getStartLaveBattery());
        robotMap.put("robotChargingTime", CommonUtils.convert2String(new Date(robot.getChargingTime()), "YYYY-MM-dd HH:mm:ss"));
        Pod pod = robot.getPod();
        if (pod != null){
            robotMap.put("podName", pod.getPodName());
            robotMap.put("podRcsId", pod.getRcsPodId());
            robotMap.put("podLockedBy", pod.getLockedBy());
            robotMap.put("podDirect", pod.getDirect());
            robotMap.put("podAddressId", pod.getAddress().getId());
            robotMap.put("podAddressStatus", pod.getAddress().getNodeState());
        }
        Order order = robot.getCurOrder();
        if (order != null){
            robotMap.put("orderId", order.getId());
            robotMap.put("orderType", order.getType());
            robotMap.put("orderPath", order.getWcsPath().getSeriesPath());
            robotMap.put("orderUpAddr", order.getWcsPath().getPodUpAddress());
            robotMap.put("orderDownAddr", order.getWcsPath().getPodDownAddress());
            robotMap.put("orderSrcAddr", order.getWcsPath().getSrcAddr());
            robotMap.put("orderEndAddr", order.getWcsPath().getEndAddr());
            robotMap.put("orderRotateTheta", order.getWcsPath().getRotateTheta());
            if (order instanceof StationPodOrder){
                StationPodOrder stationPodOrder = (StationPodOrder) order;
                robotMap.put("orderIndex", stationPodOrder.getIndex());
                robotMap.put("orderUseFace", stationPodOrder.getUseFace());
                robotMap.put("orderWorkStation", stationPodOrder.getWorkStation().toString());
            }else if (order instanceof ChargerDriveOrder){
                ChargerDriveOrder chargerDriveOrder = (ChargerDriveOrder) order;
                Charger charger = chargerDriveOrder.getCharger();
                robotMap.put("chargerId", charger.getChargerId());
                robotMap.put("chargerType", charger.getChargerType());
                robotMap.put("chargerRcsId", charger.getRcsChargerId());
                robotMap.put("chargerAddress", charger.getAddressCodeId());
            }
            if (order.getPod() != null){
                robotMap.put("orderPodName", order.getPod().getPodName());
                robotMap.put("orderPodAddr", order.getPod().getAddress().getId());
            }
            //robotMap.put("orderError", robot.getLastOrderError()==null?
            // "":order.getOrderError());
            robotMap.put("order2RcsTime", order.getSend2RcsTime());
        }
        robotMap.put("robotMessage", robot.getQueueMsg());
        robotMap.put("lastOrderId", robot.getLastOrderId());
        robotMap.put("lastOrderError", robot.getLastOrderError());
        robotMap.put("lastOrderPod", robot.getLastOrderPod());
        robotMap.put("lastOrderPath", robot.getLastPath());
        reMap.put("reInfo", robotMap);
        reMap.put("isSuccess", "true");
        String reJson = JsonUtils.map2Json(reMap);
        logger.debug(reJson);
        return reJson;
    }

    @Override
    public String updatePodStatus(String sectionId, String podId, String addrCodeId, String lockedBy) {
        return this.robotManager.updatePodStatus(sectionId,podId,addrCodeId,lockedBy);
    }

    @Override
    public String scanPods(String sectionId) {
        return this.robotManager.scanPods(sectionId);
    }

    @Override
    public void callStowPod(String sectionId, String workStationId, String podName, String logicStationId) {
        this.robotManager.callStowPod(sectionId,workStationId,podName,logicStationId);
    }

    @Override
    public String wsPodList(String sectionId, String workStationId) {
        return this.robotManager.wsPodList(sectionId, workStationId);
    }

    @Override
    public String checkPodStatus(String sectionId, String podId) {
        return this.robotManager.checkPodStatus(sectionId, podId);
    }

    @Override
    public String checkPodFavAddrs(String sectionId, String podId) {
        return this.robotManager.checkPodFavAddrs(sectionId, podId);
    }

    @Override
    public String checkWorkStationAddrs(String sectionId, String workStationId) {
        return this.robotManager.checkWorkStationAddrs(sectionId, workStationId);
    }

    @Override
    public String checkAddrState(String sectionId, String addrCodeId) {
        return this.robotManager.checkAddrState(sectionId,addrCodeId);
    }

    @Override
    public String checkAllAddrs(String sectionId) {
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
       List<String> list = new ArrayList<>();
        for (int i = 0; i < section.addressList.size(); i++) {
            Address address = section.addressList.get(i);
            if(address.getType() == AddressType.STORAGE){
                list.add(address.toString());
            }
        }
       return JsonUtils.list2Json(list);
    }

    @Override
    public String updateAddrState(String sectionId, String addrCodeId, String state, String lockedby) {
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }

        Address address = this.wareHouseManager.getAddressByAddressCodeId(addrCodeId,section);
        address.setNodeState(state);
        if (!CommonUtils.isEmpty(lockedby)){
            address.setLockedBy(Long.parseLong(lockedby));
        }
        return address.toString();
    }

    @Override
    public String resendOrder(String sectionId, String robotId, String findNewEndAddr) {
       return this.robotManager.resendOrder(sectionId,robotId,findNewEndAddr);
    }

    @Override
    public String sendOrder(String sectionId, String robotId, String orderId) {
        return this.robotManager.sendOrder(sectionId,robotId,orderId);
    }

    @Override
    public String sendLastOrder(String robotId, String face, String index) {
        return this.robotManager.sendLastOrder(robotId,face,index);
    }

    @Override
    public String robot2Rp(String robotId) {
        return this.robotManager.robot2Rp(robotId);
    }

    @Override
    public String checkPodsState(String sectionId, String pods) {
        return this.robotManager.checkPodsState(sectionId, pods);
    }

    @Override
    public String checkRobots(String sectionId, String robots) {
        return this.robotManager.checkRobots(sectionId, robots);
    }

    @Override
    public String robotOffline(String robotId, String withPod) {
        return this.robotManager.robotOffline(robotId, withPod);
    }

    @Override
    public void updateStorageAddrs(String sectionId) {
        this.robotManager.updateStorageAddrs(sectionId);
    }

    @Override
    public String robotAdjust(String robotId, String act) {
        return this.robotManager.robotAdjust(robotId,act);
    }

    @Override
    public String updateAddrNewCost(String sectionId, String addrCode, String newCost) {
        List<String> addrs = new ArrayList<>();
        addrs.add(addrCode);
        Long cost = null;
        if(!CommonUtils.isEmpty(newCost)){
            cost = Long.parseLong(newCost);
        }
        this.wareHouseManager.batchUpdateCellsCost(sectionId,addrs,cost);
        return "sectionId:"+sectionId+" addrCode:"+addrCode+" newCost:"+newCost;
    }

    @Override
    public String robot2Charge(String robotId) {
        return this.robotManager.robot2Charge(robotId);
    }

    @Override
    public String updateRcsAddressList(String sectionId, String aList, String unList) {
        return this.robotManager.updateRcsAddressList(sectionId, aList, unList);
    }

    @Override
    public String changeRcsPodsPosition(String sectionId, String podStrs, String addrStrs) {
        return this.robotManager.changeRcsPodsPosition(sectionId, podStrs, addrStrs);
    }

    @Override
    public String resendAGVPath2Rcs(String sectionId, String robotId) {
        return this.robotManager.resendAGVPath2Rcs(sectionId, robotId);
    }

    @Override
    public String checkRcsItemInfo(String sectionId, String itemKey, String itemValue) {
        return this.robotManager.checkRcsItemInfo(sectionId, itemKey, itemValue);
    }

    @Override
    public String checkMediaAGVConfigParameters(String sectionId, String robotId, String matchWord) {
        return this.robotManager.checkMediaAGVConfigParameters(sectionId, robotId, matchWord);
    }

    @Override
    public String updateMediaAGVConfigParameters(String sectionId, String robotId) {
        return this.robotManager.updateMediaAGVConfigParameters(sectionId, robotId);
    }

    @Override
    public String clearMediaAGVError(String sectionId, String robotId, String seriousErrorID, String commonErrorID, String logicErrorID, String generalErrorID) {
        return this.robotManager.clearMediaAGVError(sectionId, robotId, seriousErrorID, commonErrorID, logicErrorID, generalErrorID);
    }

    @Override
    public String orderAction2AGV(String sectionId, String robotId, String actionType, String actionValue) {
        return this.robotManager.orderAction2AGV(sectionId, robotId, actionType, actionValue);
    }

    @Override
    public String checkMediaAGVError(String sectionId, String robotId) {
        return this.robotManager.checkMediaAGVError(sectionId, robotId);
    }

    @Override
    public String doPath(WcsPath path, Long podId) {
        return this.robotManager.doPath(path,podId);
    }

    @Override
    public String updateCharger(String sectionId, String chargerId, String chargerType) {
        return this.robotManager.updateCharger(sectionId, chargerId, chargerType);
    }

    @Override
    public String getWarehouseInitInfo(Map msg) {
        return this.robotManager.ON_ANY_WCS_WAREHOUSE_INIT_REQUEST(msg);
    }

    @Override
    public String addBatchMdRobots(Map msg) {
        return this.robotManager.addBatchMdRobots(msg);
    }

    @Override
    public String sendMsg2MQ(Map msg) {
        return this.robotManager.sendMsg2MQ(msg);
    }

    @Override
    public String exchangeOrderRobot(Map msg) {
        return this.robotManager.exchangeOrderRobot(msg);
    }

    @Override
    public String exchangeOrderRobot4Restart(Map msg) {
        return this.robotManager.exchangeOrderRobot4Restart(msg);
    }

    @Override
    public String updatePodAddressHeavyCost() {
        return this.robotManager.updatePodAddressHeavyCost();
    }

    @Override
    public String changeRobotOrderEndAddr(Map paramsMap) {
        return this.robotManager.changeRobotOrderEndAddr(paramsMap);
    }

    @Override
    public String doPodRunOrder(String sectionId, String robotId) {
        //为当前的robot和Pod生成podRun
        return this.robotManager.genPodRunOrder(sectionId,robotId);
    }

    @Override
    public String driveRobot2Workstation(Map paramsMap) {
        return this.robotManager.driveRobot2Workstation(paramsMap);
    }

    @Override
    public String releaseRobotWorkStation(Map paramsMap) {
        return this.robotManager.releaseRobotWorkStation(paramsMap);
    }

    @Override
    public String getNoBindWorkStationRobots(Map paramsMap) {
        return this.robotManager.getNoBindWorkStationRobots(paramsMap);
    }

    @Override
    public String dispalySysProps(String key, String wareHouse) {
        return this.robotManager.dispalySysProps(key,wareHouse);
    }

    @Override
    public void updateSectionStorage(String sectionId) {
        Section section = this.wareHouseManager.getSectionById(sectionId);
        this.robotManager.updateSectionStorage(section);
    }

    @Override
    public String driveRobot2PlacePod(String robotId) {
        return this.robotManager.driveRobot2PlacePod(robotId);
    }

    @Override
    public String driveRobot2Charge(String robotId) {
        return this.robotManager.driveRobot2Charge(robotId);
    }

    @Override
    public String changeTarget(String robotId) {
        return this.robotManager.changeTarget(robotId);
    }

    @Override
    public String batchUpdateCost(String sectionId, String followCells, String allCells) {
        return this.robotManager.batchUpdateCostByInOut(sectionId,followCells,allCells);
    }

    @Override
    public void testRobotLock(String sectionId, String robotId, String podId) {
        this.robotManager.testRobotLock(sectionId,robotId,podId);
    }

    @Override
    public String showPodFace2Workstation(String sectionId, String workstationId, String podId, String face) {
        return this.robotManager.showPodFace2Workstation(sectionId,workstationId,podId,face);
    }

}
