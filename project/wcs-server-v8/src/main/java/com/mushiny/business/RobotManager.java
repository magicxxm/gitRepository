package com.mushiny.business;

import com.mushiny.beans.*;
import com.mushiny.beans.enums.*;
import com.mushiny.beans.map.NodeList;
import com.mushiny.beans.map.NodeListComparator;
import com.mushiny.beans.order.*;
import com.mushiny.comm.AGVConfigParameters;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import com.mushiny.jdbc.repositories.JdbcRepository;
import com.mushiny.map.MapManager;
import com.mushiny.mq.ISender;
import com.mushiny.mq.MessageReceiver;
import com.mushiny.mq.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 未检查	                -9	还未检查小车处于什么状态，不能下发任务
 * 错误状态	            -1	目前小车处于故障状态，不能下发任务
 * 等待任务	            1	此时小车处于准备接收任务状态，可以下发任务
 * 睡眠状态	            0	小车进入睡眠状态后，进入低功耗模式，此时不能下发任务（只能下发结束休眠指令），由“结束休眠”指令终止该状态。“结束休眠”后小车转为等待任务状态
 * 已赋予任务状态	        2	小车接受任务后转变为该状态，此状态可以继续下发任务，在当前任务未执行完之前，后续任务会存入缓存，
 * 但是后续任务与前一条任务路径必须是首尾相连的，否则会弃掉，因此该状态下可以下发任务，但不建议下发
 * 充电状态	            3	WCS端需要判断小车当前电量是否可执行任务，周期性状态数据包（每10s发一次）中可以获取小车当前电量，
 * 当小车电量满足执行任务标准或者已充满电，则可以下发任务
 * 停到最近二维码，减速到0	4	该指令是在AGV故障或者需要人工处理时发给AGV的，因此该状态的AGV也不能下发路径，故障排除后可以发“启动指令”让小车继续执行当前任务
 * 心跳或实时数据超时	    7	心跳或实时数据超时，有可能小车故障或者断开连接，因此不能下发任务
 * 位置不改变超时	        8	此消息用于向WCS预警，当前AGV未赋予任务时不会触发该事件，该状态指已赋予任务但是位置不改变超时，此时可能需要人工处理，因此不能下发任务
 * 急停状态	            10	急停和所有电机断电指令只能是紧急情况才能下发，（指令下发后AGV状态才改变为急停或者电机断电状态），所以不能下发任务，
 * 故障或者异常排查后AGV转为等待任务状态才可以下发
 * 所有电机断电         	13
 * 清除AGV缓存路径	        16	排除故障或者异常时发送该任务指令，不能下发任务
 * 清除RCS缓存路径	        19
 * 断开连接状态	            21	网络连接断开，发送也接收不到，因此不能下发任务
 * 断线重连状态	            24	有可能重连失败，不能下发任务，若小车在任务中断线重连成功则继续执行当前任务，若断线重连失败则视为断开连接
 */

/*
* 订单、小车、状态三者的关系
* 1、循环查找订单，因为有任务执行的小车一直在动，
*
* */

@Component
//@Transactional
@org.springframework.core.annotation.Order(value = 4)
public class RobotManager implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(RobotManager.class);


    //@Autowired
    private SystemPropertiesManager systemPropertiesManager;
    //@Autowired
    private WebApiBusiness webApiBusiness;
    @Value("${rcs.scan.pod2storage.addrList}")
    private String rcsScanPod2StorageAddrList;
    @Value("${rcs.scan.pod2storage.isUse}")
    private String rcsScanPod2StorageIsUse;
    @Value("${robot.chargingRetry.address}")
    private String robotChargingRetryAddress;


    /**
     * MD_ROBOT
     **/
    private Map<Long, Robot> basicRobots = new ConcurrentHashMap<>();
    /**
     * 因为小车是跟section无关的 可以换section工作
     * 要确定小车是否可用的表WCS_ROBOTS与MD_ROBOTS必须分开 WCS_ROBOT会不停更新 并且保留section_id属性
     * 小车可以不登录直接使用，只要WCS_ROBOT表里存在它的记录 并且section_id一致
     * 如果不一致 需要重新登录确认
     * 这两张表的记录都会启动时加载
     */

    private Map<Long, Robot> registRobots = new ConcurrentHashMap<>();
    private boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Map<Long, Robot> getRegistRobots() {
        return registRobots;
    }

    //@Autowired
    private JdbcRepository jdbcRepository;
    //@Autowired
    private PodManager podManager;
    //@Autowired
    private WareHouseManager wareHouseManager;
    //@Autowired
    private MessageSender messageSender;

    //锁格超时触发的已修改COST集合
    private Map<String, Long> newCostAddressMap = new HashMap<String, Long>();

    @Autowired
    public RobotManager(SystemPropertiesManager systemPropertiesManager,
                        WebApiBusiness webApiBusiness, JdbcRepository jdbcRepository,
                        PodManager podManager, WareHouseManager wareHouseManager,
                        MessageSender messageSender,
                        MapManager mapManager, OrderManager orderManager) {
        this.systemPropertiesManager = systemPropertiesManager;
        this.webApiBusiness = webApiBusiness;
        this.jdbcRepository = jdbcRepository;
        this.podManager = podManager;
        this.wareHouseManager = wareHouseManager;
        this.messageSender = messageSender;
        this.mapManager = mapManager;
        this.orderManager = orderManager;
    }

    //@Autowired
    private MapManager mapManager;
    //@Autowired
    private OrderManager orderManager;


    /**
     * 新执行与重新规划分开写
     * 路径只能执行一条，因为下一条的执行只能等上一条结束事件发生时
     * 如果没有下一条了,就把状态设置成可用Avaliable(可能是working也可能是idle)，等待分配新的任务
     *
     * @param order
     */
    private boolean doOrder(Order order) {

        //2、是否需要挪POD
        //movePodOfAddressGroup(order, robot);
        //3、调度单初始化
        try {
            order.initOrder();
        } catch (Exception e) {
            order.setOrderError(99);
            order.unlockEndAddress();
            logger.error("调度单初始化失败 Order:" + order, e);
        }

        if (order.getOrderError() > 0) {//不成功返回
            logger.error("调度单初始化失败,取消当前调度单! Order:" + order);
            //order.cancel();//TODO 有点简单粗暴
            order.getRobot().setLastOrderId(order.getOrderId());
            order.getRobot().setLastOrderError(order.getOrderError() + ":" + OrderErrorMessage.getMsg(order.getOrderError()));
            Pod orderPod = order.getPod();
            order.getRobot().setLastOrderPod(orderPod == null ? "" : orderPod.getPodName());
            order.getRobot().setLastPath(order.getMessage());
            return false;
        }
        order.getRobot().setLastOrderId(order.getOrderId());
        order.getRobot().setLastOrderError(0 + "");
        //4、生成消息任务，发送RCS
        order.sendMessage2Rcs();
        order.process();

        logger.debug("Order:" + order.getOrderId() + "任务已下发!");
        return true;
    }


    public Robot getRobotById(Long robotId) {
        return basicRobots.get(robotId);
    }

    /**
     * Long	robotID
     * Long	sectionID
     * long	currentAddress
     * List<Long>	unWalkCells
     * Long	time
     * <p>
     * 临时不可走的点
     *
     * @param data
     */
    public void ON_RCS_WCS_SP_UNWALK_CELL(Map data) {
        logger.debug("处理锁格超时、位置不改变超时的不可走的点:ON_RCS_WCS_SP_UNWALK_CELL" + data);
        Long robotId = CommonUtils.parseLong("robotID", data);
        Long rcs_sectionId = CommonUtils.parseLong("sectionID", data);
        if (failCheckRobot(robotId, rcs_sectionId)) return;

        Section section = this.wareHouseManager.getSectionByRcsSectionId(rcs_sectionId);
        List<Long> addrs = (List<Long>) data.get("unWalkCells");
        List<String> addrStrs = new ArrayList<>();
        for (int i = 0; i < addrs.size(); i++) {
            Long addr = addrs.get(i);
            addrStrs.add(addr + "");
        }
        this.wareHouseManager.batchUpdateCells(section.getSection_id(), addrStrs, AddressStatus.UNAVAILABLE);
        data.put("sectionID", rcs_sectionId);
        this.messageSender.sendMsg2MapMonitor(data, ISender.WCS_MAP_SP_UNWALK_CELL);
        logger.debug("处理锁格超时、位置不改变超时的不可走的点结束!");
        return;
    }


    /*
        机器ID	Long	robotID
        session ID	Long	sessionID
        时间戳	Long	rtTime
        地址码ID	Long	addressCodeID
        地址码坐标x	Short	addressCodeInfoX
        地址码坐标y	Short	addressCodeInfoY
        地址码偏移角	Float	addressCodeInfoTheta
        货架ID	Long	podCodeID
        货架坐标x	Short	podCodeInfoX
        货架坐标y	Short	podCodeInfoY
        货架偏移角	Float	podCodeInfoTheta
        实时速度	Short	speed
    * */
    private static String getPodByIndexAndSection = "select * from md_pod where pod_index=? and section_id=? and placemark=0";

    /**
     * 监听实时状态信息 只更新 不作为任务发起的条件
     *
     * @param data
     */
    //@Transactional
    public void ON_RCS_WCS_ROBOT_RT(Map data) {
        Long robotId = CommonUtils.parseLong("robotID", data);
        //logger.debug("处理小车:" + robotId + "实时状态:ON_RCS_WCS_ROBOT_RT" + data);
        Long rcs_sectionId = CommonUtils.parseLong("sectionID", data);

        if (failCheckRobot(robotId, rcs_sectionId)) return;
        Robot robot = this.getRobotById(robotId);
        if (robot.getStatus() == Robot.NOTCHECKED) {
            logger.error("小车未登录，Robot:" + robot.getRobotId());
            return;//未登录
        }


        long addressCodeID = CommonUtils.parseLong("addressCodeID", data);

        Section section = this.wareHouseManager.getSectionByRcsSectionId(rcs_sectionId);
        Address curAddr = this.wareHouseManager.getAddressByAddressCodeId(addressCodeID, section);
        if (curAddr != null) {
            //更新内存状态 关键的地址只能有实时包获取
            String preAddr = robot.getAddressId();
            robot.setAddressId(addressCodeID + "");
            curAddr.setRobot(robot);
            Address preAddress = this.wareHouseManager.getAddressByAddressCodeId(preAddr, section);
            if (preAddress != null && Objects.equals(preAddress.getRobot(), robot)) {
                preAddress.setRobot(null);
            }
        }
        int xPosition = curAddr == null ? 0 : curAddr.getxPosition();
        int yPosition = curAddr == null ? 0 : curAddr.getyPosition();

        long podId = CommonUtils.parseLong("podCodeID", data);
        //System.out.println("podId=="+podId);
        int podCodeInfoTheta = CommonUtils.parseInteger("podCodeInfoTheta", data);
        podCodeInfoTheta = this.adjustTheta(podCodeInfoTheta);
        logger.debug("收到小车实时包: 小车" + robotId
                + "地址addressCodeID:" + addressCodeID
                + " x=" + xPosition + " y=" + yPosition
                + " podId=" + podId + " podCodeInfoTheta=" + podCodeInfoTheta);


        //修改记录
        CommonUtils.modifyUselessInfo(robot.getNewValue());
        //newValue.put("ROBOT_ID",robotId);
        //robot.getNewValue().put("LAVEBATTERY", laveBattery);
        robot.getNewValue().put("PODCODEINFOTHETA", podCodeInfoTheta);
        robot.getNewValue().put("POD_ID", podId);
        robot.getNewValue().put("ADDRESSCODEID", addressCodeID);
        robot.getNewValue().put("xPosition", xPosition);
        robot.getNewValue().put("yPosition", yPosition);
        robot.getNewValue().put("section_Id", section.getSection_id());

        Pod pod = this.podManager.getPodByRcsPodId(podId, section);//this.podManager.getPod(section.getSection_id(),podId+"");
        //rcs预先放置的pod
        logger.debug("判断pod是否存在pod=" + pod);
        if ("true".equalsIgnoreCase(this.rcsScanPod2StorageIsUse) && podId != 0L && pod == null && curAddr != null) {
            logger.debug("rcs预先放置的pod=" + podId);
            List<Map> list = this.jdbcRepository.queryBySql(getPodByIndexAndSection.toUpperCase(), podId, section.getSection_id());
            if (list != null && !list.isEmpty()) {
                Map podMap = list.get(0);
                logger.debug("POD加载到WCS中，pod=" + podMap);
                Pod newPod = new Pod();
                newPod.setIsNewCreated(1);
                newPod.setPodId(CommonUtils.parseString("ID", podMap));
                newPod.setAddress(curAddr);
                newPod.setRcsPodId(CommonUtils.parseLong("POD_INDEX", podMap));
                //String podName = "P"+"0000000".substring((podId+"").length())+podId;
                newPod.setPodName(CommonUtils.parseString("NAME", podMap));
                newPod.setDirect(podCodeInfoTheta);
                newPod.setSectionId(section.getSection_id());
                robot.setPod(newPod);
                //将POD目的地设置成-1
                newPod.addKV("PLACEMARK", addressCodeID);
                newPod.addKV("TOWARD", podCodeInfoTheta);
                newPod.addKV("ADDRCODEID_TAR", -1);
                newPod.addKV("XPOS_TAR", -1);
                newPod.addKV("YPOS_TAR", -1);
                newPod.addKV("XPOS", xPosition);
                newPod.addKV("YPOS", yPosition);
                this.jdbcRepository.updateBusinessObject(newPod);
                //加到Map里 用于快速检索
                this.podManager.putPod(section.getSection_id(), newPod.getPodId(), newPod);
                //加到List里 用于排序
                this.podManager.addPod2SortList(section.getSection_id(), newPod);
            } else {
                logger.debug("getPodByIndexAndSection查询为空！");
            }
        } else if ("true".equalsIgnoreCase(this.rcsScanPod2StorageIsUse) && pod != null && curAddr != null && pod.getIsNewCreated() == 1) {
            logger.debug("实时更新pod地址：pod=" + pod);
            pod.addKV("PLACEMARK", addressCodeID);
            pod.addKV("TOWARD", podCodeInfoTheta);
            pod.addKV("ADDRCODEID_TAR", -1);
            pod.addKV("XPOS_TAR", -1);
            pod.addKV("YPOS_TAR", -1);
            pod.addKV("XPOS", xPosition);
            pod.addKV("YPOS", yPosition);
            this.jdbcRepository.updateBusinessObject(pod);
        }
        if (pod == null || !Objects.equals(robot.getPod(), pod)) { //如果没有了POD 或者小车绑定的BOD变了
            if (robot.getPod() != null) {
                robot.getPod().setRobot(null);//原先绑定的POD断开
            }
        }
        if (pod != null) {
            robot.setPod(null);//?
            pod.setDirect(podCodeInfoTheta);
            pod.setRobot(robot);
            Address preAddr = pod.getAddress();
            //Address curAddr = this.wareHouseManager.getAddressByAddressCodeId(addressCodeID, section);
            logger.debug("POD:" + podId + "原先的地址是:" + preAddr + " POD现在的地址是:" + curAddr);

            if (curAddr != null) {//防止旋转区把POD地址更新为空
                pod.setAddress(curAddr);
                curAddr.setPod(pod);
                if (preAddr != null && Objects.equals(pod, preAddr.getPod())) {//有可能将其他pod更新了
                    preAddr.setPod(null);
                }
            }
            //POD移动修改状态存储格子的状态
            //1、只修改存储位 2、只修改自己locked的格子与自己离开的格子 3、主要控制组地址的状态
            if(curAddr!=null && preAddr!=null && !Objects.equals(preAddr,curAddr)){
                logger.debug("POD位置发生变化:"+preAddr.getId()+"===>"+curAddr.getId());
                //判断是否是离开存储区的举升点
                if(preAddr.getType()==AddressType.STORAGE){
                    Order order = robot.getCurOrder();
                    logger.debug("判断是否离开了货架举升位置: curOrder" + order);
                    long podUpAddr = this.getPodUpaAddr(order);
                    AddressGroup addressGroup = preAddr.getAddressGroup();
                    if(podUpAddr == Long.parseLong(preAddr.getId())
                            && preAddr.getAddressGroup()==null){
                        logger.debug("离开非组定义存储区:"+preAddr.getId()+"===>"+curAddr.getId());
                        preAddr.setNodeState(AddressStatus.AVALIABLE);
                    }else if(addressGroup != null){
                        Address inner = preAddr.getGroupInnerAddr();
                        Address out = addressGroup.getOutterAddr();
                        Address in = addressGroup.getInnerAddr();
                        if(Objects.equals(out,preAddr) /*&& curAddr.getType()!=AddressType.STORAGE*/) {
                            logger.debug("从外面离开组定义存储区:" + preAddr.getId()
                                    + "===>" + curAddr.getId() + "内部inner:" + inner);
                            if (inner.getPod() != null) {
                                //必须是看有无POD了 看状态可能不对
                                preAddr.setNodeState(AddressStatus.AVALIABLE);
                            } else {
                                preAddr.setNodeState(AddressStatus.RESERVED);
                                //add
                                inner.setNodeState(AddressStatus.AVALIABLE);
                            }
                        }
                        //从里头离开
                        if(Objects.equals(in,preAddr) && !Objects.equals(out,curAddr)){
                            logger.debug("错误路径,从内部离开组定义存储区:" + preAddr.getId()
                                    + "===>" + curAddr.getId() + "内部out:" + out);
                            if (out.getPod() != null) {//从有无pod去判断
                                preAddr.setNodeState(AddressStatus.RESERVED);//不让锁定这个点
                            } else {
                                preAddr.setNodeState(AddressStatus.AVALIABLE);
                                //add
                                out.setNodeState(AddressStatus.RESERVED);
                            }
                        }
                    }
                }
                //判断是否到达终点
                if(curAddr.getLockedBy()==robot.getRobotId()
                        && curAddr.getType()==AddressType.STORAGE){
                    Order order = robot.getCurOrder();
                    logger.debug("判断是否到了终点: curOrder" + order);
                    if(order != null && order.getEndAddr()==addressCodeID){
                        if(curAddr.getAddressGroup() == null){//1 非组定义
                            logger.debug("到达非组定义存储区目的地 robot:"+robotId+" curAddr:"+curAddr);
                            curAddr.setNodeState(AddressStatus.OCCUPIED);
                        }else if (curAddr.getGroupInnerAddr() != null) {//2 在外面
                            logger.debug("到达非组定义的外部节点 robot:"+robotId+" curAddr:"+curAddr);
                            curAddr.setNodeState(AddressStatus.OCCUPIED);
                            Address inner = curAddr.getGroupInnerAddr();
                            if(Objects.equals(inner.getNodeState(),AddressStatus.AVALIABLE)){
                                logger.error("状态错误inner :"+inner +" out:"+curAddr);
                                inner.setNodeState(AddressStatus.RESERVED);//不能分配了
                            }
                        } else {//3 在里面
                            logger.debug("到达非组定义的内部节点 robot:"+robotId+" curAddr:"+curAddr);
                            curAddr.setNodeState(AddressStatus.OCCUPIED);
                            Address out = curAddr.getGroupOutterAddr();
                            if(out != null && out.getLockedBy()==0L){
                                out.setNodeState(AddressStatus.AVALIABLE);
                            }
                        }
                    }
                }
            }
            logger.debug("收到实时包之后,POD" + podId + "原先的地址是:" + preAddr + " POD现在的地址是:" + curAddr);
        }
        //TODO pod与车绑定 最好通过结束包来判断
        //robot.setPod(pod);
        if (robot.getCurOrder() != null) {
            Order order = robot.getCurOrder();
            if (robot.getPod() != null && !Objects.equals(pod, order.getPod())) {
                logger.error("调度单的POD与小车的POD不一致:Robot.getPod(): " + robot.getPod() + " @@@@@ " + order.getPod());
                robot.setPod(null);
            } else {
                robot.setPod(pod);
            }
        }
        //logger.debug("ON_RCS_WCS_ROBOT_RT: robot.isPersistFlag() " + robot.isPersistFlag());
        if (robot.isPersistFlag()) {
            //this.commitRobot(robot, System.currentTimeMillis());
            if (pod != null) {
                pod.addKV("XPOS", xPosition).addKV("YPOS", yPosition)
                        .addKV("PLACEMARK", addressCodeID).addKV("TOWARD", podCodeInfoTheta);
                // 实时包不记录POD位置
                // this.jdbcRepository.updateBusinessObject(pod);
            }
            robot.setPersistFlag(false);
            logger.debug("修改小车+POD记录成功!robot=" + robotId + " Pod:" + podId);
        }
    }

    private long getPodUpaAddr(Order order) {
        if(order == null || order.getWcsPath() == null){
            return 0L;
        }
        return order.getWcsPath().getPodUpAddress();
    }

    private void updatePreAddrStatus(Robot robot, Address preAddr) {
        logger.debug("原先位置"+preAddr.getId()+"是存储点");
        if(preAddr.getAddressGroup()==null){
            preAddr.setNodeState(AddressStatus.AVALIABLE);
        }else{
            Address inner = preAddr.getGroupInnerAddr();
            if(inner == null || Objects.equals(inner.getNodeState(),AddressStatus.OCCUPIED)){
                preAddr.setNodeState(AddressStatus.AVALIABLE);
            }else{
                preAddr.setNodeState(AddressStatus.RESERVED);
            }
        }
    }

    /*private void updateCurrentAddrStatus(Robot robot, Address address) {
         if(robot==null || address==null){
             logger.error("没有robot或者address, 无法操作");
             return;
         }
         if(robot.getCurOrder() == null){
             logger.error("当前小车没有调度任务, 不修改存储位状态robot:"
                     +robot.getRobotId());
             return;
         }
         Order order = robot.getCurOrder();
         if(order instanceof ChargerDriveOrder || order instanceof EmptyRunOrder){
             logger.error("当前小车调度任务是空车跑或者去充电, 不修改存储位状态robot:"
                     +robot.getRobotId());
             return;
         }

    }*/

    private int adjustTheta(int podCodeInfoTheta) {
        //调整角度，如果在46-135 为90度 136 - 225 为180度 226-315 为270度 其他为0度
        if (46 < podCodeInfoTheta && 135 > podCodeInfoTheta) {
            return 90;
        }
        if (136 < podCodeInfoTheta && 225 > podCodeInfoTheta) {
            return 180;
        }
        if (226 < podCodeInfoTheta && 315 > podCodeInfoTheta) {
            return 270;
        }
        return 0;
    }

    public int toAdjustTheta(int podCodeInfoTheta) {
        return this.adjustTheta(podCodeInfoTheta);
    }

    @Scheduled(fixedDelay = 5000l)
    @Transactional
    //(noRollbackFor = RuntimeException.class)
    public void scheduleFetchTask() {
        if (!podManager.isFinisHotCompute()) {
            logger.error("热度计算未完成,无法执行小车调度!");
            return;
        }
        logger.info("小车周期性获取任务.....");
        Iterator<Robot> robotIterator = this.getRegistRobots().values().iterator();
        while (robotIterator.hasNext()) {
            Robot next = robotIterator.next();
            checkIdle(next);
        }
        logger.info("周期性任务结束!");
    }

    private static final Object locker = new Object();

    //统一入口
    public void checkIdle(Robot robot) {
        synchronized (locker) {
            if (System.currentTimeMillis() - robot.getLoginTime() < 40 * 1000L) {
                logger.debug("checkIdle:robotId=" + robot.getRobotId() + " 的小车登录时间未达到接任务的要求，稍后重新扫描！");
                return;
            }

            /*if (robot.getStatus() == Robot.IDLE
                    && robot.isAvaliable() && robot.getCurOrder() != null
                    && !robot.getCurOrder().isFinished()// order is not finished
                    && !CommonUtils.isEmpty(robot.getAddressId())) {
                logger.debug("上次任务出错未执行完毕!" + robot.getCurOrder());
                this.resendOrder(robot.getSectionId(), robot.getRobotId() + "", "false");
                return;
            }*/

            if (robot.getStatus() == Robot.IDLE
                    && robot.isAvaliable() && robot.getCurOrder() == null
                    && !CommonUtils.isEmpty(robot.getAddressId())) {

                //处理空闲时收到的心跳包 实时包
                robot.setAvaliable(false);
                Order order = this.getRobotNewOrder(robot);
                if (order == null) {
                    logger.debug("小车当前没有任务,RobotId=" + robot.getRobotId() + " AddrCodeID:" + robot.getAddressId());
                    robot.addKV("AVAILABLE", true);
                    robot.addKV("STATUS", Robot.IDLE);
                    robot.setAvaliable(true);
                    this.jdbcRepository.updateBusinessObject(robot);
                    logger.debug("执行任务后小车状态已修改:" + robot);
                    return;
                }
                //设置order的一些信息
                order.setRobotManager(this);
                order.setJdbcRepository(this.jdbcRepository);
                order.setWareHouseManager(this.wareHouseManager);
                order.setWebApiBusiness(this.webApiBusiness);
                order.setPodManager(this.podManager);
                order.setSystemPropertiesManager(this.systemPropertiesManager);

                //马上修改robot status TODO 可能存在并发
                logger.debug("空闲的robot:" + robot.getRobotId() + "准备执行调度单:" + order.getOrderId());
                robot.setCurOrder(order);
                robot.setAvaliable(true);//如果doOrder出异常 下次循环还可以操作，否则一直就不执行了
                if (this.doOrder(order)) {
                    robot.setStatus(Robot.WORKING);
                    robot.setAvaliable(false);//不可选了
                    robot.addKV("STATUS", robot.getStatus()).addKV("AVAILABLE", robot.isAvaliable());
                    Address address = this.wareHouseManager.getAddressByAddressCodeId(order.getEndAddr(), robot.getSectionId());
                    if (order.getPod() != null && address != null) {
                        Pod pod = order.getPod();
                        pod.addKV("ADDRCODEID_TAR", address.getId());
                        pod.addKV("XPOS_TAR", address.getxPosition());
                        pod.addKV("YPOS_TAR", address.getyPosition());
                        this.jdbcRepository.updateBusinessObject(pod);
                    }
                    //更新小车
                    //robot.addKV("TARGETADDRCODEID", order.getDestAddr());
                    robot.addKV("AVAILABLE", false);//
                    robot.addKV("STATUS", Robot.WORKING);//TODO 充电时要改成charging？
                    this.jdbcRepository.updateBusinessObject(robot);
                    logger.debug("执行任务后小车与POD状态已修改:" + robot);
                } else {
                    robot.setStatus(Robot.IDLE);//没有可执行的 即可将调度单设置成完成
                    robot.setCurOrder(null);
                    robot.setAvaliable(true);
                    robot.addKV("AVAILABLE", true);//
                    robot.addKV("STATUS", Robot.IDLE);//TODO 充电时要改成charging？
                    this.jdbcRepository.updateBusinessObject(robot);
                    logger.debug("小车状态空闲，等待下一个调度单:robotId:" + robot.getRobotId());
                }
            } else {
                logger.info("小车未处于空闲状态或者没有当前地址:robotId:" + robot.getRobotId() + " status：" + robot.getStatus() + " isAvailable:" + robot.isAvaliable() + " currentOrder:" + robot.getCurOrder() + " addrcode:" + robot.getAddressId());
            }
        }
    }

    private boolean podOnRobot(Robot robot) {
        Pod pod = robot.getPod();
        if (pod == null) {
            return false;
        }
        Address address = pod.getAddress();
        return address != null && address.getType() != AddressType.STORAGE;
    }

    private void checkIfAvaliableOnCharging(Robot robot, long current) {
        logger.debug("检查是否在充电并且充电满足条件:robot.getRobotId() : "
                + robot.getRobotId() + " robot.getChargingTime():"
                + CommonUtils.convert2String(new Date(robot.getChargingTime()), "YYYY-MM-dd HH:mm:ss")
                + " robot.getLaveBattery():" + robot.getLaveBattery()
                + " robot.getChargeNum():" + robot.getChargeNum()
                + " robot.getFullChargeFlag():" + robot.getFullChargeFlag()
                + " robot.getVoltage():" + robot.getVoltage()
                + " robot.getFullChargeTime():" + CommonUtils.convert2String(new Date(robot.getFullChargeTime()), "YYYY-MM-dd HH:mm:ss")
                + " robot.getCurOrder():" + robot.getCurOrder());
        Order order = robot.getCurOrder();
        //通过调度单判断即可,修改状态的入口太多
        if (order == null || !(order instanceof ChargerDriveOrder)) {
            return;
        }
        //小车经常会发送不一致的状态
        if (robot.getStatus() != Robot.CHARGING) {
            return;
        }
        String DriveChargerTimeValue = systemPropertiesManager.getProperty("DriveChargerTimeValue", robot.getWareHouseId());
        if (DriveChargerTimeValue == null || "".equals(DriveChargerTimeValue)) {
            DriveChargerTimeValue = "5";//默认充电五分钟 通话一小时
        }

        String DriveOutChargerMinValue = systemPropertiesManager.getProperty("DriveOutChargerMinValue", robot.getWareHouseId());
        if (DriveOutChargerMinValue == null || "".equals(DriveChargerTimeValue)) {
            DriveOutChargerMinValue = "50";//默认电量大于50%时 可调度
        }

        String RobotVoltageMaxValue = systemPropertiesManager.getProperty("RobotVoltageMaxValue", robot.getWareHouseId());
        if (RobotVoltageMaxValue == null || "".equals(RobotVoltageMaxValue)) {
            RobotVoltageMaxValue = "60000";//默认最大电压60000mv时 强制完成充电
        }

        logger.debug("开始充电时间:" + CommonUtils.convert2String(new Date(robot.getChargingTime()), "YYYY-MM-dd HH:mm:ss")
                + "当前时间:" + CommonUtils.convert2String(new Date(current), "YYYY-MM-dd HH:mm:ss")
                + "当前电量:" + robot.getLaveBattery() + " 最小电量:" + DriveOutChargerMinValue
                + "当前电压:" + robot.getVoltage() + " 最大电压:" + RobotVoltageMaxValue);
        //检查小车是否充电成功
        /*if (robot.getStartLaveBattery() != -1 && robot.getChargingTime() > 0) {
            //只要重新充上电，还原重试充电次数为0
            if(robot.getLaveBattery() > robot.getStartLaveBattery()){
                logger.debug("小车："+robot.getRobotId()+"已经充上电，清空重试充电次数");
                robot.setChargingRetryNum(0);
            }
            if ((System.currentTimeMillis() - robot.getChargingTime() > 3 * 60 * 1000L) && robot.getLaveBattery() <= robot.getStartLaveBattery() && robot.getChargingRetryNum() < 3) {
                logger.debug("小车："+robot.getRobotId()+"充电失败，准备调离充电桩重试");
                robot.setChargingTime(0L); //假如此时又上报了充电状态 垃圾代码
                robot.setStartLaveBattery(-1);//假如此时又上报了充电状态 垃圾代码
                robot.setIsChargingRetry(1);
                robot.setChargingRetryNum(robot.getChargingRetryNum() + 1);//?
                Order curOrder = robot.getCurOrder();
                if (TripType.CHARGER_DRIVE.equalsIgnoreCase(curOrder.getType())){
                    logger.debug("小车："+robot.getRobotId()+"正在生成EmptyRun调度单,目标地址："+robotChargingRetryAddress);
                    robot.setAvaliable(true);
                    this.drive(robot.getRobotId(), robotChargingRetryAddress);
                    this.jdbcRepository.updateBySql(UPDATE_TRIP, TripStatus.FINISHED, curOrder.getId());
                    logger.debug("小车："+robot.getRobotId()+"上一个充电调度单已结束，orderId="+curOrder.getOrderId());
                    robot.setStatus(Robot.IDLE);
                    robot.setLastOrderId(null);
                    robot.setCurOrder(null);
                }
            }
        }*/

        //判断是否满充
        if (robot.getFullChargeFlag()) {
            String ChargeProtectValue = systemPropertiesManager.getProperty("ChargeProtectValue", robot.getWareHouseId());
            if (ChargeProtectValue == null || "".equals(ChargeProtectValue)) {
                ChargeProtectValue = "94";//默认满充最大电量，执行满充任务时，达到此电量才会结束调度单
            }
            logger.debug("小车:" + robot.getRobotId() + " 正在执行满充任务,电量必须达到：" + ChargeProtectValue + "%");
            if (robot.getLaveBattery() >= Integer.parseInt(ChargeProtectValue)) {
                //当前满充调度单结束掉
                logger.debug("结束满充调度单:" + robot.getCurOrder());
                boolean isOnCharging = Objects.equals(robot.getAddressId(), robot.getCurOrder().getEndAddr() + "")
                        && robot.getStatus() == Robot.CHARGING;
                logger.debug("isOnCharging:" + isOnCharging + " Robot:" + robot.getRobotId());
                boolean finished = isOnCharging
                        && this.orderManager.finishOrder(order, robot);
                if (finished) {
                    logger.debug("清空满充调度单:" + robot.getCurOrder());
                    robot.getNewValue().put("AVAILABLE", Boolean.TRUE);
                    robot.getNewValue().put("STATUS", Robot.IDLE);
                    //this.commitRobot(robot, current);
                    robot.setStatus(Robot.IDLE);
                    robot.setAvaliable(true);
                    robot.setCurOrder(null);
                } else {
                    logger.error("结束满充调度单失败:" + robot + " 可能未处于充电状态或者未到达充电位置!");
                }
            }
            return;
        }
        //先写死五分钟
        if ((Integer.parseInt(DriveChargerTimeValue)) * 60 * 1000 < (current - robot.getChargingTime())
                && robot.getLaveBattery() >= Integer.parseInt(DriveOutChargerMinValue)) {
            //当前充电调度单结束掉
            logger.debug("结束充电调度单:" + robot.getCurOrder());
            boolean isOnCharging = Objects.equals(robot.getAddressId(), robot.getCurOrder().getEndAddr() + "")
                    && robot.getStatus() == Robot.CHARGING;
            logger.debug("isOnCharging:" + isOnCharging + " Robot:" + robot.getRobotId());
            boolean finished = isOnCharging
                    && this.orderManager.finishOrder(order, robot);
            if (finished) {
                logger.debug("清空充电调度单:" + robot.getCurOrder());
                robot.getNewValue().put("AVAILABLE", Boolean.TRUE);
                robot.getNewValue().put("STATUS", Robot.IDLE);
                //this.commitRobot(robot, current);
                robot.setStatus(Robot.IDLE);
                robot.setAvaliable(true);
                robot.setCurOrder(null);
            } else {
                logger.error("结束充电调度单失败:" + robot + " 可能未处于充电状态或者未到达充电位置!");
            }
        } else {
            logger.info("小车未达到充电完成的条件 Robot:" + robot);
        }
    }

    @Override
    public void run(String... strings) throws Exception {
        logger.debug("加载仓库机器人小车信息....");
        List<Map> rows = this.jdbcRepository.queryByKey("RobotManager.loadBasicRobots");
        for (int i = 0; i < rows.size(); i++) {
            Map row = rows.get(i);
            Robot robot = new Robot();
            Long robot_id = CommonUtils.parseLong("ROBOT_ID", row);
            String password = CommonUtils.parseString("PASSWORD", row);
            int batteryNumber = CommonUtils.parseInteger("BATTERY_NUMBER", row);
            robot.setRobotId(robot_id);
            robot.setPassword(password);
            robot.setBatteryNumber(batteryNumber);
            this.basicRobots.put(robot_id, robot);
        }
        this.jdbcRepository.updateByKey("RobotManager.updateRobotStatus");
        List<Map> rows2 = this.jdbcRepository.queryByKey("RobotManager.loadRegistedRobots");
        for (int i = 0; i < rows2.size(); i++) {
            Map row = rows2.get(i);
            Long robot_id = CommonUtils.parseLong("ROBOT_ID", row);
            String pkId = CommonUtils.parseString("ID", row);
            Robot robot = this.basicRobots.get(robot_id);
            robot.setPkId(pkId);
            robot.setSectionId(CommonUtils.parseString("SECTION_ID", row));
            Section section = this.wareHouseManager.getSectionById(robot.getSectionId());
            robot.setStatus(CommonUtils.parseInteger("STATUS", row));
            robot.setVoltage(CommonUtils.parseInteger("VOLTAGE", row));
            robot.setChargeNum(CommonUtils.parseInteger("CHARGENUM", row));
            Timestamp timestamp = (Timestamp) row.get("FULLCHARGETIME");
            if (timestamp != null) {
                robot.setFullChargeTime(timestamp.getTime());
            } else {
                robot.setFullChargeTime(System.currentTimeMillis());
            }

            //robot.setStatus(Robot.IDLE);
            robot.setOldStatus(CommonUtils.parseInteger("OLDSTATUS", row));
            /*String podId = CommonUtils.parseString("POD_ID", row);
            if (!CommonUtils.isEmpty(podId)) {
                logger.debug("robot绑定pod: " + robot.getRobotId() + ":" + podId);
                robot.setPod(this.podManager.getPod(robot.getSectionId(), podId));
            }*/
            robot.setAddressId(CommonUtils.parseString("ADDRESSCODEID", row));
            Address address = this.wareHouseManager.getAddressByAddressCodeId(robot.getAddressId(), section);
            address.setRobot(robot);
            robot.setWareHouseId(CommonUtils.parseString("WAREHOUSE_ID", row));
            //增加avaliable 服务器启动后 robot的状态是unchecked （未登录） avaliable是false
            //robot.setAvaliable(CommonUtils.parseBoolean("AVAILABLE", row));
            robot.setAvaliable(false);
            this.registRobots.put(robot_id, robot);
            robot.addPropertyChangeListener(new RobotStatusChangeListener());
        }
        logger.debug("加载仓库机器人小车信息结束!小车总数:" + rows.size() + " 已注册小车数" + rows2.size());
        this.flag = true;
        //验证所有小车状态
        this.queryRobotStatus();
        //通知路径计算服务：增大存储位上有pod的地址的重车cost
        updatePodAddressHeavyCost();
    }

    /**
     * 更新储位的重车cost
     *
     * @return
     */
    public String updatePodAddressHeavyCost() {
        logger.debug("调用updatePodAddressHeavyCost");
        StringBuilder stopPodAddrBuilder = new StringBuilder();
        String stopPodAddrStr = "";
        Map<String, WareHouse> warehouseMap = this.wareHouseManager.getWareHouseMap();
        for (Map.Entry<String, WareHouse> whEntry : warehouseMap.entrySet()) {
            WareHouse wareHouse = whEntry.getValue();
            String cost = "";
            String podStop_HeavyCost = this.systemPropertiesManager.getProperty("Cost_PodStop_Heavy", wareHouse.getWareHouseId());
            if (!CommonUtils.isEmpty(podStop_HeavyCost)) {
                cost = podStop_HeavyCost;
            } else {
                cost = "100";
            }
            Map<String, Section> sectionMap = wareHouse.sectionMap;
            for (Map.Entry<String, Section> secEntry : sectionMap.entrySet()) {
                stopPodAddrBuilder.setLength(0);
                Section section = secEntry.getValue();
                List<Map> stopPodAddrMap = this.podManager.getPods4Rcs(section.getSection_id());
                if (stopPodAddrMap == null || stopPodAddrMap.isEmpty()) {
                    continue;
                }
                for (int i = 0; i < stopPodAddrMap.size(); i++) {
                    Map<Long, String> map = stopPodAddrMap.get(i);
                    for (Map.Entry<Long, String> podAddrEntry : map.entrySet()) {
                        String addrCodeId = podAddrEntry.getValue();
                        logger.debug("查询所有存储位有POD停放的地址：warehouseId=" + wareHouse.getWareHouseId()
                                + ", sectionId=" + section.getSection_id() + ", addressCodeId=" + addrCodeId + ", podId=" + podAddrEntry.getKey());
                        stopPodAddrBuilder.append(stopPodAddrBuilder.length() == 0 ? addrCodeId : "," + addrCodeId);
                    }
                }
                stopPodAddrStr = stopPodAddrBuilder.toString();
                logger.debug("通知path-planning服务增大warehouseId=" + wareHouse.getWareHouseId() + ", sectionId=" + section.getSection_id() + " 的重车路径的Cost=" + cost + ", addressList=" + stopPodAddrStr);
                this.webApiBusiness.addHeavyCost(wareHouse.getWareHouseId(), section.getSection_id(), stopPodAddrStr, cost);
            }
        }
        logger.debug("所有仓库中的所有地图存储位有POD的地址，重车Cost已经增大完毕！");
        return "ok";
    }

    /**
     * 查询小车状态 只发消息 等RCS回复
     */
    private void queryRobotStatus() {
        Iterator<Robot> iterator = this.registRobots.values().iterator();
        while (iterator.hasNext()) {
            Robot robot = iterator.next();
            this.sendQueryMessage(robot);
        }
    }

    /*
    long robotID
    long sectionID
    long time
    QUEUE : WCS_RCS_QUERYROBOT_REQUEST
    RESPONSE: RCS_WCS_QUERYROBOT_RESPONSE
    * */
    private void sendQueryMessage(Robot robot) {
        Map msg = new HashMap();
        msg.put("robotID", robot.getRobotId());
        Section section = this.wareHouseManager.getSectionById(robot.getSectionId());
        msg.put("sectionID", section.getRcs_sectionId());
        msg.put("time", System.currentTimeMillis());
        this.messageSender.sendMsg(ISender.EXCHANGE, ISender.WCS_RCS_QUERYROBOT_REQUEST, msg);
        logger.debug("WCS请求Robot状态 Robot : " + robot.getRobotId() + " data:" + msg);
    }

    /*
    机器ID	Long	robotID
    session ID	Long	sessionID
    IP	String	ip
    登录时间(秒)-时间戳	Long	loginTime
    密码	Long	password
    机器型号	Short	robotType
    硬件版本	Short	hardwareVersion
    软件版本	Short	softwareVersion
    出厂日期	Long	manufactureDate
    累计时长	Long	TotalTime
    最近维修时间	Long	recentFixDate
    剩余电量	int	laveBattery
    地址码ID	Long	addressCodeID
    地址码坐标X 	Short	addressCodeInfoX
    地址码坐标Y	Short	addressCodeInfoY
    地址码偏移角	Float	addressCodeInfoTheta
    货架ID	Long	podCodeID
    货架x坐标	Short	podCodeInfoX
    货架y坐标	Short	podCodeInfoY
    货架偏移角	Float	podCodeInfoTheta
    所选货架ID	Long	selectedPodCodeID
    举升状态（0：放下1：举起）	Short	upStatus
    充电状态（0：未充电 1：充电 ）	Short	chargeStatus
    错误ID	int	errorID
    信号质量	Short	signalQuality
    入侵检测次数	Int	InvasionDetection
    * */
    private Map<Long, Robot> reLoginRobots = new ConcurrentHashMap<Long, Robot>();

    @Transactional
    public void ON_RCS_WCS_ROBOT_LOGIN(Map data) {
        logger.debug("处理小车登录请求ON_RCS_WCS_ROBOT_LOGIN:" + data);
        Long robotId = CommonUtils.parseLong("robotID", data);
        Long rcs_sectionId = CommonUtils.parseLong("sectionID", data);
        String ip = (String) data.get("ip");
        int agvType = CommonUtils.parseInteger("robotType", data);
        long addressCodeID = Long.parseLong(data.get("addressCodeID") + "");
        //int xPosition = CommonUtils.parseInteger("addressCodeInfoX", data);
        //int yPosition = CommonUtils.parseInteger("addressCodeInfoY", data);
        int laveBattery = CommonUtils.parseInteger("laveBattery", data) / 10;
        int voltage = CommonUtils.parseInteger("voltage", data);
        int podCodeInfoTheta = CommonUtils.parseInteger("podCodeInfoTheta", data);
        podCodeInfoTheta = this.adjustTheta(podCodeInfoTheta);
        boolean upStatus = CommonUtils.parseBoolean("upStatus", data);//举升状态
        boolean chargeStatus = CommonUtils.parseBoolean("chargeStatus", data);//充电状态
        /*String pod_id = "0";*/
        Robot robot = this.getRobotById(robotId);
        if (robot == null) {
            logger.error("基础数据没有定义该小车 robotId:" + robotId);
            return;
        }

        //if (checkAddr(rcs_sectionId, addressCodeID, xPosition, yPosition)) return;

        Section section = this.wareHouseManager.getSectionByRcsSectionId(rcs_sectionId);
        Address address = this.wareHouseManager.getAddressByAddressCodeId(addressCodeID, section);
        if (address == null) {
            logger.error("小车登录后，没有出现在地图合适的位置:" + addressCodeID);
            return;
        }
        logger.debug("小车:" + robotId + " 登录,所在地图是:" + section.getSection_id());
        robot.setQueueMsg(null);
        Map msg = new HashMap();
        msg.put("robotID", data.get("robotID"));
        msg.put("sectionID", rcs_sectionId);
        msg.put("time", System.currentTimeMillis());
        if (!robot.getPassword().equals(CommonUtils.parseString("password", data))) {
            //登录失败
            msg.put("isLogin", 0);
            this.messageSender.WCS_RCS_ROBOT_LOGIN_RESPONSE(msg);
            logger.debug("消息:" + msg + "发送成功!");
            return;
        }

        //登录成功
        msg.put("isLogin", 1);
        robot.setLoginTime(System.currentTimeMillis());
        int x = address.getxPosition();//CommonUtils.parseInteger("addressCodeInfoX", data);
        int y = address.getyPosition();//CommonUtils.parseInteger("addressCodeInfoY", data);

        long podId = CommonUtils.parseLong("podCodeID", data);
        Pod pod = this.podManager.getPodByRcsPodId(podId, section);//P0000001 p+7位数字 补0
        if (pod != null && upStatus) {//存在podId
            //pod_id = pod.getPodId();
            pod.setRobot(robot);//将POD与机器人绑定
        }

        //将小车状态更新到数据库
        boolean isLogin = getRegistedRobot(robotId) != null;
        if (!isLogin) {
            //新增一条记录
            Map record = robot.getNewValue();
            CommonUtils.genUselessInfo(record);
            robot.setRobotId(robotId);
            String robotPKID = CommonUtils.genUUID();
            record.put("ROBOT_ID", robotId);
            record.put("WAREHOUSE_ID", section.getWareHouse_id());
            record.put("ID", robotPKID);
            record.put("IP", ip);
            record.put("LAVEBATTERY", laveBattery);
            record.put("VOLTAGE", voltage);
            record.put("PODCODEINFOTHETA", podCodeInfoTheta);
            record.put("POD_ID", pod == null ? "0" : pod.getPodId());
            record.put("ADDRESSCODEID", addressCodeID);
            record.put("xPosition", x);
            record.put("yPosition", y);
            record.put("TYPEID", agvType);
            record.put("section_Id", section.getSection_id());
            record.put("name", "robot" + robotId);
            //初始化满充时间戳
            Long fullChargerTime = System.currentTimeMillis();
            robot.setFullChargeTime(fullChargerTime);
            record.put("FULLCHARGETIME", new Timestamp(fullChargerTime));
            //状态
            if (chargeStatus) {
                record.put("STATUS", Robot.CHARGING);
            } else {
                record.put("STATUS", Robot.IDLE);
                record.put("AVAILABLE", true);//登录时能否设置成TRUE？
                // 调度单生成应该根据1）没有调度单 2）有调度单小车目的地已经生成 加上小车可用的几个条件综合判断能否新建
            }
            record.put("PODCODEINFOTHETA", podCodeInfoTheta);
            robot.setStatus((Integer) robot.getNewValue().get("STATUS"));
            //this.jdbcRepository.insertRecord("WCS_ROBOT", record);
            this.jdbcRepository.insertBusinessObject(robot);
            //robot.setPkId((String) robot.getNewValue().get("ID"));
            robot.setPkId(robotPKID);
            logger.debug("新增小车记录成功!");
        } else {
            //修改记录
            Map newValue = robot.getNewValue();
            CommonUtils.modifyUselessInfo(newValue);
            //newValue.put("ROBOT_ID",robotId);
            newValue.put("IP", ip);
            newValue.put("LAVEBATTERY", laveBattery);
            newValue.put("WAREHOUSE_ID", section.getWareHouse_id());
            newValue.put("VOLTAGE", voltage);
            newValue.put("PODCODEINFOTHETA", podCodeInfoTheta);
            newValue.put("POD_ID", pod == null ? "0" : pod.getPodId());
            newValue.put("ADDRESSCODEID", addressCodeID);
            newValue.put("xPosition", x);
            newValue.put("yPosition", y);
            newValue.put("AGVTYPE", agvType);
            newValue.put("section_Id", section.getSection_id());
            //newValue.put("name","robot"+robotId);
            newValue.put("STATUS", Robot.IDLE);//默认设置为空闲 等待分配第一个有效的调度单组
            newValue.put("AVAILABLE", true);//登录时能否设置成TRUE？
            newValue.put("PODCODEINFOTHETA", podCodeInfoTheta);
            robot.setStatus((Integer) robot.getNewValue().get("STATUS"));
            /*if(CommonUtils.isEmpty((String)robot.getId())){
                if (!CommonUtils.isEmpty((String) robot.getNewValue().get("ID"))){
                    robot.setPkId((String) robot.getNewValue().get("ID"));
                }else{
                    robot.setPkId(CommonUtils.genUUID());
                }
            }*/
            this.jdbcRepository.updateBusinessObject(robot);
            logger.debug("修改小车记录成功!");
        }
        //更新内存中小车的状态
        this.registRobots.put(robotId, robot);
        if (pod != null && upStatus) { //两个条件认为小车绑定了POD
            robot.setPod(pod);
            pod.setDirect(podCodeInfoTheta);
            //扫描到了pod
            pod.addKV("XPOS", x).addKV("YPOS", y)
                    .addKV("PLACEMARK", addressCodeID).addKV("TOWARD", podCodeInfoTheta);
            this.jdbcRepository.updateBusinessObject(pod);
        }
        robot.setAddressId(addressCodeID + "");
        robot.setSectionId(section.getSection_id());
        //int status = podId == 0? Robot.IDLE:Robot.Process;
        //robot.setStatus((Integer) robot.getNewValue().get("STATUS"));
        robot.setLaveBattery(laveBattery);
        robot.setVoltage(voltage);
        //robot.addPropertyChangeListener(new RobotStatusChangeListener());
        //登录时不处理调度

        //robot.setAvaliable(podId == 0);
        //登录时地址不准确 不能执行
        //robot.setAddressId(null);

        /*robot.setCurOrder(null);//将当前调度单清空
        robot.setLastOrderId(null);*/
        if (robot.getCurOrder() != null) {//重登陆过来的
            robot.setLastOrderId(robot.getCurOrder().getOrderId());
            //robot.setAvaliable(false);应该有问题 不用改成false 如果重登陆了
            //robot.setStatus(Robot.WORKING);
            //TODO
            logger.debug("小车重新登录，内存中有任务。robotId=" + robot.getRobotId());
            reLoginRobots.put(robot.getRobotId(), robot);
            //this.resendOrder(robot.getSectionId(),robotId + "");
        } else {
            robot.setAvaliable(true);
            robot.setStatus(Robot.IDLE);
        }
        //内存中小车状态更新完成
        logger.debug("小车:" + robotId + "登录成功!位置是:" + addressCodeID + " 绑定的pod是" + podId);
        this.messageSender.WCS_RCS_ROBOT_LOGIN_RESPONSE(msg);
        logger.debug("消息:" + msg + "发送成功!");
        //检查是否有任务
        //checkIdle(robot);
    }

    @Scheduled(fixedDelay = 5000l)
    public void scheduleDoOrder4ReLoginRobot() {
        logger.debug("定时扫描重新登录小车，判断是否满足下发任务条件");
        if (this.reLoginRobots.isEmpty() || this.reLoginRobots.size() == 0) {
            logger.debug("无重新登录的小车！");
            return;
        }
        Iterator<Robot> iterator = this.reLoginRobots.values().iterator();
        while (iterator.hasNext()) {
            Robot next = iterator.next();
            if (System.currentTimeMillis() - next.getLoginTime() < 40 * 1000L) {
                logger.debug("小车重新登录后，不满足下发任务时间，继续等待,robotId=" + next.getRobotId());
                continue;
            }
            logger.debug("小车重新登录后，满足下发任务时间,robotId=" + next.getRobotId());
            this.resendOrder(next.getSectionId(), next.getRobotId() + "", "");
            this.reLoginRobots.remove(next.getRobotId());//TODO
        }
    }

    public boolean isRegisted(Long robotId) {
        Robot robot = this.registRobots.get(robotId);
        return robot != null && robot.getStatus() != Robot.NOTCHECKED;
    }

    /* 机器ID	Long	robotID
        仓库sectionID 	Long	sectionID
        前一个位置地址码	Long	previousAddress
        当前位置地址码	Long	currentAddress
        时间戳	Long	time*/

    /**
     * AGV位置改变 会触发一系列事件
     * 如果只是收到一个位置改变的型号，没有前期登录信号，不能做处理
     *
     * @param data
     */
    @Transactional
    public void ON_RCS_WCS_AGV_POSITION_CHANGE(Map data) {
        Long robotId = CommonUtils.parseLong("robotID", data);
        Long rcs_sectionID = CommonUtils.parseLong("sectionID", data);
        long time = CommonUtils.parseLong("time", data);

        //如果没有注册 不能执行操作
        if (failCheckRobot(robotId, rcs_sectionID)) return;

        Robot robot = this.getRobotById(robotId);
        if (robot.getStatus() == Robot.NOTCHECKED) {
            logger.debug("小车 " + robot.getRobotId() + " 未登录，不执行ON_RCS_WCS_AGV_POSITION_CHANGE");
            return;//未登录
        }

        logger.debug("处理小车:" + robotId + " 位置改变 ON_RCS_WCS_AGV_POSITION_CHANGE");
        long previousAddress = CommonUtils.parseLong("previousAddress", data);
        long currentAddress = CommonUtils.parseLong("currentAddress", data);
        //判断curAddr是否在地图 有可能是旋转区
        Section section = this.wareHouseManager.getSectionById(robot.getSectionId());
        Address curAddr = this.wareHouseManager.getAddressByAddressCodeId(currentAddress, section);
        if (curAddr == null) {//可能在旋转区
            logger.debug("当前地址码" + currentAddress + "在地图中不存在!");
            return;
        }
        logger.debug("小车位置更新成功：Robot:" + robot.getRobotId()
                + " 位移: " + previousAddress + "====>" + currentAddress);
        robot.setAddressId(currentAddress + "");

        if (previousAddress != currentAddress) {//机器人确实发生移动了
            //只能是驮POD逻辑上才需要
            if (robot.getPod() != null) {
                this.podManager.onPodMove(robot.getPod(), previousAddress, currentAddress, robot.getCurOrder());
            }
            robot.setAddressId(currentAddress + "");
            //this.onRobotMove(robot, previousAddress, currentAddress);
        }

        //更新小车位置信息
        //Map newData = robot.getNewValue();
        robot.addKV("OLDADDRCODEID", previousAddress);
        robot.addKV("ADDRESSCODEID", currentAddress);
        //newData.put("Modified_date", new Timestamp(time));

        int xPosition = curAddr.getxPosition();
        int yPosition = curAddr.getyPosition();
        robot.addKV("xPosition", xPosition);
        robot.addKV("yPosition", yPosition);

        //判断小车是否到了调度单终点
        logger.debug("是否已经到达调度单终点？ isFinishOrder(robot):" + isFinishOrder(robot));
        if (isFinishOrder(robot)) {
            this.onFinishOrder(robot);
        }
    }

    /**
     * 判断条件调度单组的任务是否完成的标记是 已经到达终点(非工作台) 如果终点是工作台 得人工finish
     *
     * @param robot
     */
    private void onFinishOrder(Robot robot) {
        //TODO 到达终点的充电任务
        if (!onWorkStationStopPoint(robot) && !onCharging(robot)) {
            logger.info("Robot:" + robot.getRobotId() + "完成当前任务 OrderId:"
                    + robot.getCurOrder().getOrderId() + " 自动执行下一个......");
            Order curOrder = robot.getCurOrder();
            /*//更新不及时 && 替换小车
            if (curOrder instanceof StationPodOrder) {
                StationPodOrder stationPodOrder = (StationPodOrder) curOrder;
                robot.setOrderIndex(stationPodOrder.getIndex());
            }*/
            robot.setAvaliable(true);
            robot.setStatus(Robot.IDLE);
            robot.setCurOrder(null);//当前任务置空
            //调整下顺序
            try {
                checkIdle(robot);
            } catch (Throwable e) {
                e.printStackTrace();
                //恢复状态等下一次
                robot.setAvaliable(true);
                robot.setStatus(Robot.IDLE);
                robot.setCurOrder(null);//当前任务置空
                logger.error("检查新任务发生异常:" + robot.getId() + " 当前任务是:" + curOrder.getOrderId(), e);
            }
            this.orderManager.finishOrder(curOrder, robot);
        } else {
            logger.debug("到工作站或充电桩的小车:" + robot.getRobotId() + "需要人工或程序触发结束操作,等待获取人工操作指令....robot.getStatus:"
                    + robot.getStatus() + " robot.isAvaliable:" + robot.isAvaliable());
        }
    }

    private boolean onCharging(Robot robot) {
        Order order = robot.getCurOrder();
        if (order instanceof ChargerDriveOrder) {
            logger.debug("小车robot:" + robot.getRobotId() + " 到达充电位置:" + robot.getAddressId());
            return true;
        }
        return false;
    }

    private boolean onWorkStationStopPoint(Robot robot) {
        Order order = robot.getCurOrder();
        if (order instanceof StationPodOrder) {
            StationPodOrder stationPodOrder = (StationPodOrder) order;
            return Objects.equals(stationPodOrder.getWorkStation().getStopPoint(), robot.getAddressId());
        }
        return false;
    }

    /**
     * 判断条件调度单的任务是否完成的标记是 已经到达调度单终点
     *
     * @param robot
     * @return
     */
    private boolean isFinishOrder(Robot robot) {
        Order order = robot.getCurOrder();
        if (order == null) {
            return false;
        }
        return order.isFinish();
    }


    /*
         请求名称	String	 name
         sectionID	Long	 sectionID
         时间戳	    Long	 requestTime*/

    /**
     * 小车地图请求
     *
     * @param data
     */
    public void ON_RCS_WCS_MAP_REQUEST(Map data) {
        if (!flag) {
            return;
        }
        logger.debug("小车地图请求RCS_WCS_MAP_REQUEST:" + data);

        String name = (String) data.get("name");
        Long sectionId = CommonUtils.parseLong("sectionID", data);//地图ID mapid
        Long requestTime = CommonUtils.parseLong("requestTime", data);
        /*
        请求名称	    String	name
        sectionID	Long	sectionID
        时间戳	    Long	requestTime
        地图行数	    Int	row
        地图列数	    Int	column
        不可用Cell集合	List<long>	unWalkedCell
        旋转区, 与工作站一一对应	Map<Integer, List<List<Long>>>	stationAndTurnArea
        跟车直行点，与工作站一一对应，每个工作站一个cell，此处关闭雷达	List<long>	followCell
        * */
        Section section = this.wareHouseManager.getSectionByRcsSectionId(sectionId);
        List<Integer> rowAndColumnList = this.mapManager.MapRowAndColumn(sectionId);
        int mapId = rowAndColumnList.get(2);

        List<Long> unwalkableList = mapManager.unwalkablePoint(section.getSection_id());
        Map<String, List<Long>> turnAreaMap = mapManager.MapTurnArea(sectionId);
        Map workStationMap = mapManager.getWorkStationMap();
        Iterator iter = section.workStationMap.values().iterator();
        Map res = new HashMap();
        Map WS_TOWARD = new HashMap();
        while (iter.hasNext()) {
            WorkStation next = (WorkStation) iter.next();
            WS_TOWARD.put(next.getStopPoint(), Integer.valueOf(next.getFace()));
        }
        res.put("WS_TOWARD", WS_TOWARD);
        logger.debug("----------------------响应主题：WCS_RCS_MAP_RESPONSE-----------------");


        if (rowAndColumnList.size() != 0) {
            res.put("row", rowAndColumnList.get(0));
            res.put("column", rowAndColumnList.get(1));
        } else {
            res.put("row", 0);
            res.put("column", 0);
        }
        res.put("unWalkedCell", unwalkableList);
        res.put("stationAndTurnArea", turnAreaMap);
        res.put("workStationMap", workStationMap);

        res.put("name", name);
        res.put("sectionID", sectionId);
        res.put("sectionUUID", section.getSection_id());
        res.put("requestTime", requestTime);
        //跟车自行点 目前就设置到工作站的stoppoint
        //Section section = this.wareHouseManager.getSectionByRcsSectionId(sectionId);
        List<Long> followCell = this.mapManager.queryFollowCells(section);
        res.put("followCell", followCell);

        //多个充电桩 List<Map> Map{chargerID,addrCodeID,chargerType,toward} chargers
        //chargerID int型 chargerType int型 addrCodeID long型 toward int型
        List<Map> chargers = this.queryChargers(section);
        res.put("chargers", chargers);
        List<Map> pods = this.podManager.getPods4Rcs(section.getSection_id());
        res.put("pods", pods);
        List<Map> unStoragePods = this.podManager.getUnStoragePods4Rcs(section.getSection_id());
        res.put("unStoragePods", unStoragePods);
        Map podDirectMap = this.podManager.getPodsDirect(section.getSection_id());
        res.put("podsDirect", podDirectMap);
        //获取不需要触发锁格超时的点
        List<Long> ignoreToLockAddrList = getIgnoreToLockAddressList(section.getWareHouse_id(), section.getSection_id());
        res.put("unlockedCellTimeout", ignoreToLockAddrList);
        //获取存储区地址
        List<Long> storageAddrList = getStorageAddressList(section.getWareHouse_id(), section.getSection_id());
        res.put("storageAddrList", storageAddrList);
        //获取预先放置pod的地址
        List<Long> manualLayingDownPodAddrList = new ArrayList<Long>();
        String sanPodsAddrList = this.systemPropertiesManager.getProperty("rcsScanPod2StorageAddrList", section.getWareHouse_id());
        if (CommonUtils.isEmpty(sanPodsAddrList)) {/*this.rcsScanPod2StorageAddrList*/
            logger.debug("获取预先放置pod的地址为空！");
        } else {
            logger.debug("获取预先放置pod的地址：" + sanPodsAddrList);/*this.rcsScanPod2StorageAddrList*/
            String[] addrs = sanPodsAddrList.split(",");/*this.rcsScanPod2StorageAddrList*/
            for (int i = 0; i < addrs.length; i++) {
                Long addr = Long.parseLong(addrs[i]);
                if (this.wareHouseManager.getAddressByAddressCodeId(addr, section) != null) {
                    logger.debug("添加预先放置pod的地址合法，addr=" + addr);
                    manualLayingDownPodAddrList.add(addr);
                } else {
                    logger.debug("添加预先放置pod的地址不合法，addr=" + addr);
                }
            }
        }
        res.put("manualLayingDownPodAddrList", manualLayingDownPodAddrList);
        this.messageSender.WCS_RCS_MAP_RESPONSE(res);
        logger.debug("地图请求回复成功!");
    }

    private List<Long> getIgnoreToLockAddressList(String wareHouseId, String sectionId) {
        logger.debug("准备获取不需要触发锁格超时的地址，sectionId：" + sectionId);
        //type=5:turn   type=8:line
        List<Long> reList = new ArrayList<Long>();
        String sql = "SELECT a.* FROM WD_NODE a LEFT JOIN WD_MAP b ON a.`MAP_ID`=b.`ID` " +
                "LEFT JOIN WD_SECTION c ON b.`SECTION_ID`=c.`ID` WHERE b.`ACTIVE`=TRUE " +
                "AND c.`WAREHOUSE_ID`=? AND c.`ID`=? AND a.`TYPE` IN(5,8);";
        List queryParams = new ArrayList();
        queryParams.add(wareHouseId);
        queryParams.add(sectionId);
        List<Map> rows = this.jdbcRepository.queryBySql(sql.toUpperCase(), queryParams);
        if (rows != null && rows.size() != 0) {
            for (int i = 0; i < rows.size(); i++) {
                Map map = rows.get(i);
                Long addressCodeId = CommonUtils.parseLong("ADDRESSCODEID", map);
                reList.add(addressCodeId);
            }
            logger.debug("sectionId:" + sectionId + " 找到不需要锁格超时的地址：" + JsonUtils.list2Json(reList));
            return reList;
        }
        logger.debug("sectionId:" + sectionId + " 未找到不需要锁格超时的地址");
        return null;
    }

    private List<Long> getStorageAddressList(String wareHouseId, String sectionId) {
        logger.debug("准备获取存储区地址，sectionId：" + sectionId);
        //type=1:storage
        List<Long> reList = new ArrayList<Long>();
        String sql = "SELECT a.* FROM WD_NODE a LEFT JOIN WD_MAP b ON a.`MAP_ID`=b.`ID` " +
                "LEFT JOIN WD_SECTION c ON b.`SECTION_ID`=c.`ID` WHERE b.`ACTIVE`=TRUE " +
                "AND c.`WAREHOUSE_ID`=? AND c.`ID`=? AND a.`TYPE` = '1';";
        List queryParams = new ArrayList();
        queryParams.add(wareHouseId);
        queryParams.add(sectionId);
        List<Map> rows = this.jdbcRepository.queryBySql(sql.toUpperCase(), queryParams);
        if (rows != null && rows.size() != 0) {
            for (int i = 0; i < rows.size(); i++) {
                Map map = rows.get(i);
                Long addressCodeId = CommonUtils.parseLong("ADDRESSCODEID", map);
                reList.add(addressCodeId);
            }
            logger.debug("sectionId:" + sectionId + " 查询到所有存储位的地址：" + JsonUtils.list2Json(reList));
            return reList;
        }
        logger.debug("sectionId:" + sectionId + " 没有设置存储位置的地址");
        return null;
    }

    private List<Map> queryChargers(Section section) {
        List<Charger> rows = new ArrayList<>(section.chargers.values());
        List<Map> datas = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Charger row = rows.get(i);
            Map charger = new HashMap();
            charger.put("addrCodeID", row.getAddressCodeId());
            charger.put("chargerID", row.getRcsChargerId());
            charger.put("chargerType", row.getChargerType());
            charger.put("toward", row.getDirect());
            charger.put("UUID", row.getId());
            datas.add(charger);
        }
        return datas;
    }


    /**
     * 机器ID	       Long	robotID
     * sectionID	   Long	sectionID
     * AGV旧状态码	   int	oldStatus
     * AGV新状态码 	int	newStatus
     * 时间戳	        Long	time
     * <p>
     * 状态包目前只用于充电，其他状态没有用处
     *
     * @param data
     */
    public void ON_RCS_WCS_AGV_STATUS(Map data) {
        logger.debug("处理RCS_WCS_AGV_STATUS事件....");
        long robotID = CommonUtils.parseLong("robotID", data);
        long rcs_sectionID = CommonUtils.parseLong("sectionID", data);
        int oldStatus = CommonUtils.parseInteger("oldStatus", data);
        int newStatus = CommonUtils.parseInteger("newStatus", data);

        if (failCheckRobot(robotID, rcs_sectionID)) return;

        Robot robot = this.getRobotById(robotID);

        this.onStatusChange(oldStatus, newStatus, robot);

        logger.debug("RCS_WCS_AGV_STATUS事件处理完毕!");
    }

    /**
     * 集中处理状态变化
     *
     * @param robot
     */
    private void onStatusChange(int oldStatus, int newStatus, Robot robot) {
        long currentTime = System.currentTimeMillis();//以系统当前时间计算 不能以小车发过来的时间戳算
        if (Robot.CHARGING == newStatus && Robot.CHARGING != oldStatus && Robot.HB_RT_TIMEOUT != oldStatus) {
            logger.debug("满足开始充电状态变化的小车信息" + robot.toString());
            if (robot.getCurOrder() == null || !Objects.equals(robot.getCurOrder().getType(), TripType.CHARGER_DRIVE)) {
                logger.debug("更新小车为充电状态时，小车调度单为空或小车当前调度单不是充电类型！");
                return;
            }
            if (isNeedFullCharging(robot, currentTime)) {
                robot.setFullChargeFlag(true);
                robot.setFullChargeTime(currentTime);
                robot.addKV("FULLCHARGETIME", new Timestamp(currentTime));
                logger.debug("小车" + robot.getRobotId() + "满足满充条件");
            } else {
                robot.setFullChargeFlag(false);
                logger.debug("小车" + robot.getRobotId() + "不满足满充条件");
            }
            //开始充电，更新WCS_ROBOT表的ChargingTime
            logger.debug("Robot :" + robot.getRobotId() + "开始充电,电量是:" + robot.getLaveBattery() + ", 电压是：" + robot.getVoltage());
            robot.setChargingTime(currentTime);
            robot.setStartLaveBattery(robot.getLaveBattery());
            robot.setIsChargingRetry(0);
            robot.setStatus(Robot.CHARGING);
            robot.setChargeNum(robot.getChargeNum() + 1);
            robot.addKV("CHARGINGTIME", new Timestamp(currentTime));
            robot.addKV("CHARGENUM", robot.getChargeNum());
            this.jdbcRepository.updateBusinessObject(robot);
        } else {
            logger.debug("不满足开始充电的条件，小车：" + robot.getRobotId() + ", newStatus:" + newStatus + ", oldStatus:" + oldStatus);
        }
    }

    /**
     * 是否需要满充
     *
     * @return
     */
    public boolean isNeedFullCharging(Robot robot, Long currentTime) {
        logger.debug("校验小车：" + robot.getRobotId() + " 是否满足满充条件");
        String RobotFullChargingDays = systemPropertiesManager.getProperty("RobotFullChargingDays", robot.getWareHouseId());
        if (RobotFullChargingDays == null || "".equals(RobotFullChargingDays)) {
            RobotFullChargingDays = "7";//默认满充间隔天数
        }
        String RobotNormalChargingNum = systemPropertiesManager.getProperty("RobotNormalChargingNum", robot.getWareHouseId());
        if (RobotNormalChargingNum == null || "".equals(RobotNormalChargingNum)) {
            RobotNormalChargingNum = "50";//默认满充间隔次数
        }
        logger.debug("系统设置满充间隔天数：" + RobotFullChargingDays + ", 满充间隔次数：" + RobotNormalChargingNum);
        if ((currentTime - robot.getFullChargeTime()) >= Long.parseLong(RobotFullChargingDays) * 24 * 60 * 60 * 1000
                || robot.getChargeNum() % (Integer.parseInt(RobotNormalChargingNum) + 2) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 主要是电量事件有用
     * <p>
     * 机器ID	    Long	robotID
     * sectionID	Long	sectionID
     * IP	        String	ip
     * 状态时间-时间戳	Long	statusTime
     * 剩余电量	Int	laveBattery
     * 发动机温度	Long	motorTemperature
     *
     * @param data
     */
    public void ON_RCS_WCS_ROBOT_STATUS(Map data) {
        long robotID = CommonUtils.parseLong("robotID", data);
        logger.debug("处理小车" + robotID + "RCS_WCS_ROBOT_STATUS事件...." + data);
        long sectionID = CommonUtils.parseLong("sectionID", data);

        if (failCheckRobot(robotID, sectionID)) return;
        Robot robot = this.getRobotById(robotID);
        if (robot.getStatus() == Robot.NOTCHECKED) {
            return;//未登录
        }
        /*Long time = CommonUtils.parseLong("statusTime",data);
        if(time<robot.getLastMsgTime()){
            logger.error("收到过期消息,不消费:"+data);
            return;
        }
        robot.setLastMsgTime(time);*/
        //String ip = CommonUtils.parseString("ip", data);//IP允许发生改变
        int laveBattery = CommonUtils.parseInteger("laveBattery", data) / 10;
        int voltage = CommonUtils.parseInteger("voltage", data);
        logger.info("当前小车 " + robotID + " 电量是:" + laveBattery + ", 电压是：" + voltage);
        //long motorTemperature = CommonUtils.parseLong("motorTemperature", data);
        //long time = CommonUtils.parseLong("statusTime", data);

        robot.setLaveBattery(laveBattery);
        robot.setVoltage(voltage);


        checkIfAvaliableOnCharging(robot, System.currentTimeMillis());
        //这里可能是唯一修改robot的地方，之前各种信息会记录
        this.jdbcRepository.updateBusinessObject(robot);

        logger.debug("RCS_WCS_ROBOT_STATUS事件处理完毕!");
    }

    /**
     * 机器ID	Long	robotID
     * sectionID	Long	sectionID
     * 心跳时间-时间戳	Long	heartBeatTime
     * 上一个地址码	Long	addressCode
     *
     * @param data
     */
    public void ON_RCS_WCS_ROBOT_HEART_BEAT(Map data) {
        logger.debug("处理心跳包RCS_WCS_ROBOT_HEART_BEAT...");
        long robotID = CommonUtils.parseLong("robotID", data);
        long sectionID = CommonUtils.parseLong("sectionID", data);
        //long time = CommonUtils.parseLong("heartBeatTime", data);

        //如果没有注册 不能执行操作
        if (failCheckRobot(robotID, sectionID)) return;

        Robot robot = this.getRobotById(robotID);
        Long time = CommonUtils.parseLong("statusTime", data);
        if (time < robot.getLastMsgTime()) {
            logger.error("收到过期消息,不消费:" + data);
            return;
        }
        robot.setLastMsgTime(time);

        if (robot.getStatus() == Robot.HB_RT_TIMEOUT) {
            int oldStatus = robot.getOldStatus();
            robot.setOldStatus(robot.getStatus());
            robot.setStatus(oldStatus);//取回上一个状态
            this.jdbcRepository.updateBusinessObject(robot);
            //logger.debug("处理心跳包RCS_WCS_ROBOT_HEART_BEAT结束!"+robot);
            //return;//后面的不处理了
        }

        logger.debug("处理心跳包RCS_WCS_ROBOT_HEART_BEAT结束!");
    }

    /**
     * 给空闲分配新任务 周期性或者完成时
     *
     * @param robot
     * @return
     */
    private Order getRobotNewOrder(Robot robot) {
        //String last = robot.getLastOrderId();
        Order nextOrder = this.orderManager.getOrderByRobot(robot);
        /*if(nextOrder == null || !Objects.equals(last,nextOrder.getOrderId())){
            //没有新任务或者获取了新任务（不同调度单的任务）
            //有时候会出现任务已完成 但实际POD并未返回存储区的情况
            if(podOnRobot(robot)){
                //这个时候是不能分配新任务的
                //robot.setAvaliable(false);
                PodRunOrder newOrder = createRunningPodRunOrder(robot);
                return newOrder;
            }
        }*/
        return nextOrder;
    }

    private PodRunOrder createRunningPodRunOrder(Robot robot) {
        robot.setStatus(Robot.WORKING);
        PodRunOrder newOrder = new PodRunOrder();
        CommonUtils.genUselessInfo(newOrder.getKv());
        newOrder.setOrderStatus(TripStatus.AVAILABLE);
        newOrder.setOrderId(CommonUtils.genUUID());
        newOrder.setPod(robot.getPod());
        newOrder.setWareHouseId(robot.getWareHouseId());
        newOrder.setSectionId(robot.getSectionId() + "");
        newOrder.setRobot(robot);
        robot.setCurOrder(newOrder);
        robot.setLastOrderId(newOrder.getOrderId());
        this.jdbcRepository.insertBusinessObject(newOrder);
        //this.resendOrder(robot.getSectionId(),robot.getRobotId()+"","false");
        logger.error("为小车" + robot.getRobotId() + " Pod" + robot.getPod().getPodName() + "分配返回货架到存储区的任务");
        return newOrder;
    }


    /**
     * 机器ID	Long	robotID
     * sectionID	Long	sectionID
     * IP	String	ip
     * 错误时间-时间戳	Long	errorTime
     * 错误ID	Int	errorID
     * 错误状态信息	Short	errorStatus
     *
     * @param data
     */
    public void ON_RCS_WCS_ROBOT_ERROR(Map data) {
        logger.debug("处理故障RCS_WCS_ROBOT_ERROR...");
        long robotID = CommonUtils.parseLong("robotID", data);
        long sectionID = CommonUtils.parseLong("sectionID", data);
        String ip = (String) data.get("ip");
        long time = CommonUtils.parseLong("errorTime", data);
        int errorID = CommonUtils.parseInteger("errorID", data);
        int errorStatus = CommonUtils.parseInteger("errorStatus", data);

        if (failCheckRobot(robotID, sectionID)) return;

        //只处理POD扫码错误


        logger.debug("处理故障RCS_WCS_ROBOT_ERROR结束");
    }

    private boolean failCheckRobot(long robotID, long sectionID) {
        //如果没有注册 不能执行操作
        if (!isRegisted(robotID)) {
            logger.error("小车没有注册，需要重新登录验证! robotID" + robotID);
            return true;
        }
        return false;
    }

    /**
     * 机器ID	Long	robotID
     * sectionID	Long	sectionID
     * 时间戳	Long	time
     * <p>
     * 链接到RCS
     *
     * @param data
     */
    public void ON_RCS_WCS_ROBOT_CONNECT_RCS(Map data) {
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS...");
        long robotID = CommonUtils.parseLong("robotID", data);
        Robot robot = this.registRobots.get(robotID);
        basicMsgConsume(data, robot.getOldStatus());//重连恢复原先状态？
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS END!");
    }

    public void ON_RCS_WCS_ROBOT_CLOSE_CONNECTION(Map data) {
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS...");
        basicMsgConsume(data, Robot.OFFLINE);
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS END!");
    }

    public void ON_RCS_WCS_AGV_NOMOVE_TIMMEOUT(Map data) {
        logger.debug("ON_RCS_WCS_AGV_NOMOVE_TIMMEOUT...");
        Long robotId = CommonUtils.parseLong("robotID", data);
        Long rcs_sectionId = CommonUtils.parseLong("sectionID", data);
        if (failCheckRobot(robotId, rcs_sectionId)) return;
        Section section = this.wareHouseManager.getSectionByRcsSectionId(rcs_sectionId);
        List lockedAddressList = (List) data.get("lockedCellList");
        /*String unAvailable = this.systemPropertiesManager.getProperty("Cost_Unavailable", section.getWareHouse_id());
        Long cost = null;
        if (!CommonUtils.isEmpty(unAvailable)) {
            cost = Long.parseLong(unAvailable);
        } else {
            cost = 1000L;
        }*/
        Long cost = 100L;
        logger.debug("处理位置不改变超时，小车：" + robotId + ",收到RCS地址：" + JsonUtils.list2Json(lockedAddressList));
        if (lockedAddressList != null && lockedAddressList.size() > 0) {
            String addrList = "";
            for (int i = 0; i < lockedAddressList.size(); i++) {
                String s = lockedAddressList.get(i) + "";
                Address address = this.wareHouseManager.getAddressByAddressCodeId(Long.parseLong(s), section);
                if (address == null) {
                    logger.debug("没有找到该地址：" + s);
                    continue;
                }
                addrList += "".equals(addrList) ? s : "," + s;
            }
            logger.debug("处理位置不改变超时，小车：" + robotId + ",将地址：" + JsonUtils.list2Json(lockedAddressList) + " 的COST值更新成 " + cost);
            if (!"".equals(addrList)) {
                this.webApiBusiness.updateNewCost(section.getWareHouse_id(), section.getSection_id(), addrList, cost + "");
                String[] arrs = addrList.split(",");
                for (int i = 0; i < arrs.length; i++) {
                    String arr = arrs[i];
                    newCostAddressMap.put(arr + "," + rcs_sectionId, System.currentTimeMillis());
                }
            }
            logger.debug("位置不改变超时，已经被增大COST值的地址有：" + JsonUtils.map2Json(newCostAddressMap));
        }
        logger.debug("ON_RCS_WCS_AGV_NOMOVE_TIMMEOUT END!");
    }

    public void ON_RCS_WCS_AGV_HEART_RT_TIMEOUT(Map data) {
        logger.debug("ON_RCS_WCS_AGV_HEART_RT_TIMEOUT...");
        basicMsgConsume(data, Robot.HB_RT_TIMEOUT);
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS END!");
    }

    public void ON_AGV_REPEAT_CONNECT2_RCS_POSITION_ERROR(Map data) {
        logger.debug("ON_AGV_REPEAT_CONNECT2_RCS_POSITION_ERROR...");
        basicMsgConsume(data, Robot.RECONNECTERROR);
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS END!");
    }

    public void ON_RCS_WCS_AGV_PARKING_RESPONSE(Map data) {
        logger.debug("ON_RCS_WCS_AGV_PARKING_RESPONSE...");
        basicMsgConsume(data, Robot.STOP2NEAREST);
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS END!");
    }

    public void ON_RCS_WCS_URGENT_STOP_RESPONSE(Map data) {
        logger.debug("ON_RCS_WCS_URGENT_STOP_RESPONSE...");
        basicMsgConsume(data, Robot.URGENSTOP);
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS END!");
    }

    private void basicMsgConsume(Map data, int robotStatus) {
        long robotID = CommonUtils.parseLong("robotID", data);
        long rcs_sectionId = CommonUtils.parseLong("sectionID", data);
        long time = CommonUtils.parseLong("time", data);

        if (failCheckRobot(robotID, rcs_sectionId)) return;

        Robot robot = this.getRobotById(robotID);
        robot.setOldStatus(robot.getStatus());//保留旧状态
        robot.setStatus(robotStatus);
        this.jdbcRepository.updateBusinessObject(robot);
        //如果车的状态不是IDLE或者WORKING 取消它的订单
        if (robotStatus != Robot.IDLE || robotStatus != Robot.WORKING) {
            robot.setAvaliable(false);
            this.cancelOrder(robot);
            //this.onMapChanged(robot.getAddressId(), robot.getSectionId(), true);
        }
        robot.clearKV();//消除状态
    }

    //取消小车当前任务与下一个任务
    private void cancelOrder(Robot robot) {
        Order order = robot.getCurOrder();
        if (order != null) {
            this.orderManager.cancelRobotOrders(robot, order);
        }
        robot.setCurOrder(null);
    }

    /**
     * 地图改变事件发起
     *
     * @param addressId
     * @param sectionId
     * @param blocked
     */
    private void onMapChanged(String addressId, String sectionId, boolean blocked) {
        Address address = this.wareHouseManager.getAddressByAddressCodeId(addressId, sectionId);
        /*Map<String,Object> data = new HashMap<>();
        data.put("addressCodeId",addressId);
        data.put("sectionID",sectionId);*/

        if (blocked) {
            address.setNodeState(AddressStatus.UNAVAILABLE);
            //data.put("blocked",3);
        } else {
            address.setNodeState(AddressStatus.AVALIABLE);
            //data.put("blocked",0);
        }
        /*ObjectMapper mapper = new ObjectMapper();
        String jsonData = null;
        try {
            jsonData = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
        StringBuilder stringBuilder = new StringBuilder();

        long rcs_sectionId = this.wareHouseManager.getRcsSectionIdBySectionId(sectionId);

        stringBuilder.append(rcs_sectionId).append("&&").append(addressId).append("&&").append(blocked ? 1 : 0);
        //1&&1975&&1
        this.messageSender.ON_WCS_MAP_CHANGED(stringBuilder.toString());
    }

    public void ON_RCS_WCS_ALL_MOTOR_CUT_RESPONSE(Map data) {
        logger.debug("ON_RCS_WCS_ALL_MOTOR_CUT_RESPONSE...");
        basicMsgConsume(data, Robot.ALLMOTORCUTDOWN);
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS END!");
    }

    public void ON_RCS_WCS_START_SLEEP_RESPONSE(Map data) {
        logger.debug("ON_RCS_WCS_START_SLEEP_RESPONSE...");
        basicMsgConsume(data, Robot.SLEEPING);
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS END!");
    }

    public void ON_RCS_WCS_STOP_SLEEP_RESPONSE(Map data) {
        logger.debug("ON_RCS_WCS_STOP_SLEEP_RESPONSE...");
        basicMsgConsume(data, Robot.IDLE);//睡醒了就应该干活
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS END!");
    }

    public void ON_RCS_WCS_CLEAR_PATH_RESPONSE(Map data) {
        logger.debug("ON_RCS_WCS_CLEAR_PATH_RESPONSE...");
        basicMsgConsume(data, Robot.CLEARRCSCACHEDPATH);
        logger.debug("ON_RCS_WCS_ROBOT_CONNECT_RCS END!");
    }

    public Robot getRegistedRobot(long robotId) {
        return this.registRobots.get(robotId);
    }

    public void sendMessage2WorkStation(String str) {
        logger.debug("往工作站推送消息:" + str);
        this.messageSender.sendWebSocketMsg(str);
    }

    public static Map<Integer, String> status = new HashMap();

    static {
        status.put(Charger.CHARGING, ChargerStatus.CHARGING);
        status.put(Charger.IDLE, ChargerStatus.AVAILABLE);
        status.put(Charger.OFFLINE, ChargerStatus.OFFLINE);
        status.put(Charger.ERROR, ChargerStatus.ERROR);
    }

    /**
     * sectionID:1
     * time:1579987234
     * type:1
     * number:1
     * statusIndex:1, 2, 3, 4
     * statusName:充电, 空闲, 离线, 故障
     *
     * @param data
     */
    public void ON_MAP_CHARGER_BOARD(Map data) {
        logger.debug("ON_MAP_CHARGER_BOARD:处理充电桩信息:" + data);
        long sectionID = CommonUtils.parseLong("sectionID", data);
        long time = CommonUtils.parseLong("time", data);
        int rcsChargerId = CommonUtils.parseInteger("number", data);
        int chargerType = CommonUtils.parseInteger("type", data);

        Integer statusIndex = CommonUtils.parseInteger("statusIndex", data);
        String statusName = CommonUtils.parseString("statusName", data);
        logger.debug("充电桩:" + rcsChargerId + " 的状态是:" + statusName + " 类型是:" + chargerType);
        Section section = this.wareHouseManager.getSectionByRcsSectionId(sectionID);
        if (section == null) {
            return;
        }
        if (section.chargers == null || section.chargers.size() == 0) {
            logger.debug("没有定义充电桩,Section:" + section.getRcs_sectionId());
            return;
        }
        Charger charger = null;
        Iterator<Charger> chargerIterator = section.chargers.values().iterator();
        while (chargerIterator.hasNext()) {
            Charger next = chargerIterator.next();
            if (next.getRcsChargerId() == rcsChargerId) {
                charger = next;
                break;
            }
        }
        if (charger == null) {
            logger.debug("没有这个充电桩: rcsChargerId:" + rcsChargerId);
            return;
        }
        charger.setState(status.get(statusIndex));
        this.jdbcRepository.updateBySql("UPDATE MD_CHARGER SET STATE=? WHERE SECTION_ID=? AND CHARGER_ID=?",
                status.get(statusIndex), section.getSection_id(), rcsChargerId);
        logger.info("修改充电桩状态成功:" + rcsChargerId);

    }

    /*  机器ID	Long	robotID
        sectionID	Long	sectionID
        AGV当前位置	long	addressCodeID
        小车当前的POD   long podCodeID
        货架偏移角	Float	podCodeInfoTheta
        时间戳	Long	time*/

    /**
     * 重新请求路径
     *
     * @param data
     */
    public void ON_RCS_WCS_AGV_REQUEST_PATH(Map data) {
        long robotID = CommonUtils.parseLong("robotID", data);
        logger.debug("ON_RCS_WCS_AGV_REQUEST_PATH:小车" + robotID + "重新请求路径:" + data);
        long sectionID = CommonUtils.parseLong("sectionID", data);
        long currentAddress = CommonUtils.parseLong("addressCodeID", data);

        if (failCheckRobot(robotID, sectionID)) return;
        Robot robot = this.getRobotById(robotID);
        if (robot.getStatus() == Robot.NOTCHECKED) {
            return;//未登录
        }

        long rcsPodId = CommonUtils.parseLong("podCodeID", data);
        Section section = this.wareHouseManager.getSectionByRcsSectionId(sectionID);
        Pod pod = this.podManager.getPodByRcsPodId(rcsPodId, section);
        robot.setPod(pod);//可能是空的
        robot.setAddressId(currentAddress + "");
        Order order = robot.getCurOrder();
        if (order == null) {
            logger.error("当前内存中没有可执行的Order，Robot:" + robot.getRobotId());
            //检查是否有调度单在working状态
            // 根据当前小车的上一个任务编号robot.getLastOrderId(), 找Process的
            order = this.orderManager.getWorkingOrderByRobot(robot);
            if (order == null) {
                logger.error("当前数据库中没有可执行的Order，Robot:" + robot.getRobotId());
                robot.setStatus(Robot.IDLE);
                robot.setAvaliable(Boolean.TRUE);
                robot.addKV("AVAILABLE", true);//
                robot.addKV("STATUS", Robot.IDLE);//TODO 充电时要改成charging？
                this.jdbcRepository.updateBusinessObject(robot);
                logger.debug("小车状态空闲，等待下一个调度单:robotId:" + robot.getRobotId());
                return;
            }
            robot.setCurOrder(order);
        }
        /*//清除路径
        this.ON_WCS_RCS_CLEAR_ALLPATH(robot);
        //执行路径 TODO
        this.rePathOrder(order, robot);*/
        this.autoResendOrder4AGVRequestPath(robot.getSectionId(), robot.getRobotId() + "");


        logger.debug("ON_RCS_WCS_AGV_REQUEST_PATH,小车" + robotID + "路径重新下发成功!");
    }

    /**
     * 触发锁格超时，重新请求路径
     *
     * @param data
     */
    public void ON_LOCKCELL_TIMEOUT_TO_RESEND_PATH(Map data) {
        long robotID = CommonUtils.parseLong("robotID", data);
        logger.debug("ON_LOCKCELL_TIMEOUT_TO_RESEND_PATH:小车" + robotID + "触发锁格超时，重新请求路径:" + data);
        long sectionID = CommonUtils.parseLong("sectionID", data);
        long currentAddress = CommonUtils.parseLong("addressCodeID", data);

        if (failCheckRobot(robotID, sectionID)) return;
        Robot robot = this.getRobotById(robotID);
        if (robot.getStatus() == Robot.NOTCHECKED) {
            return;//未登录
        }

        long rcsPodId = CommonUtils.parseLong("podCodeID", data);
        Section section = this.wareHouseManager.getSectionByRcsSectionId(sectionID);
        Pod pod = this.podManager.getPodByRcsPodId(rcsPodId, section);
        robot.setPod(pod);//可能是空的
        robot.setAddressId(currentAddress + "");
        Order order = robot.getCurOrder();
        if (order == null) {
            logger.error("触发锁格超时，当前内存中没有可执行的Order，Robot:" + robot.getRobotId());
            //检查是否有调度单在working状态
            // 根据当前小车的上一个任务编号robot.getLastOrderId(), 找Process的
            order = this.orderManager.getWorkingOrderByRobot(robot);
            if (order == null) {
                logger.error("触发锁格超时，当前数据库中没有可执行的Order，Robot:" + robot.getRobotId());
                robot.setStatus(Robot.IDLE);
                robot.setAvaliable(Boolean.TRUE);
                robot.addKV("AVAILABLE", true);//
                robot.addKV("STATUS", Robot.IDLE);//TODO 充电时要改成charging？
                this.jdbcRepository.updateBusinessObject(robot);
                logger.debug("触发锁格超时，小车状态空闲，等待下一个调度单:robotId:" + robot.getRobotId());
                return;
            }
            robot.setCurOrder(order);
        }
        this.autoResendOrder4AGVRequestPath(robot.getSectionId(), robot.getRobotId() + "");
        logger.debug("ON_LOCKCELL_TIMEOUT_TO_RESEND_PATH,小车" + robotID + "触发锁格超时，路径重新下发成功!");
    }

    /*=====================================原先RobotService的代码===================================================*/

    /**
     * Order任务完成有三种:
     * 1、到达目的点 2、工作站主动调用(给提供接口) 3、充电时完成当前充电调度单明细
     * <p>
     * 工作台释放POD 直接放走 回热度运算后的目标 返回下一个POD给工位
     * <p>
     * 1、计算热度的目标地址，将目标格子锁定,计算路径
     * 2、获取POD当前的小车
     * 3、获取小车当前的调度单
     * 4、如果当前还剩其他调度单明细，cancel掉，只取最后的返回
     * 5、返回时将小车状态改成可用
     * 6、更新调度单和调度单明细
     * 7、获取下一个调度单
     *
     * @param workStation
     * @param release
     * @return
     */
    public String podRelease(WorkStation workStation, Pod release) {
        logger.debug("Workstation:" + workStation.getWorkStationId() + "释放POD:" + release);
        logger.debug("以工作站为视角释放POD,POD名称无效!" + release);
        Address stop = this.wareHouseManager
                .getAddressByAddressCodeId(workStation.getStopPoint(), workStation.getSectionId());
        /*Pod pod = stop.getPod();
        //search the pod on addr
        //this.podManager.getPodByAddress()
        if (pod == null) {
            return nextPodNameFace(workStation, true);
        }
        Robot robot = pod.getRobot();
        //全场去找这辆车 发生POD信息扫不到时 无法更新在工作站的POD信息
        if (robot == null || !Objects.equals(robot.getAddressId(), workStation.getStopPoint())) {
             robot = stop.getRobot();
        }*/
        Robot robot = stop.getRobot();
        //从工作站pod关联到小车
        if (robot == null) {
            logger.error("状态错误,当前工作站没有车,无法release当前POD，需要重新确认其状态!");
            return nextPodNameFace(workStation, true);
        }
        Order curOrder = robot.getCurOrder();
        logger.debug("当前工作站" + workStation.getStopPoint() + "的小车是:" + robot + " 调度单是:" + curOrder);
        if (curOrder != null) {//保证是当前Pod的调度单
            if (!curOrder.isCanFinish()) {
                logger.error("小车调度单:" + curOrder.getOrderId() + "未执行，不可结束!");
                return nextPodNameFace(workStation, true);
            }
            boolean finished = this.orderManager.finishOrder(curOrder, robot);
            //返回的两个调度单
            if (finished) {
                //同步save到数据库
                this.createBack2Storage(robot);//重复判断,保证能回去存储区
                //修改状态
                robot.setCurOrder(null);
                robot.setStatus(Robot.IDLE);
                robot.setAvaliable(true);
                Long start = System.currentTimeMillis();
                this.checkIdle(robot); //TODO
                logger.error("获取新任务耗时:" + (System.currentTimeMillis() - start) + "毫秒!");
                return nextPodNameFace(workStation, true);
            }
        } else {
            logger.error("小车已经获取新的调度单，可能是重复释放了多次!" + "robot:" + robot);
        }
        return nextPodNameFace(workStation, true);
    }

    //同步save到数据库
    private void createBack2Storage(Robot robot) {
        Order order = robot.getCurOrder();
        if (order == null) {
            return;
        }
        //检查是否存在
        List<Map> rows = this.jdbcRepository.queryByKey("StationPodOrder.checkExistBack2Storage4PodRelease", order.getOrderId(), TripStatus.AVAILABLE);
        if (rows != null && rows.size() == 2) {//TODO
            //已经存在
            return;
        }

        if (rows != null && rows.size() != 2 && rows.size() != 0) {
            this.jdbcRepository.updateByKey("StationPodOrder.deletePositions", order.getOrderId());
        }
        logger.debug("为小车:" + robot.getRobotId() + " 的调度单:" + order.getOrderId() + "生成返回存储区的任务明细");
        //回缓冲区为-1 缓冲区到storage为-2
        Map station2Buffer = new HashMap();
        station2Buffer.put("ID", CommonUtils.genUUID());
        station2Buffer.put("TRIPPOSITION_STATE", OrderPositionStatus.Available.getType());
        station2Buffer.put("TRIP_ID", order.getOrderId());
        station2Buffer.put("POSITION_NO", -1);
        station2Buffer.put("CREATED_DATE", new Timestamp(System.currentTimeMillis()));
        station2Buffer.put("CREATED_BY", "System");
        station2Buffer.put("SECTION_ID", order.getSectionId());
        station2Buffer.put("WAREHOUSE_ID", order.getWareHouseId());
        station2Buffer.put("VERSION", 0);
        this.jdbcRepository.insertRecord("RCS_TRIPPOSITION", station2Buffer);
        station2Buffer.put("POSITION_NO", -2);
        station2Buffer.put("ID", CommonUtils.genUUID());//新生成一个ID
        this.jdbcRepository.insertRecord("RCS_TRIPPOSITION", station2Buffer);
        //保证数据库已经修改成功
        //CommonUtils.sleep(2500L);
    }

    private String nextPodNameFace(WorkStation workStation, boolean next) {
        //获取工作站下一个POD
        logger.debug("获取工作站的面:" + workStation + " 是否是下一个POD" + next);
        Address stop = this.wareHouseManager
                .getAddressByAddressCodeId(workStation.getStopPoint(), workStation.getSectionId());
        Address mid = this.wareHouseManager
                .getAddressByAddressCodeId(workStation.getMidPoint(), workStation.getSectionId());

        Pod pod;
        if (next) {
            //忽略停止点那个 中间点有 取中间点
            pod = mid.getPod();
        } else {
            //取停止点，如果没有取中间点
            pod = stop.getPod() == null ?
                    mid.getPod() : stop.getPod();
        }
        //空pod返回一个空字符串
        if (pod == null) {
            Map data = new HashMap();
            data.put("pod", "");
            data.put("workstation", workStation.getWorkStationId());
            return JsonUtils.map2Json(data);
        }
        logger.debug("队列中的下一个POD是:" + pod);
        String sectionId = pod.getSectionId();
        String podName = pod.getPodName();
        int podFace = pod.getDirect();//0 90 180 270
        int wsFace = workStation.getFace();//工作站朝向
        String sf = CommonUtils.face2WorkStation(podFace, wsFace);

        logger.debug("当前podName:" + pod.getPodName() + "面对工作站的POD面是:" + sf);

        Map data = new HashMap();
        data.put("sectionId", sectionId);
        data.put("pod", podName + sf);
        data.put("workstation", workStation.getWorkStationId());
        return JsonUtils.map2Json(data);
    }


    /**
     * 自动分配和调度小车 无触发的active调度单
     *
     * @param podId
     * @param addressId
     * @param sectionId
     */
    public void autoDrivePod(String podId, String addressId, String sectionId) {
        //podId === uuid
        Long rcsPodId = Long.parseLong(podId);
        Section section = this.wareHouseManager.getSectionById(sectionId);
        Pod pod = this.podManager.getPodByRcsPodId(rcsPodId, section);
        Address address = this.wareHouseManager.getAddressByAddressCodeId(addressId, sectionId);
        //调公用方法
        this.createNewPodRunOrder(pod, address, section);
    }

    /**
     * 指定POD手动调整到目的地 供地图使用
     *
     * @param podId     PodId
     * @param addressId 目的地Id
     * @param sectionId
     * @param active    触发的调度单
     */

    public Order drivePod(String podId, String addressId, String sectionId, Order active) {

        //WareHouseManager wareHouseManager = SpringUtil.getBean(WareHouseManager.class);
        //分两类，一类是空闲的小车，一类是移动的小车
        Pod pod = this.podManager.getPod(sectionId, podId);
        Section section = this.wareHouseManager.getSectionById(sectionId);
        Address address = null;
        if (addressId != null) {
            address = this.wareHouseManager.getAddressByAddressCodeId(addressId, sectionId);
        }
        //产生新调度单
        return this.createNewPodRunOrder(pod, address, section);
    }


    /**
     * 指定POD,指定车 手动调整到目的地
     *
     * @param podId     PodId
     * @param addressId 目的地Id
     * @param active
     */
    public Order drivePodByRobot(Long robotId, String podId, String addressId, Order active) {

        Robot robot = this.getRobotById(robotId);
        //检查小车
        checkRobot(robotId, robot);

        Pod pod = this.podManager.findPod(podId);
        checkPod(podId, pod);

        Address address = this.getAddressById(addressId, robot.getSectionId());
        //检查地址 TODO
        checkAddress(addressId, address);

        //生成调度单
        Order orderGroup = this.createOrder(robot, pod, address, active);
        logger.info("手动任务调度单生成成功!" + orderGroup.getOrderId());
        return orderGroup;
    }

    private void checkPod(String podId, Pod pod) {
        if (pod == null) {
            logger.error("podId:" + podId + "的POD并不存在");
            throw new RuntimeException("podId:" + podId + "的POD并不存在");
        }
        //检查是否被占用
        /*List list = this.podManager.checkOccupied(podId);
        if (list != null && list.size() != 0) {
            throw new RuntimeException("POD被占用!");
        }*/
    }

    /**
     * 将车手动调整到目的地
     *
     * @param robotId   车Id
     * @param addressId 目的地Id
     */

    public void drive(long robotId, String addressId) {
        Robot robot = this.getRobotById(robotId);
        //检查小车
        checkRobot(robotId, robot);

        Address address = this.getAddressById(addressId, robot.getSectionId());
        //TODO 手動調度時不用检查地址 只需要检查目标地址有无小车
        //checkAddress(addressId, address);

        //检查是否在同一个仓库
        if (!address.getSectionId().equals(robot.getSectionId() + "")) {
            throw new RuntimeException("address:" + addressId + " Robot:" + robot + "不在一个仓库或地图里!");
        }

        //生成调度单 手动调车 pod和激活的order都是空
        this.createOrder(robot, null, address, null);
        logger.info("手动任务调度单生成成功!");
    }


    public String releasePod(String sectionId, String workStationId, String podName) {
        Section section = this.wareHouseManager.getSectionById(sectionId);
        if (section == null) {
            return "没有这个section:" + sectionId;
        }
        WorkStation workStation = section.workStationMap.get(workStationId);
        if (workStation == null) {
            return "没有这个workStation:" + workStationId;
        }
        Pod pod = this.podManager.findPodByPodName(section, podName);
        String str = this.podRelease(workStation, pod);
        //发送到QUEUE
        //this.sendMessage2WorkStation(str);
        return str;
    }

    private synchronized void checkAddress(String addressId, Address address) {
        if (address == null) {
            logger.error("addressId:" + addressId + "位置并不存在");
            throw new RuntimeException("addressId:" + addressId + "位置并不存在");
        }
        //检查是否被占用,手动正常来说不会这么傻
        if (!Objects.equals(address.getNodeState(), AddressStatus.AVALIABLE)) {//非0的格子都不能派送
            throw new RuntimeException("地址被占用或不可用");
        }
        address.setNodeState(AddressStatus.RESERVED);
    }

    private void checkRobot(long robotId, Robot robot) {
        //检查车与目的地是否存在
        if (robot == null) {
            logger.error("RobotId:" + robotId + "小车并不存在");
            throw new RuntimeException("RobotId:" + robotId + "小车并不存在");
        }
        if (!robot.isAvaliable()) {
            throw new RuntimeException("小车处于工作、充电或异常状态，无法发送调度单");
        }
    }

    private Order createNewPodRunOrder(Pod pod, Address address, Section section) {
        logger.debug(" POD:" + pod + " ===>目标地址:" + address.getId());
        Order order;
        if (pod != null) {
            order = new PodRunOrder();
            order.setPod(pod);
        } else {
            order = new EmptyRunOrder();
            order.setEndAddr(Long.parseLong(address.getId()));
        }
        order.setOrderStatus(TripStatus.NEW);
        order.setOrderId(CommonUtils.genUUID());
        //PodRun 不产生目的地
        //order.setEndAddr(Long.parseLong(address.getId()));
        order.setWareHouseId(section.getWareHouse_id());
        order.setSectionId(section.getSection_id());

        this.orderManager.saveOrderGroup(order);
        return order;
    }

    private Order createOrder(Robot robot, Pod pod, Address address, Order active) {
        Address src = this.getAddressById(robot.getAddressId(), robot.getSectionId());
        logger.debug("车当前地址是:" + src.getxPosition() + "," + src.getyPosition()
                + " 由调度单触发:" + (active == null ? "无" : active.getOrderId())
                + " ===>目标地址:" + address.getxPosition() + "," + address.getyPosition());
        /*if (pod == null) {
            pod = new Pod();//TODO 用初始化的值即可
        }*/
        Order order = null;
        if (pod != null) {
            order = new PodRunOrder();
            order.setPod(pod);
        } else {
            order = new EmptyRunOrder();
        }
        order.setOrderStatus(TripStatus.AVAILABLE);
        order.setOrderId(CommonUtils.genUUID());
        order.setRobot(robot);
        order.setEndAddr(Long.parseLong(address.getId()));
        order.setWareHouseId(robot.getWareHouseId());
        order.setSectionId(robot.getSectionId() + "");

        if (active != null) {
            order.setActivedBy(order.getOrderId());//由谁激活的
        }

        this.orderManager.saveOrderGroup(order);
        return order;
    }

    private Address getAddressById(String addressId, String sectionId) {
        Section section = this.wareHouseManager.getSectionById(sectionId);
        return section.addressMap.get(addressId);
    }


    /**
     * 更新节点状态与Cost值
     *
     * @param data
     */
    public void ON_WCS_RCS_UPDATE_CELLS(Map data) {
        /*{"availableAddressList":["1","2","3"],"unAvailableAddressList":["4","5","6"],"sectionID":"uuid"}*/
        logger.debug("处理地图格子变化：" + data);
        List<String> availableAddressList = (List<String>) data.get("availableAddressList");
        List<String> blockedAddressList = (List<String>) data.get("unAvailableAddressList");
        String sectionId = (String) data.get("sectionId");//地图发过来的是sectionId
        //this.wareHouseManager.batchUpdateCells(sectionId, availableAddressList, AddressStatus.AVALIABLE);
        //this.wareHouseManager.batchUpdateCells(sectionId, blockedAddressList, AddressStatus.UNAVAILABLE);
        Section section = this.wareHouseManager.getSectionById(sectionId);
        String cost = "";
        String unAvailable = this.systemPropertiesManager.getProperty("Cost_Unavailable_Map", section.getWareHouse_id());
        if (!CommonUtils.isEmpty(unAvailable)) {
            cost = unAvailable;
        } else {
            cost = "1001";
        }
        logger.debug("临时不可走点，准备更新address：" + JsonUtils.list2Json(blockedAddressList) + " 的NewCost:" + cost);
        if (blockedAddressList != null && blockedAddressList.size() > 0) {
            String blockAddrs = "";
            for (int i = 0; i < blockedAddressList.size(); i++) {
                String s = blockedAddressList.get(i) + "";
                blockAddrs += "".equals(blockAddrs) ? s : "," + s;
            }
            this.webApiBusiness.updateNewCost(section.getWareHouse_id(), section.getSection_id(), blockAddrs, cost);
            logger.debug("临时不可走点，更新address：" + blockAddrs + " 的NewCost:" + cost + " 已经成功！");
        }
        logger.debug("临时不可走点，准备还原address：" + JsonUtils.list2Json(availableAddressList) + " 的NewCost为空");
        if (availableAddressList != null && availableAddressList.size() > 0) {
            String availableAddrs = "";
            for (int i = 0; i < availableAddressList.size(); i++) {
                String s = availableAddressList.get(i) + "";
                availableAddrs += "".equals(availableAddrs) ? s : "," + s;
            }
            this.webApiBusiness.updateNewCost(section.getWareHouse_id(), section.getSection_id(), availableAddrs, null);
            logger.debug("临时不可走点，还原address：" + availableAddrs + " 的NewCost为null 已经成功！");
        }
        Map msgMap = new HashMap();
        msgMap.put("availableAddressList", availableAddressList);
        msgMap.put("unAvailableAddressList", blockedAddressList);
        msgMap.put("sectionID", section.getRcs_sectionId());
        msgMap.put("time", System.currentTimeMillis());
        this.messageSender.WCS_RCS_UPDATE_CELLS(msgMap);
        logger.debug("处理地图格子变化完毕!");
    }


    public String callNewPod(String sectionId, String workStationId) {
        logger.debug("workStationId:" + workStationId + "呼叫POD....");
        Section section = this.wareHouseManager.getSectionById(sectionId);
        WorkStation workStation = section.workStationMap.get(workStationId);
        //this.podRelease(workStation,null,forceRelease);
        if (workStation == null) {
            return "没有这个工作站:" + workStationId + "\n  " + section.workStationMap;
        }
        return nextPodNameFace(workStation, false);
    }

    /*
    long sectionID
    long podCodeID
    long addressCodeID
    long time
    货架偏移角	Float	podCodeInfoTheta
    * */
    public void ON_RCS_WCS_SCAN_POD(Map data) {
        long rcsSectionID = CommonUtils.parseLong("sectionID", data);
        long podCodeID = CommonUtils.parseLong("podCodeID", data);
        long addressCodeID = CommonUtils.parseLong("addressCodeID", data);
        //long time = CommonUtils.parseLong("time", data);
        int podCodeInfoTheta = CommonUtils.parseInteger("podCodeInfoTheta", data);

        Section section = this.wareHouseManager.getSectionByRcsSectionId(rcsSectionID);
        String sectionID = section.getSection_id();
        logger.debug("ON_RCS_WCS_SCAN_POD,扫描出来的POD信息是 podCodeID：" + podCodeID
                + " addressCodeID:" + addressCodeID + " sectionID" + sectionID);
        //String podName = this.podManager.getPodNameByRcsPodId(podCodeID);
        //持久化
        Address address = this.wareHouseManager.getAddressByAddressCodeId(addressCodeID, section);
        if (address == null) {
            logger.error("地图上没有标识该地址:" + addressCodeID);
            return;
        }
        Pod pod = this.podManager.getPodByRcsPodId(podCodeID, section);
        if (pod == null) {
            if (address.getType() == AddressType.STORAGE) {
                logger.debug("存储区没有扫到POD,地址码:" + addressCodeID);
            } else {
                logger.error("没有扫到POD,地址码:" + addressCodeID);
            }
            return;
        }
        pod.setAddress(address);
        pod.addKV("PLACEMARK", addressCodeID).addKV("TOWARD", podCodeInfoTheta);
        pod.addKV("XPOS", address.getxPosition()).addKV("YPOS", address.getyPosition());
        this.jdbcRepository.updateBusinessObject(pod);
        //logger.debug("更新POD podCodeID：" + podCodeID + " addressCodeID:" + addressCodeID + "成功!");
    }

    /*
    long time
    long sectionID
    long podCodeID  //要求的PODID
    long scanedPodID  //扫描的PODID
    long addressCodeID  //当前的地址码
    long robotID //当前小车
    * */
    public void ON_RCS_WCS_POD_ERRORPLACE(Map data) {
        long rcsSectionID = CommonUtils.parseLong("sectionID", data);
        long podCodeID = CommonUtils.parseLong("podCodeID", data);
        long scanedPodID = CommonUtils.parseLong("scanedPodID", data);
        long addressCodeID = CommonUtils.parseLong("addressCodeID", data);
        long time = CommonUtils.parseLong("time", data);
        long robotID = CommonUtils.parseLong("robotID", data);

        logger.debug("ON_RCS_WCS_POD_ERRORPLACE:" + data);
        if (failCheckRobot(robotID, rcsSectionID)) return;
        Robot robot = this.getRobotById(robotID);
        if (robot.getStatus() == Robot.NOTCHECKED) {
            logger.error("小车未登录 robotID :" + robotID);
            return;//未登录
        }

        //1、取消小车当前调度单及明细 更新小车状态及可用性
        if (robot.getCurOrder() != null) {
            logger.debug("扫描POD错误, 小车重发路径:" + robot.getRobotId());
            this.resendOrder(robot.getSectionId(), robot.getRobotId() + "", "");
            //Order order = robot.getCurOrder();
            //this.orderManager.cancelRobotOrders(robot, order);
            //清除下发路径
            //this.ON_WCS_RCS_CLEAR_ALLPATH(robot);
            /*Order order = robot.getCurOrder();
            this.orderManager.resetOrder(order);
            this.jdbcRepository.updateBusinessObject(robot);*/
            //CommonUtils.sleep(2000L);
            /*try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            /*robot.setAvaliable(Boolean.TRUE);
            robot.addKV("AVAILABLE", Boolean.TRUE);
            robot.setStatus(Robot.IDLE);
            robot.setCurOrder(null);*/
        }

        Section section = this.wareHouseManager.getSectionByRcsSectionId(rcsSectionID);
        //2、本身应该在那的那个POD状态设置成Reserved 不能再被使用了 其实本来是RESERVED
        Pod favorPod = this.podManager.getPodByRcsPodId(podCodeID, section);
        favorPod.addKV("STATE", PodStatus.RESERVED);
        this.jdbcRepository.updateBusinessObject(favorPod);

        //3、派遣小车检查扫出来错误POD的地址码上是什么内容
        Pod pod = this.podManager.getPodByRcsPodId(scanedPodID, section);
        if (pod == null) {
            logger.error("小车" + robotID + " 没有扫描到POD! 期望的POD是:" + podCodeID);
            return;
        }
        //获取该POD数据库存储的位置，然后派遣调度单
        String addrCode = pod.getAddress().getId();
        this.checkPodOnPlaceMark(addrCode, section);//生成调度单，派遣小车去该地址码扫描POD
        //将当前地址码赋予该POD
        Address addr = this.getAddressById(addressCodeID + "", section.getSection_id());
        if (addr == null) {
            logger.error("地图上没有标识该地址:" + addressCodeID);
            return;
        }
        pod.setAddress(addr);//当前的地址码应该是存在的
        pod.addKV("PLACEMARK", addressCodeID)
                .addKV("XPOS", addr.getxPosition())
                .addKV("YPOS", addr.getyPosition());
        this.jdbcRepository.updateBusinessObject(pod);
    }

    private void checkPodOnPlaceMark(String addrCode, Section section) {
        //生成POD检查的调度单
        Order order = new EmptyRunOrder();//TODO
        String orderId = CommonUtils.genUUID();
        order.setOrderId(orderId);
        order.setType(TripType.POD_SCAN);
        order.setSectionId(section.getSection_id());
        order.setWareHouseId(section.getWareHouse_id());
        order.setEndAddr(Long.parseLong(addrCode));//目标地址
        order.setOrderStatus(TripStatus.NEW);
        //orderGroup.set
        this.orderManager.saveOrderGroup(order);

    }

    /*
    long robotID
    long sectionID
    long podCodeID
    long addressCodeID
    int agvStatus
    long time
    Short	upStatus
    Short	chargeStatus
    QUEUE: RCS_WCS_QUERYROBOT_RESPONSE
    * */
    public void ON_RCS_WCS_QUERYROBOT_RESPONSE(Map data) {
        logger.debug("查询小车状态回复包:" + data);
        long rcsSectionID = CommonUtils.parseLong("sectionID", data);
        long podCodeID = CommonUtils.parseLong("podCodeID", data);
        long addressCodeID = CommonUtils.parseLong("addressCodeID", data);
        int status = CommonUtils.parseInteger("agvStatus", data);
        short upStatus = CommonUtils.parseShort("upStatus", data);
        short chargeStatus = CommonUtils.parseShort("chargeStatus", data);
        long robotID = CommonUtils.parseLong("robotID", data);

        if (failCheckRobot(robotID, rcsSectionID)) return;
        Robot robot = this.getRobotById(robotID);
        if (robot.getStatus() == Robot.NOTCHECKED) {
            logger.error("小车未登录 robotID :" + robotID);
            return;//未登录
        }

        if (addressCodeID == 0L) {
            logger.error("小车位置不正确，无需执行后续操作! robotID :" + robotID);
            return;
        }
        Section section = this.wareHouseManager.getSectionByRcsSectionId(rcsSectionID);
        Address curAddr = this.wareHouseManager.getAddressByAddressCodeId(addressCodeID, section);
        if (curAddr == null) {
            logger.error("小车位置不正确，无需执行后续操作! robotID :" + robotID + " addressCodeID:" + addressCodeID);
            return;
        }

        //1、调整ROBOT的状态
        robot.setStatus(status);
        if (status == Robot.IDLE) {
            robot.setAvaliable(true);
            robot.addKV("AVAILABLE", Boolean.TRUE);
        }
        if (chargeStatus == 1) {
            robot.setAvaliable(false);
            robot.setStatus(Robot.CHARGING);
            robot.addKV("AVAILABLE", Boolean.FALSE).addKV("STATUS", Robot.CHARGING);
        }

        robot.addKV("STATUS", status).addKV("ADDRESSCODEID", addressCodeID);

        //2、调整车与POD的关系，并且更新POD位置
        if (podCodeID != 0L && upStatus == 1) {//驮了POD
            Pod pod = this.podManager.getPodByRcsPodId(podCodeID, section);
            robot.setPod(pod);
            pod.addKV("PLACEMARK", addressCodeID)
                    .addKV("XPOS", curAddr.getxPosition()).addKV("YPOS", curAddr.getyPosition());
            this.jdbcRepository.updateBusinessObject(pod);
            robot.addKV("POD_ID", podCodeID);
        }

        this.jdbcRepository.updateBusinessObject(robot);
        logger.debug("查询小车状态回复包处理完毕:" + data);
    }

    /**
     * 自动分配车和热度计算的目的地
     *
     * @param podId
     * @param sectionId
     */
    public void autoAssignDrivePod(String podId, String sectionId) {
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        Pod pod = this.podManager.getPodByRcsPodId(Long.parseLong(podId), section);
        //String addrCodeId = this.podManager.computeTargetAddress(pod.getRcsPodId() + "", sectionId);
        //todo 多并发
        //Address address = this.wareHouseManager.getAddressByAddressCodeId(addrCodeId,sectionId);
        //address.setNodeState(AddressStatus.RESERVED);

        this.drivePod(pod.getPodId(), null, sectionId, null);
    }

    public String updatePodStatus(String sectionId, String podId, String addrCodeId, String lockedBy) {
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }

        Pod pod = this.podManager.getPodByRcsPodId(Long.parseLong(podId), section);
        /*Address preAddr = pod.getAddress();
        preAddr.setNodeState(AddressStatus.AVALIABLE);*/
        if (addrCodeId != null) {
            Address address = this.wareHouseManager.getAddressByAddressCodeId(addrCodeId, section);
            if (address == null) {
                return "地址不存在:" + addrCodeId;
            }
            pod.setAddress(address);
            //pod.setLockedBy(Long.parseLong(lockedBy));
            address.setNodeState(AddressStatus.OCCUPIED);
            pod.addKV("PLACEMARK", addrCodeId).addKV("XPOS", address.getxPosition()).addKV("YPOS", address.getyPosition());
            this.jdbcRepository.updateBusinessObject(pod);
        }
        if (lockedBy != null) {
            Long lock = Long.parseLong(lockedBy);
            pod.setLockedBy(lock);
        }

        return pod.toString();
    }

    public String scanPods(String sectionId) {
        Section section = this.wareHouseManager.getSectionById(sectionId);
        //section.
        Collection<Address> addrs = section.addressMap.values();
        Iterator<Address> addrItrator = addrs.iterator();
        Map<Integer, List<Address>> nodes = new LinkedHashMap<>();
        List<NodeList> ndls = new ArrayList<>();
        while (addrItrator.hasNext()) {
            Address next = addrItrator.next();
            if (nodes.get(next.getyPosition()) == null) {
                List<Address> addressList = new ArrayList<>();
                nodes.put(next.getyPosition(), addressList);
                NodeList nodeList = new NodeList();
                nodeList.setX(next.getyPosition());
                nodeList.setAddressList(addressList);
                ndls.add(nodeList);
            }
            List temp = nodes.get(next.getyPosition());
            temp.add(next);
        }

        Collections.sort(ndls, new NodeListComparator());
        boolean revert = true;
        NodeList last = null;
        for (int i = 0; i < ndls.size(); i++) {
            NodeList nodeList = ndls.get(i);
            if (last != null && last.getAddrsList().size() > 0) {
                revert = !last.isRevert();//需要转个方向
            }
            last = nodeList;
            nodeList.setRevert(revert);
            nodeList.sort();
            nodeList.export();
        }
        //分配多辆车 TODO
        //Robot robot = this.getAvailableRobot(section);
        List<Robot> robots = this.getAvailableRobots(section);
        if (robots == null || robots.size() == 0) {
            return "没有可分配的小车";
        }
        for (int i = 0; i < ndls.size(); i++) {
            NodeList nodeList = ndls.get(i);
            Robot robot = robots.get(i % robots.size());
            List<List<Long>> paths = nodeList.getAddrsList();
            for (int j = 0; j < paths.size(); j++) {
                List<Long> longs = paths.get(j);
                String path = JsonUtils.list2Json(longs);
                logger.debug("存储区同向POD地址是:" + path + " 分配的小车是:" + robot.getRobotId());
                String uuid = CommonUtils.genUUID();
                PodScanOrder podScanOrder = new PodScanOrder();
                podScanOrder.addKV("ID", uuid);
                podScanOrder.addKV("DRIVE_ID", robot.getRobotId());
                podScanOrder.addKV("PODSCANPATH", path);
                podScanOrder.setPath(longs);
                podScanOrder.setOrderId(uuid);
                CommonUtils.genUselessInfo(podScanOrder.getNewValue());
                /*TRIP_TYPE TRIP_STATE*/
                podScanOrder.addKV("TRIP_TYPE", TripType.POD_SCAN);
                podScanOrder.addKV("TRIP_STATE", "Available");
                podScanOrder.addKV("SECTION_ID", sectionId);
                podScanOrder.setSectionId(sectionId);
                this.jdbcRepository.insertBusinessObject(podScanOrder);
                logger.debug("生成POD扫描记录成功:" + podScanOrder);
            }
        }

        return "存储区POD扫描任务已下发!";
    }

    private List<Robot> getAvailableRobots(Section section) {
        List<Robot> robots = new ArrayList<>();
        Iterator<Robot> robotIterator = this.getRegistRobots().values().iterator();
        while (robotIterator.hasNext()) {
            Robot next = robotIterator.next();
            if (next.isAvaliable()
                    && next.getStatus() == Robot.IDLE
                    && next.getCurOrder() == null) {
                robots.add(next);
            }
        }
        logger.debug("加载Section:" + section.getSection_id() + "的可用小车 数量:" + robots.size());
        return robots;
    }

    private Robot getAvailableRobot(Section section) {
        Iterator<Robot> robotIterator = this.getRegistRobots().values().iterator();
        while (robotIterator.hasNext()) {
            Robot next = robotIterator.next();
            if (next.isAvaliable()
                    && next.getStatus() == Robot.IDLE
                    && next.getCurOrder() == null) {
                return next;
            }
        }
        return null;
    }

    /**
     * 执行释放POD之后的新POD呼叫
     *
     * @param sectionId
     * @param workStationId
     * @param podName
     * @param logicStationId
     */
    public void callStowPod(String sectionId, String workStationId, String podName, String logicStationId) {
        logger.debug("工作站:" + workStationId + "执行释放POD之后的新POD呼叫....");
        Section section = this.wareHouseManager.getSectionById(sectionId);
        Pod pod = this.podManager.findPodByPodName(section, podName);
        if (pod == null) {
            logger.debug("释放空POD:" + podName);
            return;
        }
        //看哪个车驮着这个POD
        Iterator<Robot> robotIterator = this.registRobots.values().iterator();
        Robot robot = null;
        while (robotIterator.hasNext()) {
            Robot next = robotIterator.next();
            if (Objects.equals(next.getPod(), pod)) {
                logger.debug("小车:" + next.getRobotId() + "驮着POD:" + pod.getPodName());
                robot = next;
            }
        }
        if (robot == null) {
            logger.error("没有小车驮着该POD,无法得知该POD的调度情况!");
            return;
        }
        Order order = robot.getCurOrder();
        if (order != null && order instanceof StationPodOrder) {
            StationPodOrder stationPodOrder = (StationPodOrder) order;
            if (stationPodOrder.getIndex() == Order.STATION2BUFFER
                    && Objects.equals(stationPodOrder.getType(), TripType.STOW_POD)) {
                logger.debug("上架POD:" + pod.getPodName() + "正在离开工作站!");
                this.webApiBusiness.callStowPods(logicStationId);
            } else {
                logger.debug("POD:" + pod.getPodName() + "正在离开工作站! " +
                        "但不能调用StowPod接口:" + stationPodOrder);
            }
        } else {
            logger.error("不是StationPodOrder类型:" + order);
        }
    }

    /**
     * 工作站当前pod列表
     *
     * @param sectionId
     * @param workStationId
     * @return
     */
    public String wsPodList(String sectionId, String workStationId) {
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }

        WorkStation workStation = section.workStationMap.get(workStationId);
        Map data = new HashMap();
        Address stop = this.wareHouseManager
                .getAddressByAddressCodeId(workStation.getStopPoint(), workStation.getSectionId());
        Address mid = this.wareHouseManager
                .getAddressByAddressCodeId(workStation.getMidPoint(), workStation.getSectionId());
        data.put("停止点", stop.getPod());
        data.put("中间点", mid.getPod());

        return JsonUtils.map2Json(data);
    }

    public String checkPodStatus(String sectionId, String podId) {
        Section section = this.wareHouseManager.getSectionById(sectionId);
        Pod pod = this.podManager.getPodByRcsPodId(Long.parseLong(podId), section);
        if (pod == null) {
            return "POD:" + podId + "不存在!";
        }
        return pod.toString();
    }

    public String checkAddrState(String sectionId, String addrCodeId) {
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        Address address = section.addressMap.get(addrCodeId);
        Pod pod = this.podManager.getPodByAddress(Long.parseLong(addrCodeId), sectionId);
        Robot robot = this.getOnAddrRobot(address, section);
        Map data = new HashMap();
        data.put("地址码", address == null ? "" : address.toString());
        data.put("码上POD", pod == null ? "" : pod.toString());
        data.put("码上小车", robot == null ? "" : robot.toString());

        return JsonUtils.map2Json(data);
    }

    private Robot getOnAddrRobot(Address address, Section section) {
        return address.getRobot();
        /*Iterator<Robot> robotIterator = this.getRegistRobots().values().iterator();
        while (robotIterator.hasNext()) {
            Robot robot = robotIterator.next();
            if (Objects.equals(robot.getAddressId(), address.getId())
                    && robot.getStatus() != Robot.NOTCHECKED
                    && Objects.equals(robot.getSectionId(), section.getSection_id())) {
                return robot;
            }
        }
        return null;*/
    }

    /**
     * 清除下发小车的路径
     *
     * @param robot
     */
    public void ON_WCS_RCS_CLEAR_ALLPATH(Robot robot) {
        logger.debug("ON_WCS_RCS_CLEAR_ALLPATH 清除小车下发的路径! robot:" + robot.getRobotId());
        Map map = new HashMap();
        map.put("robotID", robot.getRobotId());
        String sectionID = robot.getSectionId();
        Section section = this.wareHouseManager.getSectionById(sectionID);
        map.put("sectionID", section.getRcs_sectionId());
        this.messageSender.WCS_RCS_CLEAR_ALLPATH(map);
    }

    /**
     * 小车重发路径
     *
     * @param sectionId
     * @param robotId
     * @return
     */
    public String resendOrder(String sectionId, String robotId, String findNewEndAddr) {
        logger.debug("重新发消息给小车:sectionId:" + sectionId + " robotId:" + robotId + " findNewEndAddr:" + findNewEndAddr);
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "小车未注册:" + robotId;
        }
        if (robot.getCurOrder() == null) {
            logger.error("小车当前没有正在运行的任务");
            return "小车" + robotId + "当前没有正在运行的任务";
        }
        Order order = robot.getCurOrder();
        if ("true".equalsIgnoreCase(findNewEndAddr)) {//新获取一个地址
            long endAddrCodeId = order.getEndAddr();
            //先获取另外一个地址
            String newEndAddr = this.podManager.computeTargetAddress(order.getPod().getPodId(), robot.getSectionId());
            Address dest = this.wareHouseManager.getAddressByAddressCodeId(newEndAddr, robot.getSectionId());
            Long robotLocked = robot.getLockedAddr();
            if (robotLocked > 0 && endAddrCodeId != robotLocked) {//状态不一致时
                Address lockedAddr = this.wareHouseManager.getAddressByAddressCodeId(endAddrCodeId, sectionId);
                if (lockedAddr != null) {
                    lockedAddr.robotLock(robotLocked, false);
                }
            }
            robot.setLockedAddr(Long.parseLong(newEndAddr));
            order.setEndAddr(Long.parseLong(newEndAddr));
            dest.robotLock(robot.getRobotId(), true);
            dest.setNodeState(AddressStatus.RESERVED);
            //再将原先目标地址解锁
            if (endAddrCodeId > 0) {
                Address addr = this.wareHouseManager.getAddressByAddressCodeId(endAddrCodeId, sectionId);
                if (addr != null && AddressStatus.RESERVED.equals(addr.getNodeState())
                        && addr.getLockedBy() == robot.getRobotId()) {
                    addr.robotLock(robot.getRobotId(), false);
                }
            }
        }

        order.reInitOrder();
        order.setFinished(false);
        this.ON_WCS_RCS_CLEAR_ALLPATH(robot);
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("重发路径！");
        order.sendMessage2Rcs();
        return robot.toString();
    }

    /**
     * 触发锁格超时，重新发消息给小车
     *
     * @param sectionId
     * @param robotId
     * @return
     */
    public String autoResendOrder4AGVRequestPath(String sectionId, String robotId) {
        logger.debug("触发锁格超时或转弯处重新计算路径，重新发消息给小车:sectionId:" + sectionId + " robotId:" + robotId);
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "小车未注册:" + robotId;
        }
        if (robot.getCurOrder() == null) {
            logger.error("触发锁格超时或转弯处重新计算路径，小车当前没有正在运行的任务");
            return "触发锁格超时或转弯处重新计算路径，小车" + robotId + "当前没有正在运行的任务";
        }
        Order order = robot.getCurOrder();
        /*long endAddrCodeId = order.getEndAddr();
        if (endAddrCodeId > 0) {
            Address addr = this.wareHouseManager.getAddressByAddressCodeId(endAddrCodeId, sectionId);
            if (addr != null && "Reserved".equals(addr.getNodeState()) && addr.getLockedBy()==robot.getRobotId()) {
                addr.setNodeState("Available");
                addr.setLockedBy(0L);
            }
        }*/
        order.reInitOrder();
        order.setFinished(false);
        logger.debug("触发锁格超时或转弯处重新计算路径，已经由RCS提前清除小车:" + robotId + "路径，WCS准备重发路径！");
        order.sendMessage2Rcs();
        return robot.toString();
    }

    public String sendOrder(String sectionId, String robotId, String orderId) {
        logger.debug("sendNewOrder给小车:sectionId:" + sectionId + " robotId:" + robotId);
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "小车未注册:" + robotId;
        }
        Order order = this.orderManager.getOrderById(robot, orderId);
        if (order == null) {
            return "调度单:" + orderId + "不存在 robotId:" + robotId;
        }
        order.setJdbcRepository(this.jdbcRepository);
        order.setWareHouseManager(this.wareHouseManager);
        order.setWebApiBusiness(this.webApiBusiness);
        order.setPodManager(this.podManager);
        order.setSystemPropertiesManager(this.systemPropertiesManager);
        robot.setCurOrder(order);
        robot.setStatus(Robot.WORKING);
        robot.setLastOrderId(order.getOrderId());

        order.initOrder();
        if (order.getOrderError() > Order.SUCCESS) {
            order.unlockEndAddress();
            return "调度单:" + orderId + "不可执行:" + order.getOrderError();
        }
        this.ON_WCS_RCS_CLEAR_ALLPATH(robot);
        CommonUtils.sleep(2000L);
        order.sendMessage2Rcs();
        order.process();
        return robot.toString();
    }

    public String checkPodFavAddrs(String sectionId, String podId) {
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }

        Pod pod = this.podManager.getPodByRcsPodId(Long.parseLong(podId), section);
        if (pod == null) {
            return "POD:" + podId + "不存在!";
        }
        List<Address> addrList = pod.getFavorAddrs();
        List<String> reList = new ArrayList<String>();
        if (addrList != null && addrList.size() > 0) {
            for (int i = 0; i < addrList.size(); i++) {
                if (i == 6) {
                    break;
                }
                reList.add(addrList.get(i).toString());
            }
        } else {
            return "pod:" + podId + " 未找到热度地址！";
        }
        return JsonUtils.list2Json(reList);
    }

    public String checkWorkStationAddrs(String sectionId, String workStationId) {
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        WorkStation workStation = section.workStationMap.get(workStationId);
        Map<String, String> reMap = new LinkedHashMap<>();
        reMap.put("stopAddrCodeId", workStation.getStopPoint());
        reMap.put("midAddrCodeId", workStation.getMidPoint());
        reMap.put("scanAddrCodeId", workStation.getScanPoint());
        reMap.put("bufferAddrCodeId", workStation.getBufferPoint());
        reMap.put("RotateInOutAddr", workStation.getRotateInOutAddr());
        List<String> addrs = workStation.rotateOutInAddrs;
        reMap.put("rotateOutInAddr1", addrs.get(0));
        reMap.put("rotateOutInAddr2", addrs.get(1));

        return JsonUtils.map2Json(reMap);
    }


    public String sendLastOrder(String robotId, String face, String index) {
        logger.debug("sendLastOrder给小车:robotId:" + robotId + " face:" + face + " index:" + index);
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "小车未注册:" + robotId;
        }
        Order order = this.orderManager.getOrderById_FaceOrIndex(robot, face, index);
        if (order == null) {
            return "face:" + face + "index:" + index + "不存在! robotId:" + robotId;
        }
        order.setJdbcRepository(this.jdbcRepository);
        order.setWareHouseManager(this.wareHouseManager);
        order.setWebApiBusiness(this.webApiBusiness);
        order.setPodManager(this.podManager);
        order.setSystemPropertiesManager(this.systemPropertiesManager);
        robot.setCurOrder(order);
        robot.setStatus(Robot.WORKING);
        robot.setLastOrderId(order.getOrderId());

        order.initOrder();
        if (order.getOrderError() > Order.SUCCESS) {
            order.unlockEndAddress();
            return "调度单:" + order.getOrderId() + "不可执行:" + order.getOrderError();
        }
        this.ON_WCS_RCS_CLEAR_ALLPATH(robot);
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        order.sendMessage2Rcs();
        order.process();
        return robot.toString();
    }

    public static final String GETRPADDRCODEID = "SELECT WD_NODE.`ADDRESSCODEID` FROM WD_NODE,WD_MAP " +
            "WHERE WD_NODE.MAP_ID=WD_MAP.ID AND WD_MAP.ACTIVE = TRUE \n" +
            "AND WD_NODE.TYPE=7 AND WD_MAP.SECTION_ID=?";

    /**
     * 生成小车去休息区的调度单
     *
     * @param robotId
     * @return
     */
    public String robot2Rp(String robotId) {
        logger.debug("robot2Rp让小车:robotId:" + robotId + "去休息区");
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "小车未注册:" + robotId;
        }
        String sectionId = robot.getSectionId();
        List<Map> list = this.jdbcRepository.queryBySql(GETRPADDRCODEID, sectionId);
        if (list == null || list.size() == 0) {
            return "Section:" + sectionId + "没有定义小车休息区";
        }
        Map data = list.get(0);//取第一个 不用判断了
        String addrCode = CommonUtils.parseString("ADDRESSCODEID", data);
        this.drive(robot.getRobotId(), addrCode);
        return robot.toString();
    }

    public String checkPodsState(String sectionId, String pods) {
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        List<String> podState = new ArrayList<>();
        if (!CommonUtils.isEmpty(pods)) {
            String[] _pods = pods.split(",");
            for (int i = 0; i < _pods.length; i++) {
                String spod = _pods[i];
                if (CommonUtils.isEmpty(spod)) {
                    continue;
                }
                Long rcsPodId = Long.parseLong(spod);
                Pod pod = this.podManager.getPodByRcsPodId(rcsPodId, section);
                podState.add(pod.toString());
            }
        } else {
            List<Pod> list = this.podManager.getPods(section);
            for (int i = 0; i < list.size(); i++) {
                Pod pod = list.get(i);
                if (pod != null) {
                    podState.add(pod.toString());
                }
            }
        }
        return JsonUtils.list2Json(podState);
    }

    public String checkRobots(String sectionId, String robots) {
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        List<String> robotsState = new ArrayList<>();
        if (!CommonUtils.isEmpty(robots)) {
            String[] _robots = robots.split(",");
            for (int i = 0; i < _robots.length; i++) {
                String srobot = _robots[i];
                if (CommonUtils.isEmpty(srobot)) {
                    continue;
                }
                Long robotId = Long.parseLong(srobot);
                Robot robot = this.getRegistedRobot(robotId);
                robotsState.add(robot.toString());
            }
        } else {
            Iterator<Robot> robotIterator = registRobots.values().iterator();
            while (robotIterator.hasNext()) {
                Robot next = robotIterator.next();
                if (Objects.equals(section.getSection_id(), next.getSectionId())) {
                    robotsState.add(next.toString());
                }
            }
        }
        return JsonUtils.list2Json(robotsState);
    }

    public String robotOffline(String robotId, String withPod) {
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "没有这辆小车:" + robotId;
        }
        logger.debug("下线小车:" + robotId + ", robot=" + robot);
        robot.setAvaliable(false);
        robot.setStatus(Robot.ERROR);
        robot.setAddressId("0");
        this.registRobots.put(robot.getRobotId(), robot);
        //this.registRobots.remove(Long.parseLong(robotId));
        Pod pod = robot.getPod();
        if (pod != null) {
            logger.debug("小车:" + robotId + "驮pod=" + pod);
            if (robot.getRobotId() == pod.getLockedBy()) {
                pod.setLockedBy(0L);
                logger.debug("下线小车:" + robotId + "时，释放pod:" + pod.getRcsPodId() + " 的锁定状态");
            }
        } else {
            logger.debug("小车:" + robotId + "是空车");
        }
        Order curOrder = robot.getCurOrder();
        if (curOrder != null) {
            logger.debug("小车:" + robotId + "的curOrder=" + curOrder);
            Pod orderPod = robot.getCurOrder().getPod();
            if (orderPod != null) {
                logger.debug("小车:" + robotId + "的调度单pod=" + pod);
                if (robot.getRobotId() == orderPod.getLockedBy()) {
                    orderPod.setLockedBy(0L);
                    logger.debug("下线小车:" + robotId + "时，释放调度单pod:" + orderPod.getRcsPodId() + " 的锁定状态");
                }
            }
        } else {
            logger.debug("小车:" + robotId + "没有调度单");
        }

        robot.addKV("STATUS", Robot.ERROR).addKV("AVAILABLE", Boolean.FALSE).addKV("ADDRESSCODEID", 0);
        this.jdbcRepository.updateBusinessObject(robot);
        logger.debug("小车:" + robotId + "更新到数据库，STATUS=" + Robot.ERROR + ", AVAILABLE=" + Boolean.FALSE + ", ADDRESSCODEID=" + 0);
        /*Map delMap = new HashMap();
        delMap.put("ID", robot.getPkId());
        this.jdbcRepository.deleteRecords("WCS_ROBOT", delMap);*/
        this.ON_WCS_RCS_CLEAR_ALLPATH(robot);
        this.orderManager.clearRobotOrder(robot, withPod);
        logger.debug("小车:" + robotId + "的调度单清理完成");
        robot.setCurOrder(null);
        CommonUtils.sleep(1500L);
        this.ON_WCS_RCS_OFFLINE_ROBOT(robot);
        return "小车已下线:" + robotId;
    }

    /**
     * 必须是停止时才调用 用于重发路径
     *
     * @param sectionId
     * @return
     */
    public void updateStorageAddrs(String sectionId) {
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }

        List<Pod> pods = this.podManager.getPods(section);
        List<Address> addresses = section.addressList;
        Set<Address> occupied = new HashSet();
        for (int i = 0; i < pods.size(); i++) {
            Pod pod = pods.get(i);
            if (pod.getAddress() != null && pod.getAddress().getType() == AddressType.STORAGE) {
                pod.getAddress().setNodeState(AddressStatus.OCCUPIED);
                occupied.add(pod.getAddress());
            }
        }
        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);
            if (!occupied.contains(address) && address.getType() == AddressType.STORAGE) {
                if (address.getAddressGroup() == null) {
                    address.setNodeState(AddressStatus.AVALIABLE);
                } else {
                    Address inner = address.getGroupInnerAddr();
                    if (inner == null || Objects.equals(inner.getNodeState(), AddressStatus.OCCUPIED)) {
                        address.setNodeState(AddressStatus.AVALIABLE);
                    } else {
                        address.setNodeState(AddressStatus.RESERVED);
                    }
                }

            }
        }
    }

    /**
     * 只在空车时操作
     *
     * @param robotId
     * @param act
     * @return
     */
    public String robotAdjust(String robotId, String act) {
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "没有这辆小车:" + robotId;
        }
        //this.registRobots.remove(Long.parseLong(robotId));
        int oldStatus = robot.getStatus();
        boolean oldAvailable = robot.isAvaliable();
        robot.setAvaliable(false);
        robot.setStatus(Robot.ERROR);

        robot.addKV("AVAILABLE", Boolean.FALSE).addKV("STATUS", Robot.ERROR);
        this.jdbcRepository.updateBusinessObject(robot);
        CommonUtils.sleep(1000L);
        this.ON_WCS_RCS_CLEAR_ALLPATH(robot);
        CommonUtils.sleep(1000L);
        Long addr = Long.parseLong(robot.getAddressId());
        Long toAddr = 0L;
        Section section = this.wareHouseManager.getSectionById(robot.getSectionId());
        List<Integer> list = this.mapManager.MapRowAndColumn(section.getRcs_sectionId());
        int colNum = 31;
        if (list != null && list.size() > 2) {
            colNum = list.get(1);
        }

        if ("left".equals(act)) {
            toAddr = addr - 1;
        } else if ("right".equals(act)) {
            toAddr = addr + 1;
        } else if ("down".equals(act)) {
            toAddr = addr + colNum;
        } else if ("up".equals(act)) {
            toAddr = addr - colNum;
        }
        if (null == this.wareHouseManager.getAddressByAddressCodeId(toAddr, robot.getSectionId())) {
            return "地图上目标地址不存在:" + toAddr;
        }

        List<Long> path = new ArrayList<>();
        path.add(addr);
        path.add(toAddr);

        Map msg = new HashMap();
        msg.put("robotID", robot.getRobotId());
        //Section section = this.wareHouseManager.getSectionById(robot.getSectionId());
        msg.put("sectionID", section.getRcs_sectionId());
        msg.put("time", System.currentTimeMillis());
        msg.put("time", System.currentTimeMillis());
        //其他信息
        msg.put("seriesPath", path);
        this.messageSender.sendMsg("section" + section.getRcs_sectionId(),
                ISender.WCS_RCS_AGV_SERIESPATH, msg);
        int i = 0;
        while (!Objects.equals(robot.getAddressId(), toAddr + "") && i < 10) {
            CommonUtils.sleep(1000L);
            logger.debug("小车:" + robotId + "未到目的地:" + toAddr);
            i++;
        }
        logger.debug("小车:" + robotId + "到达目的地:" + toAddr);
        robot.setStatus(oldStatus);//将状态改回
        robot.setAvaliable(oldAvailable);
        robot.addKV("STATUS", robot.getStatus()).addKV("AVAILABLE", robot.isAvaliable());
        this.jdbcRepository.updateBusinessObject(robot);
        return robot.toString();
    }

    public String robot2Charge(String robotId) {
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "没有这辆小车:" + robotId;
        }
        String sectionId = robot.getSectionId();
        Section section = this.wareHouseManager.getSectionById(sectionId);
        if (section.chargers == null || section.chargers.size() == 0) {
            return "Section:" + section.getRcs_sectionId() + " 没有配置充电桩";
        }
        Iterator<Charger> chargerIterator = section.chargers.values().iterator();
        Charger charger = null;
        while (chargerIterator.hasNext()) {
            Charger iter = chargerIterator.next();
            if (canCharge(iter)) {
                charger = iter;
                break;
            }
        }
        if (charger == null) {
            return "没有可用的充电桩! 小车:" + robotId + " section:" + section.getRcs_sectionId();
        }
        this.createNewChargerOrder(robot, charger);
        return "小车:" + robotId + " 去" + charger.getAddressCodeId() + "充电";
    }

    private void createNewChargerOrder(Robot robot, Charger charger) {
        ChargerDriveOrder order = new ChargerDriveOrder();
        order.addKV("ID", CommonUtils.genUUID());
        CommonUtils.genUselessInfo(order.getKv());
        order.addKV("TRIP_STATE", TripStatus.AVAILABLE);
        order.addKV("DRIVE_ID", robot.getRobotId());
        order.addKV("TRIP_TYPE", TripType.CHARGER_DRIVE);
        order.addKV("CHARGER_ID", charger.getChargerId());
        order.addKV("WAREHOUSE_ID", robot.getWareHouseId());
        order.addKV("SECTION_ID", robot.getSectionId());
        this.jdbcRepository.insertBusinessObject(order);
    }

    private boolean canCharge(Charger charger) {
        if (!Objects.equals(charger.getState(), ChargerStatus.AVAILABLE)) {
            return false;
        }
        String addr = charger.getAddressCodeId();
        Iterator<Robot> iterator = this.registRobots.values().iterator();
        while (iterator.hasNext()) {
            Robot robot = iterator.next();
            if (Objects.equals(robot.getAddressId(), addr)) {
                //如果该位置有车
                return false;
            }
        }
        return true;
    }

    public void ON_RCS_WCS_LOCK_CELL_TIMEOUT(Map data) {
        long robotID = CommonUtils.parseLong("robotID", data);
        logger.debug("ON_RCS_WCS_LOCK_CELL_TIMEOUT:小车" + robotID + "锁格超时:" + data);
        long rcsSectionID = CommonUtils.parseLong("sectionID", data);
        long currentAddress = CommonUtils.parseLong("currentAddress", data);
        if (failCheckRobot(robotID, rcsSectionID)) return;
        Robot robot = this.getRobotById(robotID);
        if (robot.getStatus() == Robot.NOTCHECKED) {
            return;//未登录
        }
        Section section = this.wareHouseManager.getSectionByRcsSectionId(rcsSectionID);
        if (section == null) {
            logger.debug("通过sectionID:" + rcsSectionID + "获取到的section为空！");
            return;
        }
        Address robotAddr = this.wareHouseManager.getAddressByAddressCodeId(currentAddress, section);
        if (robotAddr == null) {
            logger.debug("小车：" + robotID + " 的地址：" + currentAddress + " 不存在!");
            return;
        }
        String nextAddressId = CommonUtils.parseString("nextLockAddress", data);
        logger.debug("锁格超时的点：" + nextAddressId);
        if ("-1".equals(nextAddressId)) {
            logger.debug("POD防撞触发锁格超时，不改变任何COST，直接重算路径！");
        } else {
            String cost = "";
            String unReserved = this.systemPropertiesManager.getProperty("Cost_Reserved", section.getWareHouse_id());
            if (!CommonUtils.isEmpty(unReserved)) {
                cost = unReserved;
            } else {
                cost = "1000";
            }
            if (!CommonUtils.isEmpty(nextAddressId) && !CommonUtils.isEmpty(currentAddress + "")) {
                String nextAddrListStr = nextAddressId + "," + currentAddress;
                logger.debug("准备更新address：" + nextAddrListStr + " 的NewCost:" + cost);
                this.webApiBusiness.updateNewCost(section.getWareHouse_id(), section.getSection_id(), nextAddrListStr, cost);
                newCostAddressMap.put(currentAddress + "," + rcsSectionID, System.currentTimeMillis());
                newCostAddressMap.put(nextAddressId + "," + rcsSectionID, System.currentTimeMillis());
                logger.debug("已经被增大COST值的地址有：" + JsonUtils.map2Json(newCostAddressMap));
            }
        }
        //resendPathToAGV
        data.put("addressCodeID", CommonUtils.parseLong("currentAddress", data));
        if (robot.getPod() != null) {
            Long podCodeID = robot.getPod().getRcsPodId();
            data.put("podCodeID", podCodeID);
        }
        logger.debug("准备重新请求路径,robotID:" + robotID + ",  " + JsonUtils.map2Json(data));
        this.ON_LOCKCELL_TIMEOUT_TO_RESEND_PATH(data);
        logger.debug("重新请求路径成功!robotID:" + robotID);
    }

    //还原COST
    public void ON_RCS_WCS_UNLOCKED_CELL_LIST(Map data) {
        logger.debug("ON_RCS_WCS_LOCK_CELL_TIMEOUT:" + data);
        long rcsSectionID = CommonUtils.parseLong("sectionID", data);
        Section section = this.wareHouseManager.getSectionByRcsSectionId(rcsSectionID);
        if (section == null) {
            return;
        }
        logger.debug("清除前，已经被增大COST值的地址有：" + newCostAddressMap.size() + "个,:" + JsonUtils.map2Json(newCostAddressMap));
        List unLockedList = (List) data.get("unlockedCellList");
        if (data.containsKey("wcsAutoClearNewCost") && "1".equals(data.get("wcsAutoClearNewCost"))) {
            logger.debug("WCS定时解锁的地址：" + JsonUtils.list2Json(unLockedList));
        } else {
            logger.debug("RCS已经解锁的地址：" + JsonUtils.list2Json(unLockedList));
        }
        if (unLockedList != null && unLockedList.size() != 0 && newCostAddressMap != null && !newCostAddressMap.isEmpty()) {
            //List<String> updateList = new ArrayList<String>();
            String updateList = "";
            for (int i = 0; i < unLockedList.size(); i++) {
                String addrCodeId = unLockedList.get(i) + "";
                if (newCostAddressMap.containsKey(addrCodeId + "," + rcsSectionID)) {
                    //updateList.add(addrCodeId);
                    updateList += "".equals(updateList) ? addrCodeId : "," + addrCodeId;
                    logger.debug("newCostAddressMap remove:" + addrCodeId);
                    newCostAddressMap.remove(addrCodeId + "," + rcsSectionID);
                }
            }
            //logger.debug("需要还原cost值的地址："+JsonUtils.list2Json(updateList));
            //this.wareHouseManager.batchUpdateCellsCost(section.getSection_id(), updateList, null);
            logger.debug("需要还原cost值的地址：" + updateList);
            logger.debug("清除后，已经被增大COST值的地址有：" + newCostAddressMap.size() + "个,:" + JsonUtils.map2Json(newCostAddressMap));
            if (updateList != null && !"".equals(updateList)) {
                this.webApiBusiness.updateNewCost(section.getWareHouse_id(), section.getSection_id(), updateList, null);
            }
        }
    }

    public String updateRcsAddressList(String sectionId, String aList, String unList) {
        logger.debug("在section:" + sectionId + " 中批量更新RCS_CELL的锁定状态。 解锁地址码列表：" + aList + "; 锁定地址码列表：" + unList);
        if (CommonUtils.isEmpty(sectionId) || (CommonUtils.isEmpty(aList) && CommonUtils.isEmpty(unList))) {
            return "入参 sectionId:" + sectionId + "  或(aList and unList) 为空！";
        }
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section:" + sectionId + " is null!";
        }
        List availableFailList = null;
        List availableList = null;
        if (!CommonUtils.isEmpty(aList)) {
            availableFailList = new ArrayList();
            availableList = new ArrayList();
            String[] strArr = aList.split(",");
            for (int i = 0; i < strArr.length; i++) {
                String addrCodeId = strArr[i];
                Address address = this.wareHouseManager.getAddressByAddressCodeId(addrCodeId, section.getSection_id());
                if (address == null) {
                    logger.debug("address(Available):" + addrCodeId + " 不在section:" + sectionId + "中!");
                    availableFailList.add(addrCodeId);
                    continue;
                }
                availableList.add(addrCodeId);
            }
        }
        List unAvailableFailList = null;
        List unAvailableList = null;
        if (!CommonUtils.isEmpty(unList)) {
            unAvailableFailList = new ArrayList();
            unAvailableList = new ArrayList();
            String[] strArr = unList.split(",");
            for (int i = 0; i < strArr.length; i++) {
                String addrCodeId = strArr[i];
                Address address = this.wareHouseManager.getAddressByAddressCodeId(addrCodeId, section.getSection_id());
                if (address == null) {
                    logger.debug("address(unAvailable):" + addrCodeId + " 不在section:" + sectionId + "中!");
                    unAvailableFailList.add(addrCodeId);
                    continue;
                }
                unAvailableList.add(addrCodeId);
            }
        }
        Map msgMap = new HashMap();
        msgMap.put("availableAddressList", availableList);
        msgMap.put("unAvailableAddressList", unAvailableList);
        msgMap.put("sectionID", section.getRcs_sectionId());
        msgMap.put("time", System.currentTimeMillis());
        this.messageSender.WCS_RCS_UPDATE_CELLS(msgMap);
        msgMap.put("updateAvailableList_Fail", availableFailList);
        msgMap.put("updateUnAvailableList_Fail", unAvailableFailList);
        return "RCS update address:" + JsonUtils.map2Json(msgMap);
    }

    public String changeRcsPodsPosition(String sectionId, String podStrs, String addrStrs) {
        logger.debug("在section:" + sectionId + " 中批量更新POD位置。 POD列表：" + podStrs + "; 地址码列表：" + addrStrs);
        if (CommonUtils.isEmpty(sectionId) || CommonUtils.isEmpty(podStrs) || CommonUtils.isEmpty(addrStrs)) {
            return "入参 sectionId:" + sectionId + "  或 podIdList 或 addrCodeIdList 为空！";
        }
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section:" + sectionId + " is null!";
        }
        String[] podArr = podStrs.split(",");
        String[] addrArr = addrStrs.split(",");
        if (podArr.length != addrArr.length) {
            return "POD列表：" + podStrs + " 与 地址码列表：" + addrStrs + " 数量不匹配，请检查参数！";
        }
        Map failMap = new HashMap();
        Map updateMap = new HashMap();
        for (int i = 0; i < podArr.length; i++) {
            String podId = podArr[i];
            String addrCodeId = addrArr[i];
            Pod pod = this.podManager.getPodById(podId, section.getSection_id());
            Address addr = this.wareHouseManager.getAddressByAddressCodeId(addrCodeId, section.getSection_id());
            if (pod == null || addr == null) {
                logger.debug("pod:" + podId + " 或 address:" + addrCodeId + " 不在section:" + sectionId + " 中！");
                failMap.put(podId, addrCodeId);
                continue;
            }
            updateMap.put(podId, addrCodeId);
        }
        Map msgMap = new HashMap();
        msgMap.put("sectionID", section.getRcs_sectionId());
        msgMap.put("podAddrMap", updateMap);
        msgMap.put("time", System.currentTimeMillis());
        this.messageSender.WCS_RCS_CHANGING_POD_POSITION(msgMap);
        msgMap.put("changeFail", failMap);
        return "RCS change:" + JsonUtils.map2Json(msgMap);
    }

    public String resendAGVPath2Rcs(String sectionId, String robotId) {
        logger.debug("在section:" + sectionId + " 中重新向RCS下发路径，robot：：" + robotId);
        if (CommonUtils.isEmpty(sectionId) || CommonUtils.isEmpty(robotId)) {
            return "入参 sectionId:" + sectionId + "  或 robotId 为空！";
        }
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section:" + sectionId + " is null!";
        }
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "没有这辆小车:" + robotId;
        }
        Map msgMap = new HashMap();
        msgMap.put("sectionID", section.getRcs_sectionId());
        msgMap.put("robotID", Long.parseLong(robotId));
        msgMap.put("time", System.currentTimeMillis());
        this.messageSender.WCS_RCS_AGV_PATH_RESEND(msgMap);
        return "RCS resend path, " + JsonUtils.map2Json(msgMap);
    }

    public String checkRcsItemInfo(String sectionId, String itemKey, String itemValue) {
        logger.debug("查看RCS中section:" + sectionId + " 的信息，itemKey：" + itemKey + " , itemValue：" + itemValue);
        if (CommonUtils.isEmpty(sectionId) || CommonUtils.isEmpty(itemKey) || CommonUtils.isEmpty(itemValue)) {
            return "入参 sectionId:" + sectionId + "  或 itemKey:" + itemKey + " 或 itemValue:" + itemValue + " 为空！";
        }
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section:" + sectionId + " is null!";
        }
        if ("robot".equals(itemKey)) {
            Robot robot = this.getRegistedRobot(Long.parseLong(itemValue));
            if (robot == null) {
                return "没有这辆小车:" + itemValue;
            }
        }
        if ("address".equals(itemKey)) {
            Address address = this.wareHouseManager.getAddressByAddressCodeId(itemValue, section);
            if (address == null) {
                return "没有这个地址:" + itemValue;
            }
        }
        Map msgMap = new HashMap();
        String sessionId = UUID.randomUUID().toString();
        msgMap.put("sessionID", sessionId);
        msgMap.put("sectionID", section.getRcs_sectionId());
        msgMap.put("itemKey", itemKey);
        msgMap.put("itemValue", itemValue);
        msgMap.put("time", System.currentTimeMillis());
        this.messageSender.WCS_RCS_REQUEST_ITEM_INFO(msgMap);
        logger.debug("RCS info 查询section:" + sectionId + "  " + itemKey + " = " + itemValue + "   会话ID：" + sessionId + ", " + JsonUtils.map2Json(msgMap));
        try {
            long beginTime = System.currentTimeMillis();
            long timeOut = 20 * 1000;
            Map receiveMap = null;
            while (true) {
                if (System.currentTimeMillis() - beginTime > timeOut) {
                    logger.debug("RCS info 查询超时，未收到RCS响应！");
                    return "RCS " + itemKey + " = " + itemValue + " info 查询超时，未收到RCS响应！";
                }
                receiveMap = MessageReceiver.receiveRcsItemInfoMap;
                if (receiveMap != null && !receiveMap.isEmpty()) {
                    if (receiveMap.containsKey(sessionId)) {
                        String reStr = JsonUtils.map2Json((Map) receiveMap.get(sessionId));
                        receiveMap.remove(sessionId);
                        logger.debug("RCS 返回 info：" + JsonUtils.map2Json(receiveMap));
                        return reStr;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "RCS " + itemKey + " = " + itemValue + " info 查询出错！";
    }

    public String checkMediaAGVConfigParameters(String sectionId, String robotId, String matchWord) {
        logger.debug("查看MediaAGV配置参数section:" + sectionId + " ，robotId：" + robotId + " , matchWord:" + matchWord);
        if (CommonUtils.isEmpty(sectionId) || CommonUtils.isEmpty(robotId)) {
            return "入参 sectionId:" + sectionId + "  或 robotId:" + robotId + " 为空！";
        }
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section:" + sectionId + " is null!";
        }
        //TODO  Media小车?
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "没有这辆小车:" + robotId;
        }
        Map msgMap = new HashMap();
        msgMap.put("sectionID", section.getRcs_sectionId());
        msgMap.put("robotID", Long.parseLong(robotId));
        msgMap.put("matchWord", CommonUtils.isEmpty(matchWord) ? "1" : matchWord);
        msgMap.put("time", System.currentTimeMillis());
        this.messageSender.WCS_RCS_REQUEST_MEDIA_AGV_CONFIG_PARAMETERS(msgMap);
        logger.debug("MediaAGV 配置参数查询，section:" + sectionId + "  ,AGV:" + robotId + ", " + JsonUtils.map2Json(msgMap));
        try {
            long beginTime = System.currentTimeMillis();
            long timeOut = 20 * 1000;
            Map receiveMap = null;
            while (true) {
                if (System.currentTimeMillis() - beginTime > timeOut) {
                    logger.debug("MediaAGV配置参数查询超时，未收到RCS响应！");
                    return "MediaAGV: " + robotId + " 配置参数查询超时，未收到RCS响应！";
                }
                receiveMap = MessageReceiver.receiveRcsMediaAGVConfigParametersMap;
                if (receiveMap != null && !receiveMap.isEmpty()) {
                    if (receiveMap.containsKey(robotId)) {
                        String reStr = JsonUtils.map2Json((Map) receiveMap.get(robotId));
                        receiveMap.remove(robotId);
                        logger.debug("RCS 返回MediaAGV配置参数：" + JsonUtils.map2Json(receiveMap));
                        return reStr;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "MediaAGV： " + robotId + " 配置参数查询出错！";
    }

    public String updateMediaAGVConfigParameters(String sectionId, String robotId) {
        logger.debug("修改MediaAGV配置参数section:" + sectionId + " ，robotId：" + robotId);
        if (CommonUtils.isEmpty(sectionId) || CommonUtils.isEmpty(robotId)) {
            return "入参 sectionId:" + sectionId + "  或 robotId:" + robotId + " 为空！";
        }
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section:" + sectionId + " is null!";
        }
        //TODO
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "没有这辆小车:" + robotId;
        }
        Map msgMap = new HashMap();
        AGVConfigParameters agvConfig = new AGVConfigParameters();
        Properties properties = agvConfig.getProperties();
        Enumeration allNames = properties.propertyNames();
        while (allNames.hasMoreElements()) {
            String proName = (String) allNames.nextElement();
            msgMap.put(proName, properties.getProperty(proName));
        }
        msgMap.put("sectionID", section.getRcs_sectionId());
        msgMap.put("robotID", Long.parseLong(robotId));
        msgMap.put("time", System.currentTimeMillis());
        this.messageSender.WCS_RCS_UPDATE_MEDIA_AGV_CONFIG_PARAMETERS(msgMap);
        return JsonUtils.map2Json(msgMap);
    }

    public void ON_RCS_WCS_RESPONSE_MEDIA_ERROR(Map data) {

    }

    public String checkMediaAGVError(String sectionId, String robotId) {
        logger.debug("查看MediaAGV配置参数section:" + sectionId + " ，robotId：" + robotId);
        if (CommonUtils.isEmpty(sectionId) || CommonUtils.isEmpty(robotId)) {
            return "入参 sectionId:" + sectionId + "  或 robotId:" + robotId + " 为空！";
        }
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section:" + sectionId + " is null!";
        }
        //TODO
        if (!"all".equalsIgnoreCase(robotId)) {
            Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
            if (robot == null) {
                return "没有这辆小车:" + robotId;
            }
        }
        try {
            long beginTime = System.currentTimeMillis();
            long timeOut = 10 * 1000;
            Map receiveMap = null;
            while (true) {
                if (System.currentTimeMillis() - beginTime > timeOut) {
                    logger.debug("查看AGV：" + robotId + "故障，收到RCS响应超时！");
                    return "查看AGV：" + robotId + " 故障";
                }
                receiveMap = MessageReceiver.receiveRcsMediaErrorMap;
                if (receiveMap != null && !receiveMap.isEmpty()) {
                    if (receiveMap.containsKey(robotId)) {
                        String reStr = JsonUtils.map2Json((Map) receiveMap.get(robotId));
                        receiveMap.remove(robotId);
                        logger.debug("收到RCS 回复故障包：" + JsonUtils.map2Json(receiveMap));
                        return reStr;
                    } else if ("all".equalsIgnoreCase(robotId)) {
                        logger.debug("收到RCS 回复所有小车故障包：" + JsonUtils.map2Json(receiveMap));
                        return JsonUtils.map2Json(receiveMap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "查看RCS故障 AGV： " + robotId + " 时出错！";
    }

    public String clearMediaAGVError(String sectionId, String robotId, String seriousErrorID, String commonErrorID, String logicErrorID, String generalErrorID) {
        logger.debug("清除MediaAGV配置参数section:" + sectionId + " ，robotId：" + robotId + " , seriousErrorID:" + seriousErrorID + " , commonErrorID:" + commonErrorID + " , logicErrorID:" + logicErrorID + " , generalErrorID:" + generalErrorID);
        if (CommonUtils.isEmpty(sectionId) || CommonUtils.isEmpty(robotId)) {
            return "入参 sectionId:" + sectionId + "  或 robotId:" + robotId + " 为空！";
        }
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section:" + sectionId + " is null!";
        }
        //TODO
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "没有这辆小车:" + robotId;
        }
        Map msgMap = new HashMap();
        msgMap.put("seriousErrorID", seriousErrorID);
        msgMap.put("commonErrorID", commonErrorID);
        msgMap.put("logicErrorID", logicErrorID);
        msgMap.put("generalErrorID", generalErrorID);
        msgMap.put("sectionID", section.getRcs_sectionId());
        msgMap.put("robotID", Long.parseLong(robotId));
        msgMap.put("time", System.currentTimeMillis());
        this.messageSender.WCS_RCS_CLEAR_MEDIA_ERROR(msgMap);
        return JsonUtils.map2Json(msgMap);
    }

    public String orderAction2AGV(String sectionId, String robotId, String actionType, String actionValue) {
        logger.debug("向AGV下发动作命令，section:" + sectionId + " ，robotId：" + robotId + " , actionType：" + actionType + " , actionValue:" + actionValue);
        if (CommonUtils.isEmpty(sectionId) || CommonUtils.isEmpty(robotId) || CommonUtils.isEmpty(actionType)) {
            return "入参 sectionId:" + sectionId + "  或 robotId:" + robotId + " 或 actionType:" + actionType + " 为空！";
        }
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section:" + sectionId + " is null!";
        }
        Robot robot = this.getRegistedRobot(Long.parseLong(robotId));
        if (robot == null) {
            return "没有这辆小车:" + robotId;
        }
        Map msgMap = new HashMap();
        String sessionId = UUID.randomUUID().toString();
        msgMap.put("sessionID", sessionId);
        msgMap.put("sectionID", section.getRcs_sectionId());
        msgMap.put("robotID", Long.parseLong(robotId));
        msgMap.put("actionType", actionType);
        //旋转度数校验
        actionValue = !CommonUtils.isEmpty(actionValue) && Integer.parseInt(actionType) > 3 && Integer.parseInt(actionType) < 8 ? Math.abs(Long.parseLong(actionValue)) % 360 + "" : actionValue;
        msgMap.put("actionValue", CommonUtils.isEmpty(actionValue) ? "" : actionValue);
        msgMap.put("time", System.currentTimeMillis());
        this.messageSender.WCS_RCS_REQUEST_ACTION_COMMAND(msgMap);
        logger.debug("向AGV下发动作命令，section:" + sectionId + "  " + actionType + " = " + actionValue + "   会话ID：" + sessionId + ", " + JsonUtils.map2Json(msgMap));
        try {
            long beginTime = System.currentTimeMillis();
            long timeOut = 10 * 1000;
            Map receiveMap = null;
            while (true) {
                if (System.currentTimeMillis() - beginTime > timeOut) {
                    logger.debug("已经向AGV：" + robotId + "下发动作命令，收到RCS响应超时！");
                    return "向AGV：" + robotId + " 下发命令： " + actionType + " = " + actionValue + " 已经下发，收到RCS响应超时！";
                }
                receiveMap = MessageReceiver.receiveRcsActionCommandMap;
                if (receiveMap != null && !receiveMap.isEmpty()) {
                    if (receiveMap.containsKey(robotId)) {
                        String reStr = JsonUtils.map2Json((Map) receiveMap.get(robotId));
                        receiveMap.remove(robotId);
                        logger.debug("收到RCS 回复动作命令包：" + JsonUtils.map2Json(receiveMap));
                        return reStr;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "处理RCS回复动作命令： " + actionType + " = " + actionValue + " 时出错！";
    }

    public List<Long> getUnableLockTimeoutTrigger(String sectionId, WorkStation workStation) {
        List<Long> reList = new ArrayList<Long>();
        List<String> inAddrs = workStation.rotateOutInAddrs;
        String outAddr = workStation.getRotateInOutAddr();
        List<Long> rotate2Station = this.webApiBusiness.getHeavyPath(workStation.getWareHouseId(), sectionId, Integer.parseInt(outAddr), Integer.parseInt(workStation.getStopPoint()));
        if (rotate2Station == null || rotate2Station.size() == 0) {
            logger.debug("section:" + sectionId + " 的workStation:" + workStation.getWorkStationId() + " 的旋转区到工作站路径计算为空");
            return null;
        }
        if (inAddrs != null && inAddrs.size() != 0) {
            for (int i = 0; i < inAddrs.size(); i++) {
                Long aLong = Long.parseLong(inAddrs.get(i));
                reList.add(aLong);
            }
        }
        for (int i = 0; i < rotate2Station.size(); i++) {
            Long aLong = rotate2Station.get(i);
            reList.add(aLong);
        }
        logger.debug("section:" + sectionId + " 的workStation:" + workStation.getWorkStationId() + " 不需要触发锁格超时的地址：" + JsonUtils.list2Json(reList));
        return reList;
    }


    public String doPath(WcsPath path, Long podId) {
        Long podUp = path.getPodUpAddress();
        Long podDown = path.getPodDownAddress();
        Long endAddr = path.getEndAddr();
        Long robotId = path.getRobotID();
        Robot robot = this.getRegistedRobot(robotId);
        if (robot == null) {
            return "没有这辆小车:" + robotId;
        }

        String warehouseId = robot.getWareHouseId();
        String sectionId = robot.getSectionId();
        String addr = robot.getAddressId();
        Integer current = Integer.parseInt(addr);

        List<Long> src2PodUp = new ArrayList<>();
        if (podUp != 0L && !Objects.equals(current + "", podUp + "")) {
            Integer podUpAddr = Integer.parseInt(podUp + "");
            src2PodUp = this.webApiBusiness.getEmptyPath(warehouseId, sectionId, current, podUpAddr);
            current = podUpAddr;
        }

        List<Long> addr2PodDown = new ArrayList<>();
        if (podUp != 0L && !Objects.equals(current + "", podUp + "")) {
            Integer down = Integer.parseInt(podDown + "");
            addr2PodDown = this.webApiBusiness.getHeavyPath(warehouseId, sectionId, current, down);
            current = down;
        }

        List<Long> down2Dest = new ArrayList<>();
        if (podUp != 0L && !Objects.equals(current + "", endAddr + "")) {
            Integer end = Integer.parseInt(endAddr + "");
            down2Dest = this.webApiBusiness.getHeavyPath(warehouseId, sectionId, current, end);
        }
        List<Long> src2Dest = new ArrayList<>();
        if (current == Integer.parseInt(addr)) {//podup poddown 为空
            if (current != Integer.parseInt(endAddr + "")) {
                src2Dest = this.webApiBusiness.getEmptyPath(warehouseId, sectionId, current, Integer.parseInt(addr));
            }
        }

        if (src2Dest.size() == 0 && (src2PodUp.size() + addr2PodDown.size() + down2Dest.size()) == 0) {
            return "没有计算出路径" + path;
        }

        if (src2Dest.size() != 0) {

        }
        return null;
    }

    public String updateCharger(String sectionId, String chargerId, String chargerType) {
        logger.debug("在section:" + sectionId + " 中更新充电桩信息：" + chargerId + " = " + chargerType);
        if (CommonUtils.isEmpty(sectionId) || CommonUtils.isEmpty(chargerId) || CommonUtils.isEmpty(chargerType)) {
            return "入参 sectionId:" + sectionId + "  或 chargerId 或 chargerType 为空！";
        }
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section:" + sectionId + " is null!";
        }
        Map<String, Charger> chargers = section.chargers;
        if (chargers == null) {
            return "chargers is null";
        }
        if (chargers.containsKey(chargerId)) {
            Iterator<Map.Entry<String, Charger>> it = chargers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Charger> entry = it.next();
                if (chargerId.equals(entry.getKey())) {
                    Charger cg = entry.getValue();
                    cg.setChargerType(Integer.parseInt(chargerType));
                    return cg.toString();
                }
            }
        }
        return "not found chargerId:" + chargerId;
    }

    /**
     * 下线rcs小车
     *
     * @param robot
     */
    public void ON_WCS_RCS_OFFLINE_ROBOT(Robot robot) {
        logger.debug("ON_WCS_RCS_OFFLINE_ROBOT 下线rcs小车! robot:" + robot.getRobotId());
        Map map = new HashMap();
        map.put("robotID", robot.getRobotId());
        String sectionID = robot.getSectionId();
        Section section = this.wareHouseManager.getSectionById(sectionID);
        map.put("sectionID", section.getRcs_sectionId());
        map.put("time", System.currentTimeMillis());
        this.messageSender.WCS_RCS_OFFLINE_ROBOT(map);
    }

    //空闲地址维持newcost的最长时间，单位秒
    private int keepNewCostTime = 10;

    /**
     * 定时清空空地址的newcost
     * 每60秒检测一次
     * newcost维持时间：keepNewCostTime=10 秒
     */
    @Scheduled(fixedDelay = 60000l)
    public void autoClearNewCost() {
        logger.debug("WCS开始定时清空持续" + keepNewCostTime + " 秒，newcost不为空的空地址，");
        if (this.newCostAddressMap == null || this.newCostAddressMap.isEmpty()) {
            logger.debug("WCS中增大的newcost集合为空，不需要清空！");
            return;
        }
        Long currentTime = System.currentTimeMillis();
        Map<Long, List> addrGroupMap = new HashMap<Long, List>();
        for (Map.Entry<String, Long> entry : this.newCostAddressMap.entrySet()) {
            String key = entry.getKey();
            Long changeTime = entry.getValue();
            logger.debug("WCS-newcost: addressCodeId,sectionId= " + key + " and changeTime= " + changeTime);
            if (CommonUtils.isEmpty(key) || CommonUtils.isEmpty(changeTime + "")) {
                continue;
            }
            String[] varrs = key.split(",");
            Long addressCodeId = Long.parseLong(varrs[0]);
            Long rcsSectionId = Long.parseLong(varrs[1]);
            if (currentTime - changeTime < this.keepNewCostTime * 1000L) {
                logger.debug("addressCodeId:" + addressCodeId + " 不满足自动还原时间，获取下一个");
                continue;
            }
            Section section = this.wareHouseManager.getSectionByRcsSectionId(rcsSectionId);
            //还原标志
            boolean availableFlag = true;
            //是否有小车在该地址
            if (this.basicRobots != null && this.basicRobots.size() > 0) {
                for (Map.Entry<Long, Robot> robotEntry : this.basicRobots.entrySet()) {
                    Robot robot = robotEntry.getValue();
                    if (!section.getSection_id().equals(robot.getSectionId())) {
                        continue;
                    }
                    if ((addressCodeId + "").equals(robot.getAddressId())) {
                        availableFlag = false;
                        logger.debug("addressCodeId:" + addressCodeId + " 上有小车：" + robot.getRobotId() + " 不满足自动还原条件，获取下一个");
                        break;
                    }
                }
            }
            if (!availableFlag) {
                continue;
            }
            //是否有pod在该地址
            List<Pod> podList = this.podManager.getPodListBySection(section.getSection_id());
            if (podList != null && podList.size() > 0) {
                for (int i = 0; i < podList.size(); i++) {
                    Pod pod = podList.get(i);
                    if ((addressCodeId + "").equals(pod.getAddress().getId())) {
                        availableFlag = false;
                        logger.debug("addressCodeId:" + addressCodeId + " 上有POD：" + pod.getRcsPodId() + " 不满足自动还原条件，获取下一个");
                        break;
                    }
                }
            }
            if (!availableFlag) {
                continue;
            }
            logger.debug("sectionId:" + rcsSectionId + " addressCodeId:" + addressCodeId + " 满足自动还原条件");
            if (!addrGroupMap.containsKey(rcsSectionId)) {
                List unLockList = new ArrayList();
                unLockList.add(addressCodeId + "");
                addrGroupMap.put(rcsSectionId, unLockList);
            } else {
                List unLockList = addrGroupMap.get(rcsSectionId);
                unLockList.add(addressCodeId + "");
            }
        }
        if (addrGroupMap != null && addrGroupMap.size() > 0) {
            for (Map.Entry<Long, List> entry : addrGroupMap.entrySet()) {
                Map data = new HashMap();
                data.put("wcsAutoClearNewCost", "1");
                data.put("sectionID", entry.getKey());
                data.put("unlockedCellList", entry.getValue());
                this.ON_RCS_WCS_UNLOCKED_CELL_LIST(data);
            }
        }

    }

    public String ON_ANY_WCS_WAREHOUSE_INIT_REQUEST(Map data) {
        if (!flag) {
            return "请等待WCS初始化完成！";
        }
        logger.debug("仓库初始化请求ON_ANY_WCS_WAREHOUSE_INIT_REQUEST:" + data);
        Long requestTime = CommonUtils.parseLong("requestTime", data);
        logger.debug("----------------------响应主题：ANY_WCS_WAREHOUSE_INIT_REQUEST-----------------");
        String warehouseId = CommonUtils.parseString("warehouseId", data);
        if (!CommonUtils.isEmpty(warehouseId)) {
            WareHouse warehouse = this.wareHouseManager.getWareHouseMap().get(warehouseId);
            if (warehouse == null) {
                logger.debug("初始化仓库时未找到该仓库！warehouseId:" + warehouseId);
                return "初始化仓库时未找到该仓库！warehouseId:" + warehouseId;
            }
        }
        Map resMap = new HashMap();
        resMap.put("requestTime", requestTime);
        Map<String, WareHouse> warehouseMap = this.wareHouseManager.getWareHouseMap();
        Map allWarehouseInfoMap = new HashMap();
        for (Map.Entry<String, WareHouse> whEntry : warehouseMap.entrySet()) {
            WareHouse wareHouse = whEntry.getValue();
            if (!CommonUtils.isEmpty(warehouseId)) {
                if (!wareHouse.equals(wareHouse.getWareHouseId())) {
                    continue;
                }
            }
            Map whMap = new HashMap();
            Map secsMap = new HashMap();
            Map<String, Section> sectionMap = wareHouse.sectionMap;
            for (String str : sectionMap.keySet()) {
                Section section = sectionMap.get(str);
                Map secMap = new HashMap();
                secMap.put("sectionUUID", section.getSection_id());
                secMap.put("sectionRcsId", section.getRcs_sectionId());
                secMap.put("sectionMapId", section.getActiveMapId());
                secMap.put("sectionName", section.getSection_name());
                secsMap.put(section.getSection_id(), secMap);
            }
            whMap.put("warehouseId", wareHouse.getWareHouseId());
            try {
                List<Map> warehouseList = this.jdbcRepository.queryByKey("ROBOTMANAGER.GETWAREHOUSEBYID", wareHouse.getWareHouseId());
                whMap.put("warehouseName", warehouseList.get(0).get("NAME"));
            } catch (Exception e) {
                logger.debug("查询仓库信息出错！ + \n" + e.toString());
                e.printStackTrace();
            }
            whMap.put("sectionMap", secsMap);
            allWarehouseInfoMap.put(wareHouse.getWareHouseId(), whMap);
        }
        resMap.put("allWarehouseInfo", allWarehouseInfoMap);
        this.messageSender.WCS_ANY_WAREHOUSE_INIT_RESPONSE(resMap);
        logger.debug("仓库初始化请求回复成功!");
        return JsonUtils.map2Json(resMap);
    }

    /**
     * 处理动作完成包，目前只有顶升完成和下降完成
     * actedType: 5-顶升完成；6-下降完成
     *
     * @param data
     */
    public void RCS_WCS_ACTION_FINISHED_COMMAND(Map data) {
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.debug("处理小车：" + robotId + "动作完成包");
        Long rcsSectionId = CommonUtils.parseLong("sectionID", data);
        Section section = this.wareHouseManager.getSectionByRcsSectionId(rcsSectionId);
        if (section == null) {
            logger.debug("section为空，rcsSectionId=" + rcsSectionId);
            return;
        }
        Long addressCodeId = CommonUtils.parseLong("addressCodeID", data);
        Address address = this.wareHouseManager.getAddressByAddressCodeId(addressCodeId, section);
        //Robot robot = this.getRobotById(robotId);
        if (address == null || address.getType() != AddressType.STORAGE) {
            logger.debug("address为空或不是存储位，addressCodeId=" + addressCodeId);
            return;
        }
        String podId = CommonUtils.parseString("podCodeID", data);
        int actionType = CommonUtils.parseInteger("actedType", data);
        int toward = CommonUtils.parseInteger("podAfaceToward", data);
        String cost = "";
        if (actionType == 5) {
            logger.debug("小车:" + robotId + " 驮着pod：" + podId + " 完成顶升动作");
            cost = "0";
        } else if (actionType == 6) {
            logger.debug("小车:" + robotId + " 驮着pod：" + podId + " 完成下降动作");
            /*Pod pod = robot.getPod();
            logger.debug("下降完成的pod="+pod);
            if (pod != null && pod.getIsNewCreated()==1){
                pod.setDirect(toward);
                pod.setAddress(address);
                pod.addKV("PLACEMARK", addressCodeId);
                pod.addKV("TOWARD", toward);
                pod.addKV("ADDRCODEID_TAR",-1);
                pod.addKV("XPOS_TAR",-1);
                pod.addKV("YPOS_TAR",-1);
                this.jdbcRepository.updateBusinessObject(pod);
                logger.debug("数据库中更新pod="+podId+", addressCodeId="+addressCodeId+", toward="+toward);
            }*/
            String podStop_HeavyCost = this.systemPropertiesManager.getProperty("Cost_PodStop_Heavy", section.getWareHouse_id());
            if (!CommonUtils.isEmpty(podStop_HeavyCost)) {
                cost = podStop_HeavyCost;
            } else {
                cost = "100";
            }
        } else {
            logger.debug("小车:" + robotId + " 完成其它动作码：" + actionType);
            return;
        }
        logger.debug("准备更新存储位address：" + addressCodeId + " 的HeavyCost为原有Cost加上：" + cost);
        this.webApiBusiness.addHeavyCost(section.getWareHouse_id(), section.getSection_id(), addressCodeId + "", cost);
        logger.debug("存储位HeavyCost更新成功！");

    }

    /**
     * 定时扫描数据库中的充电任务，并推送详情到mq
     *
     * @param
     */
    @Scheduled(fixedDelay = 20000l)
    public void WCS_ANY_CHARGE_ORDER() {
        logger.debug("定时扫描数据库中未完成的充电任务，并推送详情到mq...");
        Map msgMap = new HashMap();
        List<Map> reList = new ArrayList<Map>();
        List<Map> chargerOrders = this.jdbcRepository.queryByKey("RobotManager.QueryChargeOrders");
        if (chargerOrders != null && chargerOrders.size() > 0) {
            logger.debug("WCS_ANY_CHARGE_ORDER 数据库中查到未完成充电任务共" + chargerOrders.size() + "条");
            for (int i = 0; i < chargerOrders.size(); i++) {
                Map map = chargerOrders.get(i);
                Map iMap = new HashMap();
                iMap.put("warehouseId", CommonUtils.parseString("WAREHOUSE_ID", map));
                iMap.put("sectionUUID", CommonUtils.parseString("SECTION_ID", map));
                iMap.put("oderId", CommonUtils.parseString("ID", map));
                iMap.put("tripState", CommonUtils.parseString("TRIP_STATE", map));
                String robotId = CommonUtils.parseString("DRIVE_ID", map);
                iMap.put("driveId", robotId);
                iMap.put("chargeUUID", CommonUtils.parseString("CHARGER_ID", map));
                //小车充电时，不发送实时包，此处上报位置信息
                Robot robot = this.registRobots.get(Long.parseLong(robotId));
                if (robot != null) {
                    String robotAddressCodeId = robot.getAddressId();
                    logger.debug("充电小车：" + robotId + "的位置：" + robotAddressCodeId);
                    iMap.put("robotAddressCodeId", robotAddressCodeId);
                }
                reList.add(iMap);
            }
        } else {
            logger.debug("WCS_ANY_CHARGE_ORDER 没有正在执行的充电任务！");
            return;
        }
        msgMap.put("chargeList", reList);
        this.messageSender.WCS_ANY_CHARGE_ORDER(msgMap);
    }

    public String addBatchMdRobots(Map msg) {
        logger.debug("addBatchMdRobots 入参msg=" + JsonUtils.map2Json(msg));
        int startIndex = CommonUtils.parseInteger("startIndex", msg);
        int total = CommonUtils.parseInteger("total", msg);
        if (startIndex < 1) {
            logger.debug("startIndex小于1，非法！");
            return "startIndex小于1，非法！";
        }
        if (total < 1) {
            logger.debug("total小于1，不需要新增！");
            return "total小于1，不需要新增！";
        }
        Map paramsMap = new HashMap();
        paramsMap.put("PASSWORD", "0");
        paramsMap.put("ROBOT_TYPE_ID", "1");
        paramsMap.put("WAREHOUSE_ID", msg.get("warehouseId"));
        paramsMap.put("RECENTLY_MAINTAIN_TIME", msg.get("createdDate"));
        paramsMap.put("HOT_RESET_TIMES", "0");
        paramsMap.put("CREATED_DATE", msg.get("createdDate"));
        paramsMap.put("CREATED_BY", "SYSTEM");
        paramsMap.put("MODIFIED_DATE", msg.get("createdDate"));
        paramsMap.put("MODIFIED_BY", "SYSTEM");
        paramsMap.put("VERSION", "1");
        for (int i = 1; i <= total; i++) {
            paramsMap.put("ID", startIndex + i);
            paramsMap.put("ROBOT_ID", startIndex + i);
            this.jdbcRepository.insertRecord("MD_ROBOT", paramsMap);
            logger.debug("MD_ROBOT 新增一条记录成功！" + JsonUtils.map2Json(paramsMap));
        }
        logger.debug("MD_ROBOT 从第" + startIndex + "辆开始，成功新增" + total + " 条记录！");
        return "MD_ROBOT 从第" + startIndex + "辆开始，成功新增" + total + " 条记录！";
    }

    //替身术
    public String exchangeOrderRobot(Map paramMap) {
        String sectionId = CommonUtils.parseString("sectionId", paramMap);
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section is null,sectionId=" + sectionId;
        }
        String oldRobotId = CommonUtils.parseString("oldRobotId", paramMap);
        String newRobotId = CommonUtils.parseString("newRobotId", paramMap);
        Long podId = CommonUtils.parseLong("podId", paramMap);
        String podAddrCodeId = CommonUtils.parseString("podAddrCodeId", paramMap);
        Robot oldRobot = this.registRobots.get(Long.parseLong(oldRobotId));
        if (oldRobot == null || oldRobot.getCurOrder() == null) {
            logger.debug("小车不存在或调度单为空！oldRobotId=" + oldRobotId);
            return "小车不存在或调度单为空！oldRobotId=" + oldRobotId;
        }
        oldRobot.setStatus(Robot.ERROR);
        oldRobot.setAvaliable(false);
        Robot newRobot = this.registRobots.get(Long.parseLong(newRobotId));
        if (newRobot == null) {
            logger.debug("小车不存在！newRobotId=" + newRobotId);
            return "小车不存在！newRobotId=" + newRobotId;
        }
        Pod tarPod = this.podManager.getPodByRcsPodId(podId, section);
        if (tarPod == null) {
            logger.debug("POD不存在！podId=" + podId);
            return "POD不存在！podId=" + podId;
        }
        Address podAddr = this.getAddressById(podAddrCodeId, section.getSection_id());
        if (podAddr == null || (podAddr.getLockedBy() != 0L && podAddr.getLockedBy() != newRobot.getRobotId() && podAddr.getLockedBy() != oldRobot.getRobotId())) {
            return "POD所在位置不存在或POD所在位置无法锁定";
        }
        if (Robot.IDLE != newRobot.getStatus() || !newRobot.isAvaliable()) {
            logger.debug("小车状态不可用！status=" + newRobot.getStatus() + ", available=" + newRobot.isAvaliable());
            return "小车状态不可用！status=" + newRobot.getStatus() + ", available=" + newRobot.isAvaliable();
        }
        Order order = oldRobot.getCurOrder();
        Pod robotPod = oldRobot.getPod();
        if (robotPod == null || order == null) {
            return "oldRobot的pod为空或调度单为空";
        }
        if (podId != robotPod.getRcsPodId()) {
            logger.debug("替身术使用失败，POD不一致！podId=" + podId);
            return "替身术使用失败，POD不一致！podId=" + podId;
        }
        //获取小车
        newRobot.setAvaliable(false);
        newRobot.setStatus(Robot.WORKING);
        //锁定地址
        podAddr.robotLock(newRobot.getRobotId(), true);
        //锁定pod
        if (robotPod.getLockedBy() == oldRobot.getRobotId()) {
            tarPod.setLockedBy(newRobot.getRobotId());
        }
        String sql = "UPDATE RCS_TRIP SET DRIVE_ID=? WHERE ID=?";
        this.jdbcRepository.updateBySql(sql.toUpperCase(), newRobotId, order.getId());
        tarPod.setAddress(podAddr);
        tarPod.setRobot(newRobot);
        order.setPod(tarPod);
        order.setRobot(newRobot);
        newRobot.setCurOrder(order);
        //newRobot.setPod(tarPod);还没驮上
        newRobot.setOrderIndex(oldRobot.getOrderIndex());
        newRobot.setLastOrderId(oldRobot.getLastOrderId());
        newRobot.setTargetAddrCodeId(oldRobot.getTargetAddrCodeId());
        if (order instanceof StationPodOrder) {
            StationPodOrder stationPodOrder = (StationPodOrder) order;
            order.setIsExchangeRobot(1);
            if (stationPodOrder.getIndex() == Order.POD2STATION || stationPodOrder.getIndex() > 0) {
                order.reInitOrder();
            } else {
                if (stationPodOrder.getIndex() == Order.STATION2BUFFER || stationPodOrder.getIndex() == Order.BUFFER2STORAGE) {
                    Long destAddr = order.getEndAddr();
                    if (destAddr == null || destAddr == 0L || this.wareHouseManager.getAddressByAddressCodeId(destAddr, section).getType() != AddressType.STORAGE) {
                        destAddr = Long.parseLong(this.podManager.computeTargetAddress(tarPod.getRcsPodId() + "", sectionId));
                    }
                    Address destAddress = this.wareHouseManager.getAddressByAddressCodeId(destAddr, section);
                    //回仓库存储区不需要判断同组格子是否被占用 只在调pod到工位时产生
                    if (CommonUtils.isEmpty(destAddr + "") || destAddress == null) {
                        order.setOrderError(Order.ERROR_NO_HOTADDRESS);
                        return "Order.ERROR_NO_HOTADDRESS";
                    }
                    order.setEndAddr(destAddr);
                    destAddress.setNodeState(AddressStatus.RESERVED);//不参与计算了
                    if (order.lockEndAddr()) {
                        destAddress.setLockedBy(newRobot.getRobotId());
                    } else {
                        logger.info("目标格子：" + destAddr + "锁定失败！");
                        order.setOrderError(Order.ERROR_LOCK_ENDADDR);
                        return "Order.ERROR_LOCK_ENDADDR";
                    }
                    //trip表设置回存储区的终点
                    logger.debug("替换小车后返回存储区，更新：tripID=" + order.getOrderId() + " , endAddress=" + destAddr);
                    boolean re = order.updateEndAddr2Database(order.getOrderId(), destAddr + "");
                    if (re) {
                        logger.debug("替换小车后返回存储区更新成功！tripID=" + order.getOrderId() + " , endAddress=" + destAddr);
                    } else {
                        logger.debug("替换小车后返回存储区更新失败！tripID=" + order.getOrderId() + " , endAddress=" + destAddr);
                    }
                    //执行数据库锁定
                    //this.getJdbcRepository().updateBusinessObject(dest);
                    logger.debug("目标格子:" + destAddr + "已经被锁定!");
                    List<Long> back2storage = new ArrayList<Long>();
                    if (!tarPod.getAddress().getId().equals(newRobot.getAddressId()) && order.carryPod(newRobot, tarPod)) {
                        //空车到POD位置
                        back2storage = order.getEmptyPath(section.getWareHouse_id(), sectionId, newRobot.getAddressId(), tarPod.getAddress().getId());
                        if (back2storage == null || back2storage.size() == 0) {
                            order.setOrderError(Order.ERROR_EMPTY_PATH);
                            return "Order.ERROR_EMPTY_PATH";
                        }
                    }
                    List<Long> pod2storage = order.getHeavyPath(section.getWareHouse_id(), sectionId, tarPod.getAddress().getId(), destAddr + "");
                    order.add2Path(back2storage, pod2storage);
                    order.setEndAddr(destAddr);
                    order.getWcsPath().setSeriesPath(back2storage);
                    order.getWcsPath().setPodDownAddress(destAddr);
                    order.getWcsPath().setPodUpAddress(Long.parseLong(tarPod.getAddress().getId()));
                    order.sendMessage2Rcs();
                }
            }
        }
        return "ok";
    }

    //{wcsTime=1520995272819, podCodeInfoX=6, podCodeInfoY=-12, sectionID=2,
    // robotID=36, podCodeInfoTheta=180.0, speed=1, podCodeID=118,
    // addressCodeInfoTheta=180.40675, addressCodeInfoY=56, addressCodeID=513,
    // addressCodeInfoX=4, rtTime=1520995089777}
    public String sendMsg2MQ(Map msg) {
        //登录包
        Map loginMsg = new HashMap();
        loginMsg.put("sectionID", 2L);
        loginMsg.put("robotID", 36L);
        loginMsg.put("podCodeID", 0L);
        loginMsg.put("password", 0);
        loginMsg.put("loginTime", System.currentTimeMillis());
        loginMsg.put("addressCodeID", 513L);
        loginMsg.put("laveBattery", 900L);
        loginMsg.put("voltage", 49000);
        loginMsg.put("exchangeName", "section2");
        loginMsg.put("routingKey", "RCS_WCS_ROBOT_LOGIN");
        this.messageSender.sendMsgByMap(loginMsg);
        CommonUtils.sleep(1000L);
        //实时包
        msg.put("sectionID", 2L);
        msg.put("robotID", 36L);
        msg.put("podCodeInfoTheta", 90f);
        msg.put("podCodeID", 106L);
        msg.put("addressCodeID", 513L);
        this.messageSender.sendMsgByMap(msg);
        return "消息发送成功：" + msg;
    }

    public String exchangeOrderRobot4Restart(Map msg) {
        String oldRobotId = CommonUtils.parseString("oldRobotId", msg);
        String newRobotId = CommonUtils.parseString("newRobotId", msg);
        String sectionId = CommonUtils.parseString("sectionId", msg);
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section is null,sectionId=" + sectionId;
        }
        /*Robot newRobot = this.registRobots.get(Long.parseLong(newRobotId));
        if (newRobot==null){
            logger.debug("newRobot未登录！newRobotId="+newRobotId);
        }else if (newRobot.isAvaliable() && newRobot.getStatus()==Robot.IDLE){
            newRobot.setAvaliable(false);
            newRobot
        }*/
        String queryRobotOrder_sql = "select * from RCS_TRIP where DRIVE_ID=? and TRIP_STATE='Available'";
        String updateOrderRobot_sql = "update RCS_TRIP set DRIVE_ID=? where ID=?";
        List<Map> rows = this.jdbcRepository.queryBySql(queryRobotOrder_sql.toUpperCase(), oldRobotId);
        if (rows != null && rows.size() > 0) {
            logger.debug("小车oldRobotId=" + oldRobotId + " 的Available调度单有：" + rows.size() + "条");
            for (int i = 0; i < rows.size(); i++) {
                Map map = rows.get(i);
                String orderId = CommonUtils.parseString("ID", map);
                this.jdbcRepository.updateBySql(updateOrderRobot_sql.toUpperCase(), newRobotId, orderId);
                logger.debug("更换调度单小车，orderId=" + orderId + ", oldRobotId=" + oldRobotId + ", newRobotId=" + newRobotId);
            }
        } else {
            logger.debug("小车没有已分配待执行的调度单，oldRobotId=" + oldRobotId);
            return "小车没有已分配待执行的调度单，oldRobotId=" + oldRobotId;
        }
        return "ok";
    }

    public String changeRobotOrderEndAddr(Map paramsMap) {
        String sectionId = CommonUtils.parseString("sectionId", paramsMap);
        Long robotId = CommonUtils.parseLong("robotId", paramsMap);
        String destAddressCodeId = CommonUtils.parseString("destAddressCodeId", paramsMap);
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "section is null,sectionId=" + sectionId;
        }
        Robot robot = this.registRobots.get(robotId);
        if (robot == null || robot.getCurOrder() == null) {
            logger.debug("小车不存在或调度单为空！robotId=" + robotId);
            return "小车不存在或调度单为空！robotId=" + robotId;
        }
        Order order = robot.getCurOrder();
        Pod orderPod = order.getPod();
        if (orderPod == null) {
            logger.debug("调度单pod为空！");
            return "调度单pod为空！";
        }
        Long endAddrId = order.getEndAddr();
        if (endAddrId != null && endAddrId > 0) {
            Address endAddr = order.getWareHouseManager().getAddressByAddressCodeId(endAddrId + "", sectionId);
            if (endAddr != null) {
                endAddr.robotLock(robotId, false);
            }
        }
        if (CommonUtils.isEmpty(destAddressCodeId)) {
            destAddressCodeId = this.podManager.computeTargetAddress(orderPod.getRcsPodId() + "", sectionId);
        }
        order.setEndAddr(Long.parseLong(destAddressCodeId));
        robot.setCurOrder(order);
        this.resendOrder(sectionId, robotId + "", "");
        return "ok";
    }

    public String genPodRunOrder(String sectionId, String robotId) {
        //生成podRun任务
        Long _robotId = Long.parseLong(robotId);

        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            return "不存在: section error:" + sectionId;
        }
        Robot robot = this.registRobots.get(robotId);
        if (robot == null) {
            logger.debug("小车不存在！robotId=" + robotId);
            return "小车不存在！robotId=" + robotId;
        }
        Pod robotPod = robot.getPod();
        if (robotPod != null) {
            robotPod.setLockedBy(robot.getRobotId());
        }
        if (robotPod != null && robot.getCurOrder() == null) {
            //这个时候是不能分配新任务的
            robot.setAvaliable(false);
            robot.setStatus(Robot.WORKING);
            PodRunOrder newOrder = new PodRunOrder();
            newOrder.setOrderStatus(TripStatus.NEW);
            newOrder.setOrderId(CommonUtils.genUUID());
            newOrder.setPod(robotPod);
            //newOrder.setEndAddr(Long.parseLong(address.getId()));
            newOrder.setWareHouseId(robot.getWareHouseId());
            newOrder.setSectionId(robot.getSectionId() + "");
            robot.setCurOrder(newOrder);
            robot.setLastOrderId(newOrder.getOrderId());

            return "OK";

        } else if (robot.getCurOrder() != null) {
            Order order = robot.getCurOrder();
            Pod orderPod = order.getPod();
            if (orderPod == null) {
                return "小车的调度单没有POD信息 robotId=" + robotId;
            }
            robot.setCurOrder(null);
            PodRunOrder newOrder = new PodRunOrder();
            newOrder.setOrderStatus(TripStatus.NEW);
            newOrder.setOrderId(CommonUtils.genUUID());
            //newOrder.setEndAddr(Long.parseLong(address.getId()));
            newOrder.setWareHouseId(orderPod.getWareHouseId());
            newOrder.setSectionId(orderPod.getSectionId() + "");
            newOrder.setPod(robotPod);

            robot.setCurOrder(newOrder);
            robot.setLastOrderId(newOrder.getOrderId());

            //更新原先的Order;

            this.jdbcRepository.updateBySql(UPDATE_TRIPPOSITION, TripStatus.AVAILABLE, order.getId());
            this.jdbcRepository.updateBySql(UPDATE_TRIP, TripStatus.AVAILABLE, order.getId());

        /*Map newValue = new HashMap();
        newValue.put("TRIP_STATE","Available");
        Map con = new HashMap();
        con.put("ID",order.getId());
        this.jdbcRepository.updateRecords("RCS_TRIP",newValue,con);*/


            return "OK";
        }

        return "小车" + robotId + "没有任务也没有POD";
        //this.orderManageter.saveOrderGroup(order);
    }

    private static final String UPDATE_TRIPPOSITION = "UPDATE RCS_TRIPPOSITION set TRIPPOSITION_STATE=? WHERE TRIP_ID=?";
    private static final String UPDATE_TRIP = "UPDATE RCS_TRIP set TRIP_STATE=? WHERE ID=?";

    public String driveRobot2Workstation(Map paramsMap) {
        logger.debug("driveRobot2Workstation入参：" + JsonUtils.map2Json(paramsMap));
        String sectionId = CommonUtils.parseString("sectionId", paramsMap);
        Long robotId = CommonUtils.parseLong("robotId", paramsMap);
        String workStationId = CommonUtils.parseString("workStationId", paramsMap);
        Map<String, String> reMap = new HashMap<String, String>();
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            reMap.put("reCode", "0");
            reMap.put("reMsg", "section is null, sectionId=" + sectionId);
            return JsonUtils.map2Json(reMap);
        }
        Robot robot = this.registRobots.get(robotId);
        if (robot == null) {
            logger.debug("小车不存在！robotId=" + robotId);
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车不存在！robotId=" + robotId);
            return JsonUtils.map2Json(reMap);
        }
        if (CommonUtils.isEmpty(workStationId)) {
            logger.debug("workStationId为空！workStationId=" + workStationId);
            reMap.put("reCode", "0");
            reMap.put("reMsg", "workStationId为空！workStationId=" + workStationId);
            return JsonUtils.map2Json(reMap);
        }
        if (!robot.isAvaliable()) {
            logger.debug("小车正在工作中，暂时不可用！robotId=" + robotId);
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车正在工作中，暂时不可用！robotId=" + robotId);
            return JsonUtils.map2Json(reMap);
        }
        String robotWorkStationId = robot.getWorkStationId();
        if (workStationId.equals(robotWorkStationId)) {
            logger.debug("小车：" + robotId + "已被当前工作站绑定！");
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车：" + robotId + "已被当前工作站绑定！");
            return JsonUtils.map2Json(reMap);
        }
        if (!CommonUtils.isEmpty(robotWorkStationId)) {
            WorkStation robotWorkStation = this.wareHouseManager.findWorkStation(robotWorkStationId);
            logger.debug("小车：" + robotId + "正在被停止点是：" + robotWorkStation.getStopPoint() + "的工作站使用，请稍等！");
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车：" + robotId + "正在被停止点是：" + robotWorkStation.getStopPoint() + "的工作站使用，请稍等！");
            return JsonUtils.map2Json(reMap);
        }
        WorkStation workStation = this.wareHouseManager.findWorkStation(workStationId);
        if (workStation == null) {
            logger.debug("未找到对应的工作站！workStationId=" + workStationId);
            reMap.put("reCode", "0");
            reMap.put("reMsg", "未找到对应的工作站！workStationId=" + workStationId);
            return JsonUtils.map2Json(reMap);
        }
        if (robot.getAddressId().equals(workStation.getStopPoint())) {
            logger.debug("小车：" + robotId + "已在当前工作站！");
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车：" + robotId + "已在当前工作站！");
            return JsonUtils.map2Json(reMap);
        }
        String stopAddrCodeId = workStation.getStopPoint();
        this.drive(robotId, stopAddrCodeId);
        robot.setWorkStationId(workStationId);
        logger.debug("小车：" + robotId + "任务下发成功！" + ", workStationId=" + workStationId);
        reMap.put("reCode", "1");
        reMap.put("reMsg", "小车：" + robotId + "任务下发成功！" + ", workStationId=" + workStationId);
        return JsonUtils.map2Json(reMap);
    }

    public String releaseRobotWorkStation(Map paramsMap) {
        logger.debug("releaseRobotWorkStation入参：" + JsonUtils.map2Json(paramsMap));
        String sectionId = CommonUtils.parseString("sectionId", paramsMap);
        Long robotId = CommonUtils.parseLong("robotId", paramsMap);
        String workStationId = CommonUtils.parseString("workStationId", paramsMap);
        Map<String, String> reMap = new HashMap<String, String>();
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            reMap.put("reCode", "0");
            reMap.put("reMsg", "section is null, sectionId=" + sectionId);
            return JsonUtils.map2Json(reMap);
        }
        if (CommonUtils.isEmpty(workStationId)) {
            logger.debug("workStationId为空！workStationId=" + workStationId);
            reMap.put("reCode", "0");
            reMap.put("reMsg", "workStationId为空！workStationId=" + workStationId);
            return JsonUtils.map2Json(reMap);
        }
        WorkStation workStation = this.wareHouseManager.findWorkStation(workStationId);
        if (workStation == null) {
            logger.debug("未找到对应的工作站！workStationId=" + workStationId);
            reMap.put("reCode", "0");
            reMap.put("reMsg", "未找到对应的工作站！workStationId=" + workStationId);
            return JsonUtils.map2Json(reMap);
        }
        String stopAddrCodeId = workStation.getStopPoint();
        Robot robot = new Robot();
        if (robotId != 0) {
            robot = this.registRobots.get(robotId);
        } else {
            Iterator<Robot> robotIterator = this.registRobots.values().iterator();
            while (robotIterator.hasNext()) {
                Robot iRobot = robotIterator.next();
                if (stopAddrCodeId.equals(iRobot.getAddressId())) {
                    logger.debug("工作站停止点" + stopAddrCodeId + "的小车：" + iRobot.getRobotId());
                    robot = iRobot;
                    break;
                }
            }
        }
        if (robot == null) {
            logger.debug("小车不存在！robotId=" + robotId);
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车不存在！robotId=" + robotId);
            return JsonUtils.map2Json(reMap);
        }
        if (!robot.isAvaliable()) {
            logger.debug("小车正在工作中，无法解绑！robotId=" + robotId);
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车正在工作中，无法解绑！robotId=" + robotId);
            return JsonUtils.map2Json(reMap);
        }
        String robotWorkStationId = robot.getWorkStationId();
        if (!workStationId.equals(robotWorkStationId)) {
            WorkStation robotWorkStation = this.wareHouseManager.findWorkStation(robotWorkStationId);
            logger.debug("小车：" + robotId + "已经被停止点是：" + robotWorkStation.getStopPoint() + "的工作站绑定，请稍等！");
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车：" + robotId + "已经被停止点是：" + robotWorkStation.getStopPoint() + "的工作站绑定，请稍等！");
            return JsonUtils.map2Json(reMap);
        }
        robot.setWorkStationId("");
        logger.debug("小车：" + robotId + "被停止点是：" + stopAddrCodeId + "的工作站解绑成功！");
        reMap.put("reCode", "1");
        reMap.put("reMsg", "小车：" + robotId + "被停止点是：" + stopAddrCodeId + "的工作站解绑成功！");
        return JsonUtils.map2Json(reMap);
    }

    public String getNoBindWorkStationRobots(Map paramsMap) {
        logger.debug("getNoBindWorkStationRobots入参：" + JsonUtils.map2Json(paramsMap));
        String sectionId = CommonUtils.parseString("sectionId", paramsMap);
        Map<String, String> reMap = new HashMap<String, String>();
        Section section = null;
        try {
            Long sec = Long.parseLong(sectionId);
            section = this.wareHouseManager.getSectionByRcsSectionId(sec);
        } catch (Exception e) {
            section = this.wareHouseManager.getSectionById(sectionId);
        }
        if (section == null) {
            reMap.put("reCode", "0");
            reMap.put("reMsg", "section is null, sectionId=" + sectionId);
            return JsonUtils.map2Json(reMap);
        }
        Iterator<Robot> robotIterator = this.registRobots.values().iterator();
        if (robotIterator == null) {
            reMap.put("reCode", "1");
            reMap.put("reMsg", "无可用的小车，请稍等");
            return JsonUtils.map2Json(reMap);
        }
        Map<Long, Robot> reRobotMap = new HashMap<Long, Robot>();
        while (robotIterator.hasNext()) {
            Robot iRobot = robotIterator.next();
            if (iRobot.isAvaliable() && iRobot.getStatus() == Robot.IDLE && CommonUtils.isEmpty(iRobot.getWorkStationId())) {
                logger.debug("工作站可以使用的小车有：" + iRobot.getRobotId());
                reRobotMap.put(iRobot.getRobotId(), iRobot);
            }
        }
        reMap.put("reCode", "1");
        reMap.put("reMsg", JsonUtils.map2Json(reRobotMap));
        return JsonUtils.map2Json(reMap);
    }

    //@Scheduled(fixedDelay = 60000l)
    //十分钟周期执行
    // 有两种，一种是作为小车目的地被锁，但小车实际目的地不是去这里
    //另一种是外层放了货架 内层是空的，生成PodRun任务，有分车逻辑分配 将它驮走，内部设置为不可用Reserved
    //每次周期检查两个格子 如果都是可用 并且没有目的地指向这两个存储位置，将外层不可用
    public void scheduleRestoreAddr() {
        String sys = systemPropertiesManager.getProperty("WCS_RESTOREADDR", "JN1");
        if (CommonUtils.isEmpty(sys) || Integer.parseInt(sys) == 0) {
            return;
        }
        logger.error("将存储格状态恢复，生成调度任务!");
        Map<String, WareHouse> wareHouseMap = this.wareHouseManager.getWareHouseMap();
        Iterator<WareHouse> iterator = wareHouseMap.values().iterator();
        while (iterator.hasNext()) {
            WareHouse wareHouse = iterator.next();
            Map<String, Section> sectionMap = wareHouse.sectionMap;
            Iterator<Section> sectionIterator = sectionMap.values().iterator();
            while (sectionIterator.hasNext()) {
                Section next = sectionIterator.next();
                this.updateSectionStorage(next);
            }
        }
    }

    /**
     * 更新地图的存储区 WCS与车分配逻辑存在时间差 会导致存储区位置不正常
     * 表现为:
     * 1、某存储格不是小车的目的地，但被某小车锁定
     * 2、深层货架两个节点都可用
     * 3、深层货架里头节点为空 外面节点被占
     * 4、两个节点都已放了货架，但仍然有车往该目的地放货架(在车调度里判断)
     * <p>
     * 以上原因待查
     *
     * @param section
     */
    public void updateSectionStorage(Section section) {
        Set<AddressGroup> addressGroups = new HashSet<>();//存放组地址的容器，运算过的就不需要再算了
        List<Address> addresses = section.getStorageAddrs();
        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);
            if (address.getAddressGroup() != null) {
                addressGroups.add(address.getAddressGroup());
            } else {
                //检查地址状态
                updateAddrStatus(address, section);
            }
        }
        //更新深层货架状态
        updateAdressGroups(section, addressGroups);
    }

    private void updateAdressGroups(Section next, Set<AddressGroup> addressGroups) {
        Iterator<AddressGroup> itAg = addressGroups.iterator();
        while (itAg.hasNext()) {
            AddressGroup addressGroup = itAg.next();
            Address inner = addressGroup.getInnerAddr();
            Address outter = addressGroup.getOutterAddr();
            /*以下不叫代码*/
            /*两个available 外面的要设置为Reserved*/
            if (inner.getNodeState().equals(AddressStatus.AVALIABLE)
                    && outter.getNodeState().equals(AddressStatus.AVALIABLE)) {
                outter.setNodeState(AddressStatus.RESERVED);
                continue;
            }
            if (inner.getNodeState().equals(AddressStatus.RESERVED)
                    && outter.getNodeState().equals(AddressStatus.AVALIABLE)) {
                outter.setNodeState(AddressStatus.RESERVED);
                //inner.setNodeState(AddressStatus.RESERVED);
                continue;
            }
            /*如果外面放了货架，里头可用，将里头Reserved*/
            if (inner.getNodeState().equals(AddressStatus.AVALIABLE)
                    && outter.getNodeState().equals(AddressStatus.OCCUPIED)) {
                String addrCodeId = outter.getId();
                Pod pod = this.podManager.getPodByAddress(Long.parseLong(addrCodeId),
                        next.getSection_id());
                if (pod != null) {
                    inner.setNodeState(AddressStatus.RESERVED);
                    Order order = new PodRunOrder();
                    CommonUtils.genUselessInfo(order.getKv());
                    order.addKV("SECTION_ID", inner.getSectionId())
                            .addKV("WAREHOUSE_ID", inner.getWareHouseId())
                            .addKV("POD_ID", pod.getPodId())
                            .addKV("TRIP_STATE", TripStatus.NEW)
                            .addKV("ID", CommonUtils.genUUID())
                            .addKV("VERSION", 0)
                            .addKV("TRIP_TYPE", order.getType());
                    //产生一条任务
                    this.jdbcRepository.insertBusinessObject(order);
                } else {
                    outter.setNodeState(AddressStatus.RESERVED);
                }
                logger.error("完成对地址组状态的调整 inner:" + inner + " out:" + outter);
            }
            //logger.error("完成对地址组状态的调整:"+addressGroup.getGroupId());
        }
    }

    //
    private void updateAddrStatus(Address address, Section next) {
        String state = address.getNodeState();
        String addrCodeId = address.getId();
        Pod pod = this.podManager.getPodByAddress(Long.parseLong(addrCodeId), next.getSection_id());
        //Robot robot = this.getOnAddrRobot(address, next);
        if (AddressStatus.AVALIABLE.equals(state) && pod != null) {
            address.setNodeState(AddressStatus.OCCUPIED);
            logger.error("节点:" + addrCodeId + "状态从" + state + "变成:" + address.getNodeState());
            return;
        }
        if (AddressStatus.RESERVED.equals(state)) {
            if (address.getLockedBy() != 0L) {//有车锁定 但车的目的地不是这里
                Robot robot = this.getRobotById(address.getLockedBy());
                if (robot.getCurOrder() != null
                        && robot.getCurOrder().getEndAddr() != Long.parseLong(addrCodeId)) {
                    //不在这个目的地
                    address.setNodeState(AddressStatus.AVALIABLE);
                    address.setLockedBy(0L);
                    logger.error("节点:" + addrCodeId + "状态从" + state + "变成:" + address.getNodeState());
                }
            }
            return;
        }
        if (AddressStatus.OCCUPIED.equals(state) && pod == null) {
            address.setNodeState(AddressStatus.AVALIABLE);
            address.setLockedBy(0L);
            logger.error("节点:" + addrCodeId + "状态从" + state + "变成:" + address.getNodeState());
            return;
        }
    }

    public String dispalySysProps(String key, String wareHouse) {
        String value = this.systemPropertiesManager.getProperty(key, wareHouse);
        Map data = new HashMap();
        data.put("wareHouse", wareHouse);
        data.put("key", key);
        data.put("value", value);
        return JsonUtils.map2Json(data);
    }

    public String driveRobot2PlacePod(String robotId) {
        Robot robot = this.registRobots.get(Long.parseLong(robotId));
        if (robot == null) {
            logger.debug("小车不存在！robotId=" + robotId);
            Map reMap = new HashMap();
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车不存在！robotId=" + robotId);
            return JsonUtils.map2Json(reMap);
        }
        if (podOnRobot(robot)) {
            robot.setAvaliable(false);//不分配任务了
            robot.setCurOrder(null);
            robot.getPod().setLockedBy(robot.getRobotId());//绑定
            this.createRunningPodRunOrder(robot);
            this.resendOrder(robot.getSectionId(), robotId, "false");
            Map reMap = new HashMap();
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车robotId=" + robotId + "去" + robot.getLockedAddr());
            return JsonUtils.map2Json(reMap);
        } else {
            logger.debug("小车不存在！robotId=" + robotId);
            Map reMap = new HashMap();
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车没有驮着POD！robotId=" + robotId);
            return JsonUtils.map2Json(reMap);
        }
    }

    public String driveRobot2Charge(String robotId) {
        Robot robot = this.registRobots.get(Long.parseLong(robotId));
        if (robot == null) {
            logger.debug("小车不存在！robotId=" + robotId);
            Map reMap = new HashMap();
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车不存在！robotId=" + robotId);
            return JsonUtils.map2Json(reMap);
        }
        robot.setCurOrder(null);
        robot.setAvaliable(false);
        Section section = this.wareHouseManager.getSectionById(robot.getSectionId());
        Address robotAddr = this.wareHouseManager.getAddressByAddressCodeId(robot.getAddressId(), section);
        Iterator<Charger> iterator = section.chargers.values().iterator();
        //找最近可用的
        Charger nearestAvailable = null;
        int distance = Integer.MAX_VALUE;
        while (iterator.hasNext()) {
            Charger charger = iterator.next();
            Address chargerAddr = wareHouseManager.getAddressByAddressCodeId(charger.getAddressCodeId(), section);
            int tempDist = Math.abs(chargerAddr.getxPosition() - robotAddr.getxPosition())
                    + Math.abs(chargerAddr.getyPosition() - robotAddr.getyPosition());
            if (tempDist < distance && Objects.equals(charger.getState(), ChargerStatus.AVAILABLE)) {
                nearestAvailable = charger;
                distance = tempDist;
            }
        }
        if (nearestAvailable == null) {
            logger.debug("没有可用的充电桩！robotId=" + robotId);
            Map reMap = new HashMap();
            reMap.put("reCode", "0");
            reMap.put("reMsg", "没有可用的充电桩！robotId=" + robotId);
            return JsonUtils.map2Json(reMap);
        }
        ChargerDriveOrder order = new ChargerDriveOrder();
        order.setCharger(nearestAvailable);
        order.setType("ChargerDrive");
        order.setRobot(robot);
        robot.setCurOrder(order);
        CommonUtils.genUselessInfo(order.getKv());
        order.addKV("SECTION_ID", robot.getSectionId())
                .addKV("WAREHOUSE_ID", robot.getWareHouseId())
                .addKV("CHARGER_ID", nearestAvailable.getChargerId())
                .addKV("TRIP_STATE", TripStatus.AVAILABLE)
                .addKV("ID", CommonUtils.genUUID())
                .addKV("VERSION", 0)
                .addKV("TRIP_TYPE", order.getType());
        this.jdbcRepository.asynPersist(order);
        this.resendOrder(robot.getSectionId(), robotId, "false");
        return "小车robotId=" + robotId + " 去充电桩:" + nearestAvailable.getName();
    }

    @Scheduled(fixedDelay = 60000l)
    /**
     * 未知原因小车状态为不可用 长时间不动超过五分钟 一分钟查一次 TODO
     */
    //@Scheduled(fixedDelay = 60000l)
    public void restoreRobotStatus() {
        Iterator<Robot> iterator = this.registRobots.values().iterator();
        while (iterator.hasNext()) {
            Robot robot = iterator.next();
            if (robot.getCurOrder() == null
                    && robot.getStatus() == Robot.IDLE
                    && !robot.isAvaliable()) {
                //bad smell 可能在获取下一个任务
                robot.setAvaliable(true);
            }
        }
    }

    public String changeTarget(String robotId) {
        Robot robot = this.registRobots.get(Long.parseLong(robotId));
        if (robot == null) {
            logger.debug("小车不存在！robotId=" + robotId);
            Map reMap = new HashMap();
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车不存在！robotId=" + robotId);
            return JsonUtils.map2Json(reMap);
        }
        if (robot.getCurOrder() == null) {
            logger.debug("小车没有任务！robotId=" + robotId);
            Map reMap = new HashMap();
            reMap.put("reCode", "0");
            reMap.put("reMsg", "小车没有任务！robotId=" + robotId);
        }
        Order order = robot.getCurOrder();
        Long endAddr = order.getEndAddr();
        Address end = this.wareHouseManager.getAddressByAddressCodeId(endAddr, robot.getSectionId());
        if (end != null) {
            end.robotLock(robot.getRobotId(), false);
        }
        if (canChangeTarget(order)) {
            order.setEndAddr(0L);
        }
        return "小车:" + robotId + "目的地已变成:" + order.getEndAddr();
    }

    /**
     * 可以修改目的地的几种类型
     *
     * @param order
     * @return
     */
    private boolean canChangeTarget(Order order) {
        boolean back2Storage = false;
        if (order instanceof StationPodOrder) {
            StationPodOrder stationPodOrder = (StationPodOrder) order;
            back2Storage = stationPodOrder.getIndex() == StationPodOrder.BUFFER2STORAGE;
        }
        return back2Storage || (order instanceof EmptyRunOrder)
                || (order instanceof PodRunOrder);
    }

    /**
     * @param sectionId
     * @param updateCost
     * @return
     * @Deperated
     */
    public String batchUpdateCosts(String sectionId, String updateCost) {
        Section section = this.wareHouseManager.getSectionById(sectionId);
        //100#100@2198#2199__-1#-1@2199#2198
        // 以__分开（两个下划线） @ 前面的是 cost值 #号之前是cost 后面是carryCost
        // 后面的是进出的两个节点 前面是in 后面是out

        if (CommonUtils.isEmpty(updateCost)) {
            return "updateCost为空";
        }

        StringBuilder stringBuilder = new StringBuilder();
        String[] costs = updateCost.split("__");
        for (int i = 0; i < costs.length; i++) {
            String cost = costs[i];
            String[] strings = cost.split("@");
            if (strings.length != 2) {
                logger.error(i + "定义有误:" + cost);
                continue;
            }
            String cost_carryCost = strings[0];
            String[] twoCost = cost_carryCost.split("#");
            if (twoCost.length != 2) {
                logger.error(i + "定义有误:" + cost);
                continue;
            }
            String newCost = twoCost[0];
            String newCarryCost = twoCost[1];

            String in_out = strings[1];
            String[] addrs = in_out.split("#");
            if (addrs.length != 2) {
                logger.error(i + "定义有误:" + cost);
                stringBuilder.append(i + "定义有误:" + cost);
                continue;
            }
            String in = addrs[0];
            String out = addrs[1];

            Address inAddr = this.wareHouseManager.getAddressByAddressCodeId(in, section);
            Address outAddr = this.wareHouseManager.getAddressByAddressCodeId(out, section);

            Map newValue = new HashMap();
            newValue.put("COST", Integer.parseInt(newCost));
            newValue.put("CARRYINGCOST", Integer.parseInt(newCarryCost));

            Map con = new HashMap();
            con.put("IN_ID", inAddr.getPkId());
            con.put("OUT_ID", outAddr.getPkId());

            this.jdbcRepository.updateRecords("WD_NEIGHBOR", newValue, con);

            stringBuilder.append("从" + in + "到" + out + "的Cost值修改为:" + newCost + " CarryCost值修改为:" + newCarryCost).append("\n");
        }


        return stringBuilder.toString();
    }


    public String batchUpdateCostByInOut(String sectionId, String followCells, String allCells) {
        //单向行驶点 都是单方向1 反方向-1 其他方向都是-1
        //出口单方向-1 其他方向不可走
        //旋转区入口和不旋转入口 入口-1 出口1 其他1
        //与区域外的节点 如果不是出口入口 都是不可走 -1
        //1234#5678#3456,1234#5678#3456 逗号分开
        String[] follows = followCells.split(",");
        //1234#2341 ,说明1234其他方向不可出
        //String[] in_out = exit.split("#");
        //第一个是非旋转的 第二个是旋转的
        //1234#2342,1234#2343
        //String[] entrys = enters.split(",");
        //allCells 除进出口外 都是封闭的
        //第一步 将所有节点的进出都设置成-1 IN_ID=? OR OUT_ID=?
        String[] cells = allCells.split("#");
        for (int i = 0; i < cells.length; i++) {
            String cell = cells[i];
            Address address = this.getAddressById(cell, sectionId);
            this.jdbcRepository.updateBySql(UPDATE_NODE_COST, address.getPkId(), address.getPkId());
        }
        //第二步 处理排队的，可能是多个排队的 如 旋转区排队 不旋转区排队
        //List<Map> nodes = new ArrayList<>();
        for (int i = 0; i < follows.length; i++) {

            String follow = follows[i];
            String[] fs = follow.split("#");
            for (int j = 0; j < fs.length; j++) {
                String cell = fs[j];
                if (i < fs.length - 1) {//最后一个是不生成的
                    updateCostByInOut(sectionId, fs[i + 1], cell);
                }
            }
        }


        Map data = new HashMap();
        data.put("allCells", allCells);
        data.put("followCells", followCells);
        return JsonUtils.map2Json(data);
    }

    public void updateCostByInOut(String sectionId, String oCode, String iCode) {
        Map node = new HashMap();
        node.put("IN", iCode);
        node.put("OUT", oCode);
        Address in = this.getAddressById(iCode, sectionId);
        Address out = this.getAddressById(oCode, sectionId);
        Map newValue = new HashMap();
        newValue.put("COST", 1);
        newValue.put("CARRYINGCOST", 1);
        Map con = new HashMap();
        con.put("IN_ID", in.getPkId());
        con.put("OUT_ID", out.getPkId());
        this.jdbcRepository.updateRecords("WD_NEIGHBOR", newValue, con);
    }

    private static final String UPDATE_NODE_COST =
            "UPDATE WD_NEIGHBOR SET COST=-1,CARRYINGCOST=-1 " +
                    "WHERE IN_ID=? OR OUT_ID=?";


    public void testRobotLock(String sectionId, String robotId, String podId) {
        Section section = this.wareHouseManager.getSectionById(sectionId);
        Robot robot = this.getRobotById(Long.parseLong(robotId));
        Pod pod = this.podManager.getPodByRcsPodId(Long.parseLong(podId), section);
        robot.setPod(pod);
        pod.setRobot(robot);
        int i = 0;
        String[] addrs = {"47","49"};
        while (i < 100) {
            String target = addrs[i%addrs.length];
            if (CommonUtils.isEmpty(target)) {
                logger.error("未找到目标地址:" + robotId + " podId:" + podId);
                continue;
            }
            Address address = this.wareHouseManager.getAddressByAddressCodeId(target, section);
            if (address != null) {
                boolean success = address.robotLock(robot.getRobotId(), true);
                if (success) {
                    CommonUtils.sleep(10L);
                    address.robotLock(robot.getRobotId(), false);
                }else{
                    CommonUtils.sleep(10L);
                }
            }
            i++;
        }
    }

    /**
     * 显示该货架的角度 工作站的角度 以及该货架到工作站是否转面及角度
     *
     * @param sectionId
     * @param workstationId
     * @param podId
     * @param face
     * @return
     */
    public String showPodFace2Workstation(String sectionId, String workstationId, String podId, String face) {
        Section section = this.wareHouseManager.getSectionById(sectionId);
        Pod pod = this.podManager.getPodByRcsPodId(Long.parseLong(podId), section);
        //Integer arch = pod.getDirect();
        WorkStation workStation = section.workStationMap.get(workstationId);
        Integer arch = CommonUtils.aFaceToward(pod.getDirect(), workStation.getFace(), face);
        boolean gotoRotate = (pod.getDirect() != (arch + CommonUtils.podCorrectTheta) % 360);
        Map data = new LinkedHashMap();
        data.put("pod.getDirect(): ", pod.getDirect());
        data.put("workStation.getFace: ", workStation.getFace());
        data.put("Need"+face + " TurnFace:", gotoRotate+" arch:"+arch);
        return JsonUtils.map2Json(data);
    }
}
