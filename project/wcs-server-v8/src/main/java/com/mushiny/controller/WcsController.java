package com.mushiny.controller;

/**
 * Created by Tank.li on 2017/7/26.
 */

import com.mushiny.beans.order.WcsPath;
import com.mushiny.business.WareHouseManager;
import com.mushiny.comm.CommonUtils;
import com.mushiny.jdbc.repositories.JdbcRepository;
import com.mushiny.service.RobotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 123 on 2017/5/2.
 */
@RestController
@RequestMapping("/wcs")
public class WcsController {

    private final static Logger logger = LoggerFactory.getLogger(WcsController.class);
    //@Autowired
    private WareHouseManager wareHouseManager;
    //@Autowired
    private JdbcRepository jdbcRepository;

    @Autowired
    public WcsController(WareHouseManager wareHouseManager, JdbcRepository jdbcRepository) {
        this.wareHouseManager = wareHouseManager;
        this.jdbcRepository = jdbcRepository;

    }

    @GetMapping(path = "/path-planning/empty-drive/path", params = {"warehouseId", "sectionId", "sourceVertex", "targetVertex"})
    public ResponseEntity<List<String>> getInteger(@RequestParam String warehouseId,
                                                   @RequestParam String sectionId,
                                                   @RequestParam String sourceVertex,
                                                   @RequestParam String targetVertex) {
        List<String> integerList = new ArrayList<>();
        integerList.add(warehouseId);
        integerList.add(sectionId);
        integerList.add(sourceVertex);
        integerList.add(targetVertex);

        return ResponseEntity.ok(integerList);
    }

    @Autowired
    private RobotService robotService;


    /*http://localhost:8080/wcs/drivePod?warehouseId=DEFAULT&sectionId=e297aa3f-a941-4607-be1f-263a98a2d07a
    &addrCodeId=31&podId=36fae3e5-9a76-4c6b-8884-7c7d7fc099a9*/
    @GetMapping(path = "/drivePod", params = {"podId", "addrCodeId", "sectionId"})
    public String drivePod(@RequestParam String podId,
                           @RequestParam String addrCodeId,
                           @RequestParam String sectionId) {

        robotService.drivePod(podId, addrCodeId, sectionId);
        return "ok";
    }

    /**
     * 自动分配车和POD目的地
     * @param podId
     * @param sectionId
     * @return
     */
    @GetMapping(path = "/autoAssignAdnDrivePod", params = {"podId", "sectionId"})
    public String autoAssignAdnDrivePod(@RequestParam String podId,
                           @RequestParam String sectionId) {

        robotService.autoAssignAdnDrivePod(podId, sectionId);
        return "ok";
    }

    @GetMapping(path = "/driveRobot", params = {"robotId", "addrCodeId", "sectionId"})
    public String driveRobot(@RequestParam String robotId,
                           @RequestParam String addrCodeId,
                           @RequestParam String sectionId) {

        robotService.drive(Long.parseLong(robotId), addrCodeId, sectionId);
        return "ok";
    }


    /**
     * @param robotId
     * @param podId
     * @param addrCodeId
     * @param sectionId
     * @return
     */
    @GetMapping(path = "/driveRobotCarryPod", params = {"robotId", "podId","addrCodeId", "sectionId"})
    public String driveRobotCarryPod(@RequestParam String robotId,
                             @RequestParam String podId,
                             @RequestParam String addrCodeId,
                             @RequestParam String sectionId) {

        robotService.driveRobotCarryPod(Long.parseLong(robotId),podId, addrCodeId, sectionId);
        return "ok";
    }

    //@GetMapping(path = "/podRelease", params = {"sectionId","workStationId", "podName","force"})

    @GetMapping(path = "/podRelease")
    public String podRelease(HttpServletRequest request, HttpServletResponse response) {
        long bt = System.currentTimeMillis();
        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.debug("podRelease客户机释放POD request.getRemoteHost():"+request.getRemoteHost()+" request.getRemoteAddr():"+request.getRemoteAddr());
        String sectionId = request.getParameter("sectionId");
        String workStationId = request.getParameter("workStationId");
        //添加逻辑工作站ID
        String logicStationId = request.getParameter("logicStationId");
        String podName = request.getParameter("podName");
        String force =request.getParameter("force");
        boolean forceRelease = Boolean.parseBoolean(force);
        logger.debug("释放POD：sectionId:"+sectionId+" workStationId:"+workStationId +" podName"+podName+" force"+force);
        String src = this.robotService.releasePod(sectionId,workStationId,podName,forceRelease);
        //logger.debug("发起新POD呼叫：sectionId:"+sectionId+" workStationId:"+workStationId +" podName"+podName+" logicStationId"+logicStationId);
        //this.robotService.callStowPod(sectionId,workStationId,podName,logicStationId);//由原先POD的路径 发送新POD请求
        logger.debug("WCS释放POD耗时："+(System.currentTimeMillis()-bt)+" 毫秒");
        return src;
    }

    @GetMapping(path = "/callNewPod")//, params = {"sectionId","workStationId"} ,@RequestParam String sectionId,@RequestParam String workStationId
    public String callNewPod(HttpServletRequest request, HttpServletResponse response) {
        long bt = System.currentTimeMillis();
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String workStationId = request.getParameter("workStationId");
        logger.debug("刷新POD：sectionId:"+sectionId+" workStationId:"+workStationId);
        String reStr = this.robotService.callNewPod(sectionId,workStationId);
        logger.debug("WCS刷新POD耗时："+(System.currentTimeMillis()-bt)+" 毫秒");
        return reStr;
    }

    @GetMapping(path = "/podStatus")//, params = {"sectionId","workStationId"} ,@RequestParam String sectionId,@RequestParam String workStationId
    public String podStatus(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        return this.robotService.podStatus(sectionId);
    }

    /**
     * 更新小车状态
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/updateRobotStatus")
    public String updateRobotStatus(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        String available = request.getParameter("available");
        Boolean canWork = available!=null && Boolean.parseBoolean(available);
        String status = request.getParameter("status");
        String cancelOrder = request.getParameter("cancelOrder");
        Boolean cancel = cancelOrder != null && Boolean.parseBoolean(cancelOrder);
        Integer robotStatus = Integer.parseInt(status);
        logger.debug("刷新Robot：sectionId:"+sectionId+" robotId:"+robotId + " available:"+status +" stauts:"+status);
        return this.robotService.updateRobotStatus(sectionId,robotId,robotStatus,canWork,cancel);
    }

    /**
     * 查看小车状态
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/checkRobotStatus")
    public String checkRobotStatus(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        return this.robotService.checkRobotStatus(sectionId,robotId);
    }

    /**
     * 更新POD状态==地址码
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/updatePodStatus")
    public String updatePodStatus(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String podId = request.getParameter("podId");
        String addrCodeId = request.getParameter("addrCodeId");
        String lockedBy = request.getParameter("lockedBy");
        logger.debug("刷新Robot：sectionId:"+sectionId+" podId:"+podId + " addrCodeId:"+addrCodeId);
        return this.robotService.updatePodStatus(sectionId,podId,addrCodeId,lockedBy);
    }

    /**
     * 下发POD巡检路径 全部存储区
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/scanPods")
    public String scanPods(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        return this.robotService.scanPods(sectionId);
    }

    /**
     * 获取工作站pod列表
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/wsPodList")
    public String wsPodList(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String workStationId = request.getParameter("workStationId");
        return this.robotService.wsPodList(sectionId, workStationId);
    }

    /**
     * 检查pod信息
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/checkPodStatus")
    public String checkPodStatus(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String podId = request.getParameter("podId");
        return this.robotService.checkPodStatus(sectionId, podId);
    }

    /**
     * 检查地址信息
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/checkAddrState")
    public String checkAddrState(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String addrCodeId = request.getParameter("addrCodeId");
        return this.robotService.checkAddrState(sectionId, addrCodeId);
    }

    @GetMapping(path = "/updateAddrState")
    public String updateAddrState(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String addrCodeId = request.getParameter("addrCodeId");
        String state = request.getParameter("status");
        String lockedby = request.getParameter("lockedby");
        return this.robotService.updateAddrState(sectionId, addrCodeId,state, lockedby);
    }



    /**
     * 检查所有Pod信息
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/checkPods")
    public String checkPodsState(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String pods = request.getParameter("pods");
        //String addrCodeId = request.getParameter("addrCodeId");
        return this.robotService.checkPodsState(sectionId,pods);
    }

    /**
     * 检查所有Pod信息
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/checkRobots")
    public String checkRobots(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robots = request.getParameter("robots");
        //String addrCodeId = request.getParameter("addrCodeId");
        return this.robotService.checkRobots(sectionId,robots);
    }

    /**
     * 重新发起小车当前调度单 下发路径
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/resendOrder")
    public String resendOrder(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        String findNewEndAddr = request.getParameter("findNewEndAddr");
        return this.robotService.resendOrder(sectionId,robotId,findNewEndAddr);
    }

    @GetMapping(path = "/doPodRunOrder")
    public String doPodRunOrder(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");

        String msg = this.robotService.doPodRunOrder(sectionId,robotId);
        if(!"OK".equalsIgnoreCase(msg)){
            return msg;
        }


        return this.robotService.resendOrder(sectionId,robotId,null);
    }

    /**
     * 获取某调度单并下发
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/sendOrder")
    public String sendOrder(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        String orderId = request.getParameter("orderId");
        return this.robotService.sendOrder(sectionId,robotId,orderId);
    }


    /*======================================*/
    /**
     * 检查pod热度地址  暂时取前5个
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/checkPodFavAddrs")
    public String checkPodFavAddrs(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String podId = request.getParameter("podId");
        return this.robotService.checkPodFavAddrs(sectionId, podId);
    }

    /**
     * 检查工作站特殊地址信息
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/checkWorkStationAddrs")
    public String checkWorkStationAddrs(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String workStationId = request.getParameter("workStationId");
        return this.robotService.checkWorkStationAddrs(sectionId, workStationId);
    }

    /**
     * 给小车发送最新的指令
     * 最新process的及明细
     * 如果有face 优先face
     * 如果有index 排其次
     * 如果没有face和index 以index排序最大的那个process明细
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/sendLastOrder")
    public String sendLastOrder(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String robotId = request.getParameter("robotId");
        String face = request.getParameter("face");
        String index = request.getParameter("index");
        return this.robotService.sendLastOrder(robotId,face,index);
    }

    /**
     * 让小车去休息区，走重车路径 ，绕开货架地下的小车
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/robot2Rp")
    public String robot2Rp(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String robotId = request.getParameter("robotId");
        return this.robotService.robot2Rp(robotId);
    }

    /**
     * 让小车离线
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/robotOffline")
    public String robotOffline(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String robotId = request.getParameter("robotId");
        String withPod = request.getParameter("withPod");
        logger.debug("请求接口robotOffline，参数robotId="+robotId+", withPod="+withPod);
        return this.robotService.robotOffline(robotId, withPod);
    }

    /**
     * 更新所有存储格的状态 根据上面有没有POD
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/updateStorageAddrs")
    public String updateStorageAddrs(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        this.robotService.updateStorageAddrs(sectionId);
        return this.robotService.checkAllAddrs(sectionId);
    }

    /**
     * 检查所有存储位地址信息
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/checkAllAddrs")
    public String checkAllAddrs(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        //String addrCodeId = request.getParameter("addrCodeId");
        return this.robotService.checkAllAddrs(sectionId);
    }

    @GetMapping(path = "/robotAct")
    public String robotAdjust(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String robotId = request.getParameter("robotId");
        String act = request.getParameter("act");
        return this.robotService.robotAdjust(robotId,act);
    }

    @GetMapping(path = "/updateAddrNewCost")
    public String updateAddrNewCost(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String addrCode = request.getParameter("addrCode");
        String sectionId = request.getParameter("sectionId");
        String newCost = request.getParameter("costValue");
        return this.robotService.updateAddrNewCost(sectionId,addrCode,newCost);
    }

    @GetMapping(path = "/robot2Charge")
    public String robot2Charge(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String robotId = request.getParameter("robotId");
        return this.robotService.robot2Charge(robotId);
    }

    /**
     * 批量更新RCS CELL的 [锁定][解锁] 状态
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/updateRcsAddressList")
    public String updateRcsAddressList(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String aList = request.getParameter("availableAddressList");
        String unList = request.getParameter("unAvailableAddressList");
        return this.robotService.updateRcsAddressList(sectionId, aList, unList);
    }

    /**
     * 批量更新RCS POD的地址
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/changeRcsPodsPosition")
    public String changeRcsPodsPosition(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String podStrs = request.getParameter("podIdList");
        String addrStrs = request.getParameter("addrCodeIdList");
        return this.robotService.changeRcsPodsPosition(sectionId, podStrs, addrStrs);
    }

    /**
     * 直接向RCS重新下发路径
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/resendAGVPath2Rcs")
    public String resendAGVPath2Rcs(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        return this.robotService.resendAGVPath2Rcs(sectionId, robotId);
    }

    /**
     * 查看RCS中的信息，如AGV，CELL
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/checkRcsItemInfo")
    public String checkRcsItemInfo(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String itemKey = request.getParameter("itemKey");
        String itemValue = request.getParameter("itemValue");
        return this.robotService.checkRcsItemInfo(sectionId, itemKey, itemValue);
    }

    /**
     * 查看MediaAGV配置参数
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/checkMediaAGVConfigParameters")
    public String checkMediaAGVConfigParameters(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        String matchWord = request.getParameter("matchWord");
        return this.robotService.checkMediaAGVConfigParameters(sectionId, robotId, matchWord);
    }

    /**
     * 修改MediaAGV配置参数
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/updateMediaAGVConfigParameters")
    public String updateMediaAGVConfigParameters(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        return this.robotService.updateMediaAGVConfigParameters(sectionId, robotId);
    }

    /**
     * 查看MediaAGV故障
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/checkMediaAGVError")
    public String checkMediaAGVError(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        return this.robotService.checkMediaAGVError(sectionId, robotId);
    }

    /**
     * 清除MediaAGV故障
     * @param request   seriousErrorID  commonErrorID   logicErrorID    generalErrorID
     * @param response
     * @return
     */
    @GetMapping(path = "/clearMediaAGVError")
    public String clearMediaAGVError(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        //严重错误
        String seriousErrorID = request.getParameter("seriousErrorID");
        //普通错误
        String commonErrorID = request.getParameter("commonErrorID");
        //逻辑错误
        String logicErrorID = request.getParameter("logicErrorID");
        //一般错误
        String generalErrorID = request.getParameter("generalErrorID");
        return this.robotService.clearMediaAGVError(sectionId, robotId, seriousErrorID, commonErrorID, logicErrorID, generalErrorID);
    }

    /**
     * 向AGV下发动作命令
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/orderAction2AGV")
    public String orderAction2AGV(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        //actionType:命令类型 actionValue:命令参数
        /*
            0 - "启动",
            1 - "停到最近的二维码，减速到0",
            2 - "急停",
            3 - "所有电机供电断电",
            4 - "旋转(托盘固定)",
            5 - "旋转(托盘单独转动)",
            6 - "旋转(托盘固定，且附带顶升)",
            7 - "旋转(托盘固定，且附带降落)",
            8 - "顶升",
            9 - "下降",
            10 - "开始休眠",
            11 - "结束休眠",
            12 - "清空已经下发路径节点",
            13 - "开始充电（by LSJ）",
            14 - "结束充电（by LSJ）"
         */
        String actionType = request.getParameter("actionType");
        String actionValue = request.getParameter("actionValue");
        return this.robotService.orderAction2AGV(sectionId, robotId, actionType, actionValue);
    }

    /**
     * 小车执行路径 起点到终点 举升点 下降点 POD编号（人工操作）
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/doPath")
    public String doPath(HttpServletRequest request, HttpServletResponse response) {
        String destAddr = request.getParameter("destAddr");
        String podUpAddr = request.getParameter("podUpAddr");
        String podDownAddr = request.getParameter("podDownAddr");
        String podCodeId = request.getParameter("podCodeId");
        String robotId = request.getParameter("robotId");

        Long podUp = CommonUtils.isEmpty(podUpAddr)? 0L:Long.parseLong(podUpAddr);
        Long podDown = CommonUtils.isEmpty(podDownAddr)? 0:Long.parseLong(podDownAddr);

        Long dest = CommonUtils.isEmpty(destAddr)? 0:Long.parseLong(destAddr);
        Long podId = CommonUtils.isEmpty(podCodeId)? 0:Long.parseLong(podCodeId);

        WcsPath path = new WcsPath();
        path.setRobotID(Long.parseLong(robotId));
        path.setPodUpAddress(podUp);
        path.setPodDownAddress(podDown);
        path.setEndAddr(dest);

        return this.robotService.doPath(path,podId);
    }

    /**
     * 更新充电桩信息
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/updateCharger")
    public String updateCharger(HttpServletRequest request, HttpServletResponse response) {
        String sectionId = request.getParameter("sectionId");
        String chargerId = request.getParameter("chargerId");
        String chargerType = request.getParameter("chargerType");
        return this.robotService.updateCharger(sectionId, chargerId, chargerType);
    }

    /**
     * 终端仓库初始化
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/getWarehouseInitInfo")
    public String getWarehouseInitInfo(HttpServletRequest request, HttpServletResponse response) {
        String warehouseId = request.getParameter("warehouseId");
        Long requestTime = System.currentTimeMillis();
        Map msg = new HashMap();
        msg.put("requestTime", requestTime);
        msg.put("warehouseId", warehouseId);
        logger.debug("请求接口getWarehouseInitInfo：requestTime="+requestTime+", warehouseId="+warehouseId);
        return this.robotService.getWarehouseInitInfo(msg);
    }

    /**
     * 批量新增基础小车
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/addBatchMdRobots")
    public String addBatchMdRobots(HttpServletRequest request, HttpServletResponse response) {
        String warehouseId = request.getParameter("warehouseId");
        if (CommonUtils.isEmpty(warehouseId)){
            warehouseId = "DEFAULT";
        }
        String startIndex = request.getParameter("startIndex");
        String total = request.getParameter("total");
        Map msg = new HashMap();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        msg.put("createdDate", timestamp);
        msg.put("warehouseId", warehouseId);
        msg.put("startIndex", startIndex);
        msg.put("total", total);
        logger.debug("请求接口addBatchMdRobots：createdDate="+timestamp+", warehouseId="+warehouseId+", startIndex="+startIndex+", total="+total);
        return this.robotService.addBatchMdRobots(msg);
    }

    /**
     * 手动发送消息到mq
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/sendMsg2MQ")
    public String sendMsg2MQ(HttpServletRequest request, HttpServletResponse response) {
        String exchangeName = request.getParameter("exchangeName");
        String routingKey = request.getParameter("routingKey");
        Map msg = new HashMap();
        msg.put("exchangeName", exchangeName);
        msg.put("routingKey", routingKey);
        logger.debug("请求接口sendMsg2MQ：exchangeName="+exchangeName+", routingKey="+routingKey);
        return this.robotService.sendMsg2MQ(msg);
    }

    /**
     * 服务运行中替换小车    未测试
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/exchangeOrderRobot")
    public String exchangeOrderRobot(HttpServletRequest request, HttpServletResponse response) {
        String oldRobotId = request.getParameter("oldRobotId");
        String newRobotId = request.getParameter("newRobotId");
        String sectionId = request.getParameter("sectionId");
        String podId = request.getParameter("podId");
        String podAddrCodeId = request.getParameter("podAddrCodeId");
        Map msg = new HashMap();
        msg.put("oldRobotId", oldRobotId);
        msg.put("newRobotId", newRobotId);
        msg.put("sectionId", sectionId);
        msg.put("podId", podId);
        msg.put("podAddrCodeId", podAddrCodeId);
        logger.debug("请求接口exchangeOrderRobot：oldRobotId="+oldRobotId+", newRobotId="+newRobotId+", sectionId="+sectionId+", podId="+podId+", podAddrCodeId="+podAddrCodeId);
        return this.robotService.exchangeOrderRobot(msg);
    }

    /**
     * 服务重启后替换故障小车    未测试
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/exchangeOrderRobot4Restart")
    public String exchangeOrderRobot4Restart(HttpServletRequest request, HttpServletResponse response) {
        String oldRobotId = request.getParameter("oldRobotId");
        String newRobotId = request.getParameter("newRobotId");
        String sectionId = request.getParameter("sectionId");
        Map msg = new HashMap();
        msg.put("oldRobotId", oldRobotId);
        msg.put("newRobotId", newRobotId);
        msg.put("sectionId", sectionId);
        logger.debug("请求接口exchangeOrderRobot4Restart：oldRobotId="+oldRobotId+", newRobotId="+newRobotId+", sectionId="+sectionId);
        return this.robotService.exchangeOrderRobot4Restart(msg);
    }

    /**
     * 更新储位的重车cost    建议小车停止时调用
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/updatePodAddressHeavyCost")
    public String updatePodAddressHeavyCost(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("请求接口updatePodAddressHeavyCost");
        return this.robotService.updatePodAddressHeavyCost();
    }

    /**
     * 更新小车调度单终点
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/changeRobotOrderEndAddr")
    public String changeRobotOrderEndAddr(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("请求接口changeRobotOrderEndAddr");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        String destAddressCodeId = request.getParameter("destAddressCodeId");
        Map paramsMap = new HashMap();
        paramsMap.put("sectionId", sectionId);
        paramsMap.put("robotId", robotId);
        paramsMap.put("destAddressCodeId", destAddressCodeId);
        return this.robotService.changeRobotOrderEndAddr(paramsMap);
    }

    /**
     * 指定某小车去某工作站
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/driveRobot2Workstation")
    public String driveRobot2Workstation(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("请求接口driveRobot2Workstation");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        String workStationId = request.getParameter("workStationId");
        Map paramsMap = new HashMap();
        paramsMap.put("sectionId", sectionId);
        paramsMap.put("robotId", robotId);
        paramsMap.put("workStationId", workStationId);
        return this.robotService.driveRobot2Workstation(paramsMap);
    }

    /**
     * 解绑小车和工作站
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/releaseRobotWorkStation")
    public String releaseRobotWorkStation(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("请求接口releaseRobotWorkStation");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        String workStationId = request.getParameter("workStationId");
        Map paramsMap = new HashMap();
        paramsMap.put("sectionId", sectionId);
        paramsMap.put("robotId", robotId);
        paramsMap.put("workStationId", workStationId);
        return this.robotService.releaseRobotWorkStation(paramsMap);
    }

    /**
     * 获取工作站可以绑定的小车
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/getNoBindWorkStationRobots")
    public String getNoBindWorkStationRobots(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("请求接口getNoBindWorkStationRobots");
        String sectionId = request.getParameter("sectionId");
        Map paramsMap = new HashMap();
        paramsMap.put("sectionId", sectionId);
        return this.robotService.getNoBindWorkStationRobots(paramsMap);
    }

    /**
     * 获取系统参数
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/dispalySysProps")
    public String dispalySysProps(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("请求接口getNoBindWorkStationRobots");
        String wareHouse = request.getParameter("wareHouse");
        String key = request.getParameter("key");
        return this.robotService.dispalySysProps(key,wareHouse);
    }

    /**
     * 重新加载工作站 目前是全部的
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/reloadWorkStation")
    public String reloadWorkStation(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("重新加载工作站reloadWorkStation，RCS需要重启!");
        String sectionId = request.getParameter("sectionId");
        String workstationId = request.getParameter("workstationId");
        this.wareHouseManager.reloadWorkStation(sectionId,workstationId);
        return "sectionId="+sectionId+"重新加载工作站reloadWorkStation"
                + workstationId +"，RCS需要重启!";
    }

    /**
     * 重新加载充电桩 目前是全部的
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/reloadCharger")
    public String reloadCharger(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("重新加载工作站reloadWorkStation，RCS需要重启!");
        String sectionId = request.getParameter("sectionId");
        String chargerId = request.getParameter("chargerId");
        this.wareHouseManager.reloadCharger(sectionId,chargerId);
        return "sectionId="+sectionId+"重新加载充电桩reloadWorkStation"
                + chargerId +"，RCS需要重启!";
    }

    /**
     * 重新加载Section 含地图信息 旋转区 充电桩
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/reloadSection")
    public String reloadSection(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("重新地图reloadSection，RCS需要重启!");
        String sectionId = request.getParameter("sectionId");
        this.wareHouseManager.reloadSection(sectionId);
        return "sectionId="+sectionId+"重新加载，RCS需要重启!";
    }

    /**
     * 改进的方法 更新存储格的目标状态
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/updateSectionStorage")
    public String updateSectionStorage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("重新更新地图存储格的目标状态完成!");
        String sectionId = request.getParameter("sectionId");
        this.robotService.updateSectionStorage(sectionId);
        return "sectionId="+sectionId+"更新存储格的目标状态完成!";
    }

    /**
     * 驱动小车去其他地方放货架 不考虑是否可用 不考虑其他任务
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/driveRobot2PlacePod")
    public String driveRobot2PlacePod(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("重新更新地图存储格的目标状态完成!");
        String robotId = request.getParameter("robotId");
        return this.robotService.driveRobot2PlacePod(robotId);
    }

    /**
     * 驱动小车去充电 不考虑是否可用 不考虑其他任务
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/driveRobot2Charge")
    public String driveRobot2Charge(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("重新更新地图存储格的目标状态完成!");
        String robotId = request.getParameter("robotId");
        //String chargerId = request.getParameter("chargerId");
        return this.robotService.driveRobot2Charge(robotId);
    }

    /**
     * 更新小车目的地，改进的方法 TODO
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/changeTarget")
    public String changeTarget(HttpServletRequest request, HttpServletResponse response) {
        String robotId = request.getParameter("robotId");
        logger.debug("更新小车目的地 robotId:"+robotId);
        return this.robotService.changeTarget(robotId);
    }

    /**
     * 批量修改cost和carryCost值
     * 当前section激活的地图
     * @param request
     * @param response
     * @return
     */
    @PostMapping(path = "/batchUpdateCost")
    public String batchUpdateCost(HttpServletRequest request, HttpServletResponse response) {
        String sectionId = request.getParameter("sectionId");
        //单向行驶点 都是单方向1 反方向-1 其他方向都是-1
        //出口单方向-1 其他方向不可走
        //旋转区入口和不旋转入口 入口-1 出口1 其他1
        //与区域外的节点 如果不是出口入口 都是不可走 -1
        //1234#5678#3456,1234#5678#3456 逗号分开
        String followCells = request.getParameter("followCells");
        //1234#2341 ,说明1234其他方向不可出
        String exit = request.getParameter("exit");
        //第一个是非旋转的 第二个是旋转的
        //1234#2342,1234#2343
        String enters = request.getParameter("enters");
        //allCells 除进出口外 都是封闭的
        String allCells = request.getParameter("allCells");

        return this.robotService.batchUpdateCost(sectionId,followCells,allCells);
    }

    @GetMapping(path = "/testRobotLock")
    public String testRobotLock(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("测试目标锁定方法!");
        String sectionId = request.getParameter("sectionId");
        String robotId = request.getParameter("robotId");
        String podId = request.getParameter("podId");
        this.robotService.testRobotLock(sectionId, robotId, podId);
        return this.robotService.checkRobots(sectionId,"");
    }

    @GetMapping(path = "/showPodFace2Workstation")
    public String showPodFace2Workstation(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("showPodFace2Workstation!");
        String sectionId = request.getParameter("sectionId");
        String workstationId = request.getParameter("workstationId");
        String podId = request.getParameter("podId");
        String face = request.getParameter("face");
        //this.robotService.testRobotLock(sectionId, robotId, podId);
        return this.robotService.showPodFace2Workstation(sectionId, workstationId, podId,face);
    }
}
