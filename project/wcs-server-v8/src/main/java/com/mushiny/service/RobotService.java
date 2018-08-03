package com.mushiny.service;

import com.mushiny.beans.order.WcsPath;

import java.util.Map;

/**
 * Created by Tank.li on 2017/7/29.
 */
public interface RobotService {

    void drivePod(String podId, String addressId,String sectionId);

    void drive(long robotId, String addressId,String sectionId);

    String releasePod(String sectionId, String workStationId, String podName, boolean forceRelease);

    String callNewPod(String sectionId, String workStationId);

    void driveRobotCarryPod(long robotId, String podId, String addrCodeId, String sectionId);

    void autoAssignAdnDrivePod(String podId, String sectionId);

    String podStatus(String sectionId);

    String updateRobotStatus(String sectionId, String robotId, Integer robotStatus, Boolean canWork,Boolean cancel);

    String checkRobotStatus(String sectionId, String robotId);

    String updatePodStatus(String sectionId, String podId, String addrCodeId, String lockedBy);

    String scanPods(String sectionId);

    void callStowPod(String sectionId, String workStationId, String podName, String logicStationId);

    String wsPodList(String sectionId, String workStationId);

    String checkPodStatus(String sectionId, String podId);

    String checkPodFavAddrs(String sectionId, String podId);

    String checkWorkStationAddrs(String sectionId, String workStationId);

    String checkAddrState(String sectionId, String addrCodeId);

    String checkAllAddrs(String sectionId);

    String updateAddrState(String sectionId, String addrCodeId, String state, String lockedby);

    String resendOrder(String sectionId, String robotId, String findNewEndAddr);

    String sendOrder(String sectionId, String robotId, String orderId);

    String sendLastOrder(String robotId, String face, String index);

    String robot2Rp(String robotId);

    String checkPodsState(String sectionId, String pods);

    String checkRobots(String sectionId, String robots);

    String robotOffline(String robotId, String withPod);

    void updateStorageAddrs(String sectionId);

    String robotAdjust(String robotId, String act);

    String updateAddrNewCost(String sectionId, String addrCode, String newCost);

    String robot2Charge(String robotId);

    String updateRcsAddressList(String sectionId, String aList, String unList);

    String changeRcsPodsPosition(String sectionId, String podStrs, String addrStrs);

    String resendAGVPath2Rcs(String sectionId, String robotId);

    String checkRcsItemInfo(String sectionId, String itemKey, String itemValue);

    String checkMediaAGVConfigParameters(String sectionId, String robotId, String matchWord);

    String updateMediaAGVConfigParameters(String sectionId, String robotId);

    String clearMediaAGVError(String sectionId, String robotId, String seriousErrorID, String commonErrorID, String logicErrorID, String generalErrorID);

    String orderAction2AGV(String sectionId, String robotId, String actionType, String actionValue);

    String checkMediaAGVError(String sectionId, String robotId);

    String doPath(WcsPath path, Long podId);

    String updateCharger(String sectionId, String chargerId, String chargerType);

    String getWarehouseInitInfo(Map msg);

    String addBatchMdRobots(Map msg);

    String sendMsg2MQ(Map msg);

    String exchangeOrderRobot(Map msg);

    String exchangeOrderRobot4Restart(Map msg);

    String updatePodAddressHeavyCost();

    String changeRobotOrderEndAddr(Map paramsMap);

    String doPodRunOrder(String sectionId, String robotId);

    String driveRobot2Workstation(Map paramsMap);

    String releaseRobotWorkStation(Map paramsMap);

    String getNoBindWorkStationRobots(Map paramsMap);

    String dispalySysProps(String key, String wareHouse);

    void updateSectionStorage(String sectionId);

    String driveRobot2PlacePod(String robotId);

    String driveRobot2Charge(String robotId);

    String changeTarget(String robotId);

    String batchUpdateCost(String sectionId, String followCells,String allCells);

    void testRobotLock(String sectionId, String robotId, String podId);

    String showPodFace2Workstation(String sectionId, String workstationId, String podId, String face);
}
