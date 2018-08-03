package com.mushiny.business;

import com.mushiny.beans.*;
import com.mushiny.beans.enums.TripStatus;
import com.mushiny.beans.enums.OrderPositionStatus;
import com.mushiny.beans.enums.TripType;
import com.mushiny.beans.order.*;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import com.mushiny.jdbc.repositories.JdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 调度单的加载分三部分
 * 1、启动时自动分配
 * 2、定时任务从数据库里重新加载，加载后存内存里
 * 3、为防止小车在计算过程中没有任务导致空闲，会将下一个任务赋予小车，等待执行
 * 4、空车闲置时随心跳包加载任务
 * 5、正在工作的小车一旦完成自动获取下一个任务执行
 * 6、小车状态转换成非正常（工作或空闲）时，当前任务和下一个任务退回，当前数据库绑定该小车的调度单cancel掉
 * Created by Tank.li on 2017/7/23.
 */
@Component
@org.springframework.core.annotation.Order(value = 5)
public class OrderManager implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(OrderManager.class);


    //public Map<String,OrderGroup> orderGroupMap = new ConcurrentHashMap<>();
    @Autowired
    private JdbcRepository jdbcRepository;
    @Autowired
    private WareHouseManager wareHouseManager;
    @Autowired
    private RobotManager robotManager;
    @Autowired
    private PodManager podManager;

    @Override
    public void run(String... strings) throws Exception {
        //startFetchOrders();
        logger.debug("调度单管理器启动成功!");
    }

    //处理异常的代码先不考虑

    /**
     * 存储调度单到数据库 同时更新内存 少了TRIP_TYPE，不知道什么意思
     * @param order
     */
    @Transactional
    public void saveOrderGroup(Order order) {
        order.addKV("SECTION_ID", order.getSectionId())
                .addKV("WAREHOUSE_ID", order.getWareHouseId())
                .addKV("END_ADDRESS", order.getEndAddr())
                //.addKV("CHARGER_ID", order.getChargerId())
                //.addKV("WORKSTATION_ID", order.getWorkStationId())
                //.addKV("DRIVE_ID", order.getRobot().getRobotId())
                .addKV("TRIP_STATE", order.getOrderStatus())
                .addKV("ID", order.getOrderId())
                .addKV("VERSION", 0)
                .addKV("TRIP_TYPE", order.getType());
        if(order.getRobot()!=null){
           order.addKV("DRIVE_ID", order.getRobot().getRobotId());
        }

        if(order.getPod()!=null){
            order.addKV("POD_ID", order.getPod().getPodId());
        }

        this.jdbcRepository.insertBusinessObject(order);
    }
    private static final Object locker = new Object();
    public boolean finishOrder(Order curOrder, Robot robot) {
        synchronized (locker) {
            if(curOrder == null || curOrder.isFinished()){
                //可能存在两个order关联一个robot
                return false;
            }

            if(robot.getCurOrder()!=null && !Objects.equals(curOrder,robot.getCurOrder())){
               curOrder.finish();
               curOrder.setFinished(true);
               return false;//因为已经不同了，不要再次获取路径
            }

            if(!Objects.equals(curOrder.getEndAddr()+"",robot.getAddressId())){
               logger.debug("小车:"+curOrder.getRobot()+"未到调度单终点");
                return false;
            }
            curOrder.finish();
            curOrder.setFinished(true);
            return true;
        }
    }

    /**
     * 生成从工作站到buffer再到storage的调度单与明细
     * 1、判断三个位置是否正确匹配，不匹配表示不一致
     * 2、生成调度单和两个明细
     * 3、返回调度单
     * @param robot
     * @param release
     * @param workStation
     * @return
     */
    public Order drivePodFromStation2Storage(Robot robot, Pod release, WorkStation workStation) {
        if(!robot.getAddressId().equals(release.getAddress().getId())
                ||!robot.getAddressId().equals(workStation.getStopPoint())){
             logger.error("调度单无法生成，因为小车、工作站或POD的位置不一致!");
             return null;
        }

        return null;
    }

    public Order getOrderByRobot(Robot robot) {
        List<Map> rows = null;

        if(!CommonUtils.isEmpty(robot.getLastOrderId())){
            /*SELECT RCS_TRIP.*,RCS_TRIPPOSITION.POD_USING_FACE,RCS_TRIPPOSITION.POSITION_NO
            FROM RCS_TRIP LEFT JOIN RCS_TRIPPOSITION ON RCS_TRIP.`ID` = RCS_TRIPPOSITION.`TRIP_ID`
                    WHERE TRIPPOSITION_STATE=? AND RCS_TRIP.DRIVE_ID=? AND RCS_TRIP.ID=? AND POSITION_NO NOT IN (-1,-2)
                    ORDER BY POSITION_NO LIMIT 1*/
            //检查是否还有除了-1-2的调度单明细之外的明细
            rows = this.jdbcRepository.queryByKey("OrderManager.getSameOrderByRobot",
                    OrderPositionStatus.Available.getType(),robot.getRobotId(),robot.getLastOrderId());
            if (rows != null && rows.size() != 0) {
                Map map = rows.get(0);//取第一条
                Order order = genOrder(robot, map);
                //robot.setLastOrderId(order.getOrderId());//更新
                if(order instanceof StationPodOrder){
                    StationPodOrder stationPodOrder = (StationPodOrder) order;
                    robot.setOrderIndex(stationPodOrder.getIndex());
                }
                return order;
            } else {
                logger.debug("根据上一个调度单：orderID:"+robot.getLastOrderId()+" Robot:"+robot.getRobotId()+"没有取到同组可执行的调度单!");
                //检查是否有-1和-2的 不要管状态啦 如果index大于等于0 取-1的 如果index=-1 取 -2
                /*SELECT RCS_TRIP.*,RCS_TRIPPOSITION.POD_USING_FACE,RCS_TRIPPOSITION.POSITION_NO
                    FROM RCS_TRIP LEFT JOIN RCS_TRIPPOSITION ON RCS_TRIP.`ID` = RCS_TRIPPOSITION.`TRIP_ID`
                    WHERE RCS_TRIP.DRIVE_ID=? AND RCS_TRIP.ID=? AND POSITION_NO =?
                    ORDER BY POSITION_NO DESC*/
                int index = robot.getOrderIndex();//上一次任务
                if(index >= 0){
                    index = Order.STATION2BUFFER;//获取返回到缓冲点的任务
                }else if(index == Order.STATION2BUFFER){
                    index = Order.BUFFER2STORAGE;//获取从缓冲点到存储区的任务
                }else{
                    index = -3;//不存在的序号
                }
                rows = this.jdbcRepository.queryByKey("OrderManager.getBack2StorageOrderByRobot",
                        robot.getRobotId(),robot.getLastOrderId(),index );
                if (rows != null && rows.size() != 0) {
                    Map map = rows.get(0);//取第一条
                    Order order = genOrder(robot, map);
                    if(order instanceof StationPodOrder){
                        StationPodOrder stationPodOrder = (StationPodOrder) order;
                        robot.setOrderIndex(stationPodOrder.getIndex());
                    }
                   return order;
                }

            }
        }
        logger.debug("Robot获取新调度单 Robot :"+robot.getRobotId());
         rows = this.jdbcRepository.queryByKey("OrderManager.getOrderByRobot",
                TripStatus.AVAILABLE, robot.getRobotId(), OrderPositionStatus.Available.getType());

        if(rows==null || rows.size()==0){
            robot.setLastOrderId(null); //没有新调度单就清空
            return null;
        }

        Map map = rows.get(0);//取第一条
        Order order = genOrder(robot, map);
        if (order != null) {
            robot.setLastOrderId(order.getOrderId());//更新
        }
        return order;
    }

    private Order genOrder(Robot robot, Map map) {
        Section section = this.wareHouseManager.getSectionById(robot.getSectionId());

        String type = CommonUtils.parseString("TRIP_TYPE", map);
        logger.debug("调度单类型:" + type);

        Order order = null;

        if(Objects.equals(type, TripType.CHARGER_DRIVE)){
            order = genChargerDriverOrder(map,section);
        }else if(Objects.equals(type, TripType.POD_RUN)){
            order = new PodRunOrder();
        }else if(Objects.equals(type, TripType.EMPTY_RUN)){
            order = new EmptyRunOrder();
        }else if(Objects.equals(type, TripType.STOW_POD)
                || Objects.equals(type, TripType.PICK_POD)){
            order = genStationPod(map,section);
        }else if(Objects.equals(type, TripType.IBP_POD)
                || Objects.equals(type, TripType.OBP_POD)
                || Objects.equals(type, TripType.ICQA_POD)){
            order = genPqaPod(map,section);
        }else if(Objects.equals(type, TripType.POD_SCAN)){
            order = genPodScanOrder(map);
        }else{
            logger.error("调度单类型:" + type+"未知!");
            return null;
        }
        String podId = CommonUtils.parseString("POD_ID", map);
        Pod pod = this.podManager.getPod(robot.getSectionId(),podId);
        logger.debug("新调度单的Pod是:"+pod);
        order.setPod(pod);
        order.setOrderId(CommonUtils.parseString("ID", map));
        order.setOrderStatus(CommonUtils.parseString("TRIP_STATE", map));
        order.setEndAddr(CommonUtils.parseLong("END_ADDRESS", map));
        order.setRobot(robot);
        order.setSectionId(CommonUtils.parseString("SECTION_ID", map));
        order.setWareHouseId(CommonUtils.parseString("WAREHOUSE_ID", map));
        order.setActivedBy(CommonUtils.parseString("ACTIVED_BY", map));
        order.setType(type);
        return order;
    }

    private Order genPqaPod(Map map, Section section) {
        PqaPodOrder order = new PqaPodOrder();
        //workstation 是逻辑还是物理？
        String workStationId = CommonUtils.parseString("WORKSTATION_ID", map);
        WorkStation workStation = section.workStationMap.get(workStationId);
        //其他信息
        order.setWorkStation(workStation);
        order.setIndex(CommonUtils.parseInteger("POSITION_NO", map));
        order.setUseFace(CommonUtils.parseString("POD_USING_FACE", map));
        order.setOrderId(CommonUtils.parseString("ID", map));
        String useFace = CommonUtils.parseString("POD_USING_FACE", map);
        //将同一面的其他子调度单都带上
        if (!CommonUtils.isEmpty(useFace)) {
            List<Map> positions = this.jdbcRepository.queryByKey("OrderManager.orderPositions",
                    order.getOrderId(),useFace);
            List<OrderPosition> orderPositions = new ArrayList<>();
            for (int i = 0; i < positions.size(); i++) {
                Map data =  positions.get(i);
                OrderPosition orderPosition = new OrderPosition();
                orderPosition.setPodId(CommonUtils.parseString("POD_ID", map));
                orderPosition.setOrderPositionId(CommonUtils.parseString("ID", data));
                orderPosition.setUseFace(CommonUtils.parseString("POD_USING_FACE", data));
                orderPosition.setIndex(CommonUtils.parseInteger("POSITION_NO", data));
                orderPositions.add(orderPosition);
            }
            order.setOrderPositions(orderPositions);
        }else if(order.getIndex() == Order.STATION2BUFFER
                || order.getIndex() == Order.BUFFER2STORAGE){
            List<Map> positions = this.jdbcRepository.queryByKey("OrderManager.orderPositionsByIndex",
                    order.getOrderId(),order.getIndex());
            List<OrderPosition> orderPositions = new ArrayList<>();
            for (int i = 0; i < positions.size(); i++) {
                Map data =  positions.get(i);
                OrderPosition orderPosition = new OrderPosition();
                orderPosition.setPodId(CommonUtils.parseString("POD_ID", map));
                orderPosition.setOrderPositionId(CommonUtils.parseString("ID", data));
                orderPosition.setUseFace(CommonUtils.parseString("POD_USING_FACE", data));
                orderPosition.setIndex(CommonUtils.parseInteger("POSITION_NO", data));
                orderPositions.add(orderPosition);
            }
            order.setOrderPositions(orderPositions);
        }
        return order;
    }

    private Order genPodScanOrder(Map map) {
        PodScanOrder podScanOrder = new PodScanOrder();
        String path = CommonUtils.parseString("PODSCANPATH",map);
        podScanOrder.setPath(JsonUtils.json2List(path,Long.class));
        return podScanOrder;
    }

    private Order genStationPod(Map map, Section section) {
        StationPodOrder order = new StationPodOrder();
        //workstation
        String workStationId = CommonUtils.parseString("WORKSTATION_ID", map);
        WorkStation workStation = section.workStationMap.get(workStationId);
        //其他信息
        order.setWorkStation(workStation);
        order.setIndex(CommonUtils.parseInteger("POSITION_NO", map));
        order.setUseFace(CommonUtils.parseString("POD_USING_FACE", map));
        order.setOrderId(CommonUtils.parseString("ID", map));
        String useFace = CommonUtils.parseString("POD_USING_FACE", map);
        //将同一面的其他子调度单都带上
        if (!CommonUtils.isEmpty(useFace)) {
            List<Map> positions = this.jdbcRepository.queryByKey("OrderManager.orderPositions",
                    order.getOrderId(),useFace);
            List<OrderPosition> orderPositions = new ArrayList<>();
            for (int i = 0; i < positions.size(); i++) {
                Map data =  positions.get(i);
                OrderPosition orderPosition = new OrderPosition();
                orderPosition.setPodId(CommonUtils.parseString("POD_ID", map));
                orderPosition.setOrderPositionId(CommonUtils.parseString("ID", data));
                orderPosition.setUseFace(CommonUtils.parseString("POD_USING_FACE", data));
                orderPosition.setIndex(CommonUtils.parseInteger("POSITION_NO", data));
                orderPositions.add(orderPosition);
            }
            order.setOrderPositions(orderPositions);
        }else if(order.getIndex() == Order.STATION2BUFFER
                || order.getIndex() == Order.BUFFER2STORAGE){
            List<Map> positions = this.jdbcRepository.queryByKey("OrderManager.orderPositionsByIndex",
                    order.getOrderId(),order.getIndex());
            List<OrderPosition> orderPositions = new ArrayList<>();
            for (int i = 0; i < positions.size(); i++) {
                Map data =  positions.get(i);
                OrderPosition orderPosition = new OrderPosition();
                orderPosition.setPodId(CommonUtils.parseString("POD_ID", map));
                orderPosition.setOrderPositionId(CommonUtils.parseString("ID", data));
                orderPosition.setUseFace(CommonUtils.parseString("POD_USING_FACE", data));
                orderPosition.setIndex(CommonUtils.parseInteger("POSITION_NO", data));
                orderPositions.add(orderPosition);
            }
            order.setOrderPositions(orderPositions);
        }
        return order;
    }

    private Order genChargerDriverOrder(Map map, Section section) {
        ChargerDriveOrder order =  new ChargerDriveOrder();
        //chargerID
        String chargerId = CommonUtils.parseString("CHARGER_ID",map);
        Charger charger = section.chargers.get(chargerId);
        order.setCharger(charger);
        return order;
    }

    public void cancelRobotOrders(Robot robot, Order order) {
        logger.debug("取消Robot:"+robot.getRobotId()+"的调度单: "+order.getOrderId());
        this.jdbcRepository.updateByKey("OrderManager.cancelRobotOrders",
                OrderPositionStatus.Not_Finished.getType(),robot.getRobotId(),order.getOrderId());
        this.jdbcRepository.updateByKey("OrderManager.cancelRobotOrderGroups",
                TripStatus.NOT_FINISHED,robot.getRobotId(),order.getOrderId());
    }

    public Order getWorkingOrderByRobot(Robot robot) {
        if (!CommonUtils.isEmpty(robot.getLastOrderId())){
            //根据当前车的上个调度单完成细节
            List<Map> rows = this.jdbcRepository.queryByKey("OrderManager.getWorkingOrderByRobot",
                    TripStatus.PROCESS, TripStatus.LEAVING, OrderPositionStatus.Process.getType(),
                    robot.getRobotId(),robot.getLastOrderId());
            if (rows != null && rows.size() != 0) {
                return genOrder(robot,rows.get(0));
            }
        }else{
            logger.debug("小车："+robot.getRobotId()+" 的LastOrderId为空，直接获取下一个任务！");
        }
        //如果没有可执行的任务，就查询下一个吧 哈哈哈哈
        return getOrderByRobot(robot);
    }

    public void resetOrder(Order order) {
        logger.debug("回退当前调度单和明细的状态,Order:"+order.getOrderId());
        order.addKV("TRIP_STATE",TripStatus.AVAILABLE);
        this.jdbcRepository.updateBusinessObject(order);
        if(order instanceof StationPodOrder){
            StationPodOrder stationPodOrder = (StationPodOrder) order;
            List<OrderPosition> orderPositions = stationPodOrder.getOrderPositions();
            for (int i = 0; (orderPositions!=null && i < orderPositions.size()); i++) {
                OrderPosition orderPosition = orderPositions.get(i);
                orderPosition.addKV("TRIPPOSITION_STATE",OrderPositionStatus.Available);
                this.jdbcRepository.updateBusinessObject(orderPosition);
            }
        }
        logger.debug("回退当前调度单和明细的状态,Order:"+order.getOrderId()+"结束!");
    }
    public static final String GET_ORDER_BYID = "SELECT RCS_TRIP.*,RCS_TRIPPOSITION.POD_USING_FACE,RCS_TRIPPOSITION.POSITION_NO \n" +
            "FROM RCS_TRIP,RCS_TRIPPOSITION WHERE RCS_TRIP.ID=RCS_TRIPPOSITION.TRIP_ID \n" +
            "AND (RCS_TRIP.ID=? OR RCS_TRIPPOSITION.ID=?) AND RCS_TRIP.DRIVE_ID=? ORDER BY POSITION_NO LIMIT 1";
    public Order getOrderById(Robot robot, String orderId) {
        List<Map> list = this.jdbcRepository.queryBySql(GET_ORDER_BYID,orderId,orderId,robot.getRobotId());
        if(list == null || list.size() ==0){
            return null;
        }
        Map data = list.get(0);
        return this.genOrder(robot,data);
    }
    public static final String GET_ORDER_BYFACE = "SELECT RCS_TRIP.*,RCS_TRIPPOSITION.POD_USING_FACE,RCS_TRIPPOSITION.POSITION_NO \n" +
            "FROM RCS_TRIP,RCS_TRIPPOSITION WHERE RCS_TRIP.ID=RCS_TRIPPOSITION.TRIP_ID \n" +
            "AND (RCS_TRIPPOSITION.POD_USING_FACE=?) AND (RCS_TRIP=TRIP_STATE=? OR RCS_TRIP=TRIP_STATE=?)" +
            "AND RCS_TRIP.DRIVE_ID=? ORDER BY RCS_TRIP.CREATED_DATE DESC, POSITION_NO LIMIT 1";

    public static final String GET_ORDER_BYINDEX = "SELECT RCS_TRIP.*,RCS_TRIPPOSITION.POD_USING_FACE,RCS_TRIPPOSITION.POSITION_NO \n" +
            "FROM RCS_TRIP,RCS_TRIPPOSITION WHERE RCS_TRIP.ID=RCS_TRIPPOSITION.TRIP_ID \n" +
            "AND (RCS_TRIPPOSITION.POSITION_NO=?) AND (RCS_TRIP=TRIP_STATE=? OR RCS_TRIP=TRIP_STATE=?) " +
            "AND RCS_TRIP.DRIVE_ID=? ORDER BY RCS_TRIP.CREATED_DATE DESC, POSITION_NO LIMIT 1";

    private static final java.lang.String GET_CURRENT_PROCESS_ORDER = "SELECT RCS_TRIP.*,RCS_TRIPPOSITION.POD_USING_FACE,RCS_TRIPPOSITION.POSITION_NO \n" +
            "FROM RCS_TRIP,RCS_TRIPPOSITION WHERE RCS_TRIP.ID=RCS_TRIPPOSITION.TRIP_ID \n" +
            "AND (RCS_TRIPPOSITION.TRIPPOSITION_STATE=?) AND (RCS_TRIP=TRIP_STATE=? OR RCS_TRIP=TRIP_STATE=?) " +
            "AND RCS_TRIP.DRIVE_ID=? ORDER BY RCS_TRIP.CREATED_DATE DESC, POSITION_NO LIMIT 1";

    public Order getOrderById_FaceOrIndex(Robot robot, String face, String index) {
        if(face!=null && "ABCD".contains(face)){
            List<Map> list = this.jdbcRepository.queryBySql(GET_ORDER_BYFACE, face ,
                    TripStatus.PROCESS,TripStatus.LEAVING, robot.getRobotId());
            if(list == null || list.size() ==0){
                return null;
            }
            Map data = list.get(0);
            return this.genOrder(robot,data);

        }else if(index!=null){
           Integer posno = Integer.parseInt(index);
           List<Map> list = this.jdbcRepository.queryBySql(GET_ORDER_BYINDEX, posno ,
                   TripStatus.PROCESS,TripStatus.LEAVING, robot.getRobotId());
            if(list == null || list.size() ==0){
                return null;
            }
            Map data = list.get(0);
            return this.genOrder(robot,data);

        }else{
            //获取最近的process
            List<Map> list = this.jdbcRepository.queryBySql(GET_CURRENT_PROCESS_ORDER,
                    OrderPositionStatus.Process, TripStatus.PROCESS,TripStatus.LEAVING, robot.getRobotId());
            if(list == null || list.size() ==0){
                return null;
            }
            Map data = list.get(0);
            return this.genOrder(robot,data);
        }
    }
    public static final String ROBOT_CURRENT_ORDER =
            "SELECT * FROM RCS_TRIP WHERE DRIVE_ID=? AND SECTION_ID=? AND TRIP_STATE=?";

    /**
     * 清空小车任务
     * 1、将当前任务设置为完成
     * 2、如果没有其他任务了 POD恢复可用 可派车 可调度
     * 3、清除ENROUTEPOD
     * @param robot
     */
    public void clearRobotOrder(Robot robot, String withPod) {
        List<Map> list = this.jdbcRepository.queryBySql(ROBOT_CURRENT_ORDER,robot.getRobotId(),robot.getSectionId(),TripStatus.PROCESS);
        if(list == null || list.isEmpty()){
            return;
        }
        Map row = list.get(0);
        String podId = CommonUtils.parseString("POD_ID",row);
        Pod pod = this.podManager.getPod(robot.getSectionId(),podId);

        BaseBpo baseBpo = new BaseBpo();
        baseBpo.setTable("RCS_TRIP");
        baseBpo.setId(robot.getLastOrderId());
        baseBpo.setIdName("ID");
        baseBpo.addKV("TRIP_STATE",TripStatus.FINISHED);
        this.jdbcRepository.updateBusinessObject(baseBpo);

        CommonUtils.sleep(2000L);

        if(pod !=null && this.podManager.noPodInAvailableTrip(robot.getCurOrder())){
            if(pod.getLockedBy()==robot.getRobotId()){
                pod.setLockedBy(0L);
            }
            if ("true".equals(withPod)){
                pod.addKV("STATE","Reserved");
                pod.addKV("PLACEMARK", 0);
            }else{
                pod.addKV("STATE","Available");//TODO 是否还有单子
            }
            this.jdbcRepository.updateBusinessObject(pod);
        }

        if (!(robot.getCurOrder() instanceof StationPodOrder)) {
            return;
        }
        StationPodOrder stationPodOrder = (StationPodOrder) robot.getCurOrder();
            /*  SELECT * FROM IB_ENROUTEPOD;
                SELECT * FROM OB_ENROUTEPOD;
                SELECT * FROM PQA_ENROUTEPOD;*/
        if(Objects.equals(stationPodOrder.getType(),TripType.STOW_POD)){
            stationPodOrder.unBindEnroutePod("IB_ENROUTEPOD",stationPodOrder.getPod().getPodId());
        }
        if(Objects.equals(stationPodOrder.getType(),TripType.PICK_POD)){
            stationPodOrder.unBindEnroutePod("OB_ENROUTEPOD",stationPodOrder.getPod().getPodId());
        }
        if(Objects.equals(stationPodOrder.getType(),TripType.IBP_POD)
                ||Objects.equals(stationPodOrder.getType(),TripType.OBP_POD)
                ||Objects.equals(stationPodOrder.getType(),TripType.ICQA_POD)){
            stationPodOrder.unBindEnroutePod("PQA_ENROUTEPOD",stationPodOrder.getPod().getPodId());
        }
    }
}
