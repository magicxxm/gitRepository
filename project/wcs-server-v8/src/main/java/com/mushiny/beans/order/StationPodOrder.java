package com.mushiny.beans.order;

import com.mushiny.beans.*;
import com.mushiny.beans.enums.*;
import com.mushiny.business.WebApiBusiness;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import com.mushiny.mq.ISender;
import com.mushiny.mq.MessageSender;
import org.apache.http.client.utils.DateUtils;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Tank.li on 2017/9/5.
 * 支持两种格式：pickPod stowpod
 */
public class StationPodOrder extends Order {

    private static final String STATIONPODORDER_POD_IN_TRIP = "SELECT 1 FROM RCS_TRIP WHERE POD_ID=? AND TRIP_STATE<>? LIMIT 1";
    private WorkStation workStation;

    private String useFace;

    private int index;

    private String type;

    private List<OrderPosition> orderPositions;//可以一次性把多个A/B/C/D面拣齐

    public List<OrderPosition> getOrderPositions() {
        return orderPositions;
    }

    public void setOrderPositions(List<OrderPosition> orderPositions) {
        this.orderPositions = orderPositions;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUseFace() {
        return useFace;
    }

    public void setUseFace(String useFace) {
        this.useFace = useFace;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

    @Override
    public void sendMessage2Rcs() {

        Map message = this.genWcsMessage();
        message.put("workStationId", this.getWorkStation().getStopPoint());
        message.put("type",this.getType());
        message.put("index",this.getIndex());
        message.put("useFace",this.getUseFace());

        MessageSender.sendMapMessage(message, ISender.WCS_RCS_AGV_SERIESPATH);
        this.setMessage(JsonUtils.map2Json(message));
        this.setSend2RcsTime(CommonUtils.formatDate(new Date(System.currentTimeMillis())));
    }

    private boolean checkSrcAddr() {
        if (this.wcsPath == null
                || this.wcsPath.getSeriesPath() == null
                || this.wcsPath.getSeriesPath().size() == 0) {
            return false;
        }
        return Objects.equals(this.wcsPath.getSeriesPath().get(0),
                Long.parseLong(this.getRobot().getAddressId()));
    }

    @Override
    public void initOrder() {
        if(getPod() == null){
            this.setOrderError(Order.ERROR_NOPOD);//没有该POD
            return;
        }
        logger.debug("初始化StationPodOrder:" + this.getOrderId() + " Robot:" + " index=" + this.getIndex()
                + " Robot:" + this.getRobot().getRobotId() + " pod:" + this.getPod().getPodId()
                + " POD_LOCKEDBY:" + this.getPod().getLockedBy());

        if(this.getPod().getLockedBy() != 0L
                && this.getPod().getLockedBy() != this.getRobot().getRobotId()){//未被释放
            this.setOrderError(Order.ERROR_POD);
            return;
        }

        boolean locked = this.getPod().lockPod(this);
        /*if (this.getPod().getLockedBy() != 0L
                && this.getPod().getLockedBy() != this.getRobot().getRobotId()) {//未被释放
            logger.error("POD:" + getPod().getPodId() + " 被车[" + this.getPod().getLockedBy() + "]锁定!");
            this.setOrderError(Order.ERROR_POD);
            return;
        }
        this.getPod().setLockedBy(this.getRobot().getRobotId());//TODO 并发*/
        if(!locked){
            logger.error("调度单锁定POD失败! RobotId:"+this.getRobot().getRobotId()
                    +"未锁定pod:"+this.getPod().getPodName()+ " 已 locked by: "+this.getPod().getLockedBy());
            this.setOrderError(Order.ERROR_POD);
            return;
        }

        //分四种情况：pod2station station2station station2buffer buffer2storage
        if (this.getIndex() < -2) {
            logger.error("调度单状态不对! Order.getIndex() : " + this.getIndex());
            return;
        }
        Boolean flag = carryPod(this.getRobot(),this.getPod());
        logger.debug("小车:"+this.getRobot().getRobotId()+"是否已经驮上该调度单的POD:"+this.getPod().getPodName()+" Flag==>"+flag);
        if (this.getIndex() == Order.POD2STATION) {
            if(CommonUtils.isEmpty(this.getUseFace())){
                this.setOrderError(Order.ERROR_POD_NONEEDFACE);
                return;
            }
            logger.debug("处理POD到工位");
            //生成pod到buffer 和 pod到存储区的调度单明细
            //createBack2Storage();
            initPod2Station(flag);
            if (this.getOrderError() == 0) {
                checkOrder();
            }
            return;
        }

        if (this.getIndex() > 0) {
            logger.debug("处理工位到工位");
            if(CommonUtils.isEmpty(this.getUseFace())){
                this.setOrderError(Order.ERROR_POD_NONEEDFACE);
                return;
            }
            //createBack2Storage();
            initStation2Station();
            if (this.getOrderError() == 0) {
                checkOrder();
            }
            return;
        }

        if (this.getIndex() == Order.STATION2BUFFER) {
            logger.debug("处理工位到缓冲点");
            initStation2Buffer();
            if (this.getOrderError() == 0) {
                checkOrder();
            }
            return;
        }

        if (this.getIndex() == Order.BUFFER2STORAGE) {
            logger.debug("处理缓冲点到存储区");
            initBuffer2Storage();
            if (this.getOrderError() == 0) {
                checkOrder();
            }
            return;
        }
        //logger.error("调度单状态不对! Order: " + this);
    }

    private void checkOrder() {
        logger.debug("生成小车路径是:" + this.wcsPath.getSeriesPath()
                + " robot.addr:" + this.getRobot().getAddressId());

        if (this.wcsPath.getSeriesPath() == null
                || this.wcsPath.getSeriesPath().size() == 0) {
            logger.error("调度单初始化失败，不能发送消息:" + this);
            this.setOrderError(Order.ERROR_NO_WCSPATH);
            unlockEndAddress();
            return;
        }

        if (!checkSrcAddr()) {
            logger.error("小车初始路径不在路径起点,可能是路径计算失败! Robot.getAddressId():" + getRobot().getAddressId()
                    + " this.wcsPath.getSeriesPath().get(0):" + this.wcsPath.getSeriesPath().get(0));
            this.setOrderError(Order.ERROR_START_POINT);
            unlockEndAddress();
            return;
        }
        //如果路径长度是1，且执行工作站到缓冲点（position_no=-1），则设置调度单错误码，此处需要修改明细-1为finish
        if (this.wcsPath.getSeriesPath().size()==1 && this.getIndex()==Order.STATION2BUFFER){
            logger.error("小车："+this.getRobot().getRobotId()+"执行工作站到缓冲点时，路径长度=1，直接finish");
            this.setOrderError(Order.ERROR_NO_WCSPATH);
            this.finish();
            return;
        }
    }

    private void initBuffer2Storage() {
        //绝大部分代码与calc_ROBOT_POD_STATION 一致，就是少了举升地址 不用旋转 直接取缓冲区
        Long endAddr = this.getEndAddr();
        Address endAddress = this.getWareHouseManager().getAddressByAddressCodeId(endAddr,this.getRobot().getSectionId());
        if (endAddress!=null && endAddress.getType()==AddressType.STORAGE //只能回存储区
                && this.lockEndAddr()/*endAddr>0 && targetAvailable(endAddr)
                && Objects.equals(this.getRobot().getLockedAddr(), endAddr)*/){
            //正常应该不会有目标地址，如果有那就是重算后的，重算后的必须是本车锁定的节点
            // 不能是lockedby==0的，否则就绕过了并发的检测
            logger.debug("小车"+this.getRobot().getRobotId()
                    +"初始化缓冲点到存储位时，目标地址不变："+endAddr);
        }else{
            //恢复原先锁定的节点
            this.unlockEndAddress();
            String destAddr = this.hotDestAddr();
            logger.debug("小车"+this.getRobot().getRobotId()
                    +"初始化缓冲点到存储位时，目标地址重新计算："+destAddr);
            //回仓库存储区不需要判断同组格子是否被占用 只在调pod到工位时产生
            if(CommonUtils.isEmpty(destAddr)){
                this.setOrderError(Order.ERROR_NO_HOTADDRESS);
                return;
            }
            this.setEndAddr(Long.parseLong(destAddr));
            endAddr = Long.parseLong(destAddr);
        }

        Address dest = this.getWareHouseManager().getAddressByAddressCodeId(endAddr, this.getSectionId());
        if(dest == null){
            this.setOrderError(Order.ERROR_NO_HOTADDRESS);
            return;
        }
        //dest.setNodeState(AddressStatus.RESERVED);//不参与计算了
        boolean lockAddr = dest.robotLock(this.getRobot().getRobotId(),true);
        if (!lockAddr) {
            logger.info("目标格子："+endAddr+"锁定失败！destAddr.getLockedBy():"+dest.getLockedBy());
            this.setOrderError(Order.ERROR_LOCK_ENDADDR);
            return;
        }
        //恢复原先锁定的节点 设置新的目标地址
        Long preLocked = this.getRobot().getLockedAddr();
        if(preLocked != 0L && !Objects.equals(preLocked,endAddr)){
            Address preLockedAddr= this.getWareHouseManager()
                    .getAddressByAddressCodeId(preLocked, this.getSectionId());
            logger.info("小车"+this.getRobot().getRobotId()+"的原目标地址:"
                    + preLocked + " 新目标地址:"+endAddr+"不相同!");
            if (preLockedAddr != null) {
                preLockedAddr.robotLock(this.getRobot().getRobotId(),false);
            }
            /*if(preLockedAddr.getLockedBy() == this.getRobot().getRobotId()){
                preLockedAddr.setLockedBy(0);
                preLockedAddr.setNodeState(AddressStatus.AVALIABLE);
            }*/
        }
        this.getRobot().setLockedAddr(endAddr);
        //结束设置小车新的目标地址
        //trip表设置回存储区的终点
        logger.debug("initBuffer2Storage更新：tripID="+this.getOrderId()+" , endAddress="+endAddr);
        boolean re = updateEndAddr2Database(this.getOrderId(), endAddr+"");
        if (re){
            logger.debug("initBuffer2Storage更新成功！tripID="+this.getOrderId()+" , endAddress="+endAddr);
        }else{
            logger.debug("initBuffer2Storage更新失败！tripID="+this.getOrderId()+" , endAddress="+endAddr);
        }
        //执行数据库锁定
        //this.getJdbcRepository().updateBusinessObject(dest);
        logger.debug("目标格子:" + endAddr + "已经被锁定!");

        List<Long> station2Rotate = this.getHeavyPath(this.getWareHouseId(), this.getSectionId(), this.getRobot().getAddressId(), endAddr+"");

        this.setEndAddr(endAddr);
        this.wcsPath.setSeriesPath(station2Rotate);
        this.wcsPath.setPodDownAddress(endAddr);
        //this.wcsPath.setPodDownAddress(endAddr);
        this.wcsPath.setPodUpAddress(Long.parseLong(this.getPod().getAddress().getId()));
    }

    private boolean targetAvailable(Long endAddr) {
        //产生这个方法的调用都是因为发生了重算
        String sectionId = this.getRobot().getSectionId();
        Address address = this.getWareHouseManager().getAddressByAddressCodeId(endAddr,sectionId);
        return address!=null && address.getNodeState().equals(AddressStatus.RESERVED)
                && noOuterOccupied(address) && noInnerEmpty(address) &&
                (address.getLockedBy() == this.getRobot().getRobotId());
    }
    //内部节点如果是空的 该节点不能用
    private boolean noInnerEmpty(Address address) {
        Address inner = address.getGroupInnerAddr();
        if(inner == null){
            return true;
        }
        return inner.getNodeState().equals(AddressStatus.OCCUPIED);
    }
    //外部地址被占 该节点不能用
    private boolean noOuterOccupied(Address address) {
        Address out = address.getGroupOutterAddr();
        if(out == null){
            return true;
        }
        return ! out.getNodeState().equals(AddressStatus.OCCUPIED);
    }

    private String hotDestAddr() {
        //return "421";
        return this.getPodManager().computeTargetAddress(this.getPod().getRcsPodId()+"", this.getSectionId());
    }

    private void initStation2Buffer() {
        //绝大部分代码与calc_ROBOT_POD_STATION 一致，就是少了举升地址 不用旋转 直接取缓冲区
        String bufferAddr = workStation.getBufferPoint();
        List<Long> station2Buffer = this.getHeavyPath(this.getWareHouseId(), this.getSectionId(), this.getRobot().getAddressId(), bufferAddr);
        this.wcsPath.setSeriesPath(station2Buffer);
        this.wcsPath.setPodDownAddress(0L);
        if (this.getPod() != null){
            this.wcsPath.setPodUpAddress(Long.parseLong(this.getPod().getAddress().getId()));
        }else{
            this.wcsPath.setPodUpAddress(0L);
        }
        this.setEndAddr(Long.parseLong(bufferAddr));
    }

    private void initStation2Station() {
        //工作站旋转模式
        /*if(Objects.equals(this.getWorkStation().getRotatePoint(),this.getWorkStation().getStopPoint())
                            && Objects.equals(this.getRobot().getAddressId(),this.getWorkStation().getRotatePoint())){

            List<Long> path = new ArrayList<>();
            path.add(Long.parseLong(this.getWorkStation().getRotatePoint()));
            this.getWcsPath().setSeriesPath(path);
            this.setEndAddr(Long.parseLong(this.getWorkStation().getRotatePoint()));
            this.getWcsPath().setPodUpAddress(Long.parseLong(this.getWorkStation().getRotatePoint()));
            String needFace = this.getUseFace();
            Integer workStationFace = this.getWorkStation().getFace();
            Integer podFace = this.getPod().getDirect();
            Integer theta = CommonUtils.aFaceToward(podFace,workStationFace,needFace);
            this.getWcsPath().setRotateTheta(theta);
            this.wcsPath.setRobotID(this.getRobot().getRobotId());
            *//*Section section = this.getWareHouseManager().getSectionById(getRobot().getSectionId());
            this.wcsPath.setSectionID(section.getSection_id());*//*
            return;
        }*/

        //绝大部分代码与calc_ROBOT_POD_STATION 一致，就是少了举升地址 获取固定的旋转区入口
        String destAddr = this.getWorkStation().getStopPoint();
        String face = this.getUseFace();//需要的朝向
        int podDirect = this.getPod().getDirect();//当前面
        //到达目标位置时A面朝向
        Integer arch = CommonUtils.aFaceToward(podDirect, this.workStation.getFace(), face);
        //this.getWebApiBusiness().getPodTurning2(this.getPod(), workStation, face);//this.getArch(face, podDirect, workStation.getFace());
        logger.debug("当前POD" + getPod().getPodName() + "的A面朝向:" + podDirect + " 工作站朝向:" + workStation.getFace() + " 需要的面:" + face + " 出旋转区后的A面朝向:" + arch);
        String selected = workStation.getStation2RotateAddr();
        String rotateInOutAddr = workStation.getRotateInOutAddr();
        workStation.inAddr2.getAndIncrement();//排队加1
        logger.debug("工作站" + workStation.getWorkStationId() + " 入口1排队数量是:" + workStation.inAddr1.get()
                + " 入口2排队数量是:" + workStation.inAddr2.get());
        List<Long> station2Rotate2Station = new ArrayList<Long>();
        //替换小车时，先计算小车到pod的路径
        Robot robot = this.getRobot();
        String wareHouseId = robot.getWareHouseId();
        String sectionId = robot.getSectionId();
        String robotAddr = robot.getAddressId();
        Pod pod = this.getPod();
        String podAddrId = pod.getAddress().getId();

        if (this.getIsExchangeRobot()==1 || (!podAddrId.equals(robotAddr) && !carryPod(robot,pod))){
            //空车到POD位置
            station2Rotate2Station = this.getEmptyPath(wareHouseId, sectionId, robotAddr, podAddrId);
            if (station2Rotate2Station == null || station2Rotate2Station.size() == 0) {
                this.setOrderError(Order.ERROR_EMPTY_PATH);
                return;
            }
            //从pod地址到旋转区入口
            List<Long> pod2Rotate = this.getHeavyPath(this.getWareHouseId(), this.getSectionId(), podAddrId, selected);
            add2Path(station2Rotate2Station, pod2Rotate);
            //旋转区出口到工作站
            List<Long> rotate2Station = this.getHeavyPath(this.getWareHouseId(), this.getSectionId(), rotateInOutAddr, destAddr);
            add2Path(station2Rotate2Station, rotate2Station);
        }else{
            //从当前地址到旋转区入口
            station2Rotate2Station = this.getHeavyPath(this.getWareHouseId(), this.getSectionId(), this.getRobot().getAddressId(), selected);
            //旋转区出口到工作站
            List<Long> rotate2Station = this.getHeavyPath(this.getWareHouseId(), this.getSectionId(), rotateInOutAddr, destAddr);
            add2Path(station2Rotate2Station, rotate2Station);
        }
        this.wcsPath.setRotatePod(true);//这个true或false不是说转不转 而是回不回到旋转区

        WebApiBusiness.logPath(station2Rotate2Station,"全");
        //无论有没有驮起 都发举升地址
        this.wcsPath.setPodUpAddress(Long.parseLong(this.getPod().getAddress().getId()));
        this.wcsPath.setPodDownAddress(0L);
        this.wcsPath.setSeriesPath(station2Rotate2Station);
        this.wcsPath.setRotateTheta(arch);

        this.setEndAddr(Long.parseLong(destAddr));
    }


    //异步save到数据库
    private void createBack2Storage() {
        //检查是否存在
        List<Map> rows = this.getJdbcRepository().queryByKey("StationPodOrder.checkExistBack2Storage", this.getOrderId());
        if (rows != null && rows.size() == 2) {//TODO
            //已经存在
            return;
        }

        if (rows != null && rows.size()!=2 && rows.size()!=0) {
            this.getJdbcRepository().updateByKey("StationPodOrder.deletePositions", this.getOrderId());
        }
        logger.debug("为调度单:"+this.getOrderId()+"生成返回存储区的任务明细");

        //回缓冲区为-1 缓冲区到storage为-2
        Map station2Buffer = new HashMap();
        station2Buffer.put("ID", CommonUtils.genUUID());
        station2Buffer.put("TRIPPOSITION_STATE", OrderPositionStatus.Available.getType());
        station2Buffer.put("TRIP_ID", this.getOrderId());
        station2Buffer.put("POSITION_NO", -1);
        station2Buffer.put("CREATED_DATE", new Timestamp(System.currentTimeMillis()));
        station2Buffer.put("CREATED_BY", "System");
        station2Buffer.put("SECTION_ID", this.getSectionId());
        station2Buffer.put("WAREHOUSE_ID", this.getWareHouseId());
        station2Buffer.put("VERSION", 0);
        //this.getJdbcRepository().insertRecord("RCS_TRIPPOSITION", station2Buffer);
        //this.getJdbcRepository().insertBusinessObject();
        OrderPosition orderPosition = new OrderPosition();
        orderPosition.setKv(station2Buffer);
        this.getJdbcRepository().asynPersist(orderPosition);

        station2Buffer.put("POSITION_NO", -2);
        station2Buffer.put("ID", CommonUtils.genUUID());//新生成一个ID
        //this.getJdbcRepository().insertRecord("RCS_TRIPPOSITION", station2Buffer);
        orderPosition.setKv(station2Buffer);
        this.getJdbcRepository().asynPersist(orderPosition);
    }

    private void initPod2Station(Boolean flag) {

        Address podAddr = this.getPod().getAddress();
        //检查是否在深层货架中驮起, 如果外层没生成podRun 产生一个
        if(checkInnerAddr(podAddr)){
            //判断外层货架是否为空闲
            Address out = podAddr.getGroupOutterAddr();
            Pod outPod = this.getPodManager().getPodByAddress(out.getId(), out.getSectionId());
            logger.debug("当前调度单的外层货架 pod:" + outPod);
            if(outPod != null){
                List list = this.getJdbcRepository().queryBySql(STATIONPODORDER_POD_IN_TRIP,
                        outPod.getPodId(),TripStatus.FINISHED);
                if (list == null || list.size() == 0) {
                    logger.debug("当前调度单不可执行! 外层货架 podName:" + outPod.getPodName()+"没有分配调度任务!");
                    this.createPodRun(outPod);
                    logger.debug("为外层货架 podName:" + outPod.getPodName()+"生成调度任务PodRun! 当前任务等待下次循环...");
                    this.setOrderError(Order.ERROR_POD_ONPATH);
                    return;
                }
            }
        }
        //外层地址被lock 不可执行
        if(outterAddressLocked(podAddr)){
            logger.error("调度任务"+this.getOrderId()+"货架所在地址格"+podAddr.getId()+"的外层存储位被小车锁定" +
                    "，此时不能执行调度!");
            this.setOrderError(Order.ERROR_POD_ONPATH);
            return;
        }

        String destAddr = this.workStation.getStopPoint();
        Robot robot = this.getRobot();
        String sectionId = robot.getSectionId();
        String wareHouseId = robot.getWareHouseId();

        String robotAddr = robot.getAddressId();
        String podAddrId = podAddr.getId();
        //TODO 如果发现pod地址码跟起点不一样，要加上小车到Pod的计算路径 是空车路径 加到重车路径前面
        List<Long> robot2Pod2Station = new ArrayList<>();//默认列表是空的 小车起点跟POD一致
        if (!podAddrId.equals(robotAddr) && !flag) {
            //空车到POD位置
            robot2Pod2Station = this.getEmptyPath(wareHouseId, sectionId, robotAddr, podAddrId);
            if (robot2Pod2Station == null || robot2Pod2Station.size() == 0) {
                this.setOrderError(Order.ERROR_EMPTY_PATH);
                return;
            }
        }
        String face = this.getUseFace();//需要的朝向
        //发现没有面 可能是没有生成调度单
        if (CommonUtils.isEmpty(face)) {
            this.setOrderError(Order.ERROR_POD_NONEEDFACE);
            return;
        }
        int podDirect = this.getPod().getDirect();//pod
        Integer arch = CommonUtils.aFaceToward(podDirect, workStation.getFace(), face);
        //this.getWebApiBusiness().getPodTurning2(this.getPod(), workStation, face);//this.getArch(face, podDirect, workStation.getFace());
        logger.debug("当前POD的地址是:" + this.getPod().getAddress().getId() + " POD名称是:" + this.getPod().getPodName() + "的A面朝向:" + podDirect + " 工作站朝向:" + workStation.getFace() + " 需要的面:" + face + " 出旋转区后的A面朝向:" + arch);
        //加上是否已经到了工作站 如果到了 还必须再绕回来 不然结束不了
        if (podDirect != (arch+ CommonUtils.podCorrectTheta)%360/*|| workStation.getRotatePoint() != null*/
                || Objects.equals(robot.getAddressId(),workStation.getStopPoint())) { //当前A面朝向跟最终A面朝向不同 表示要旋转
            List<String> rotateInAddrs = workStation.rotateOutInAddrs;//this.getWareHouseManager().getRotateInAddrs(workStation.getWorkStationId(), sectionId);
            //TODO 先随机选一个
            String selected = selectInAddr(rotateInAddrs, workStation);
            String out = workStation.getRotateInOutAddr();
            //pod到旋转区入口
            List<Long> pod2RotateInAddr = this.getHeavyPath(robot.getWareHouseId(), this.getSectionId(), this.getPod().getAddress().getId(), selected);
            if (pod2RotateInAddr == null || pod2RotateInAddr.size() == 0) {
                this.setOrderError(Order.ERROR_HEAVY_PATH);
                return;
            }
            add2Path(robot2Pod2Station, pod2RotateInAddr);
            //TODO 入口到旋转出口
            List<Long> rIn2ROut = this.getHeavyPath(robot.getWareHouseId(), this.getSectionId(), selected, out);

            add2Path(robot2Pod2Station, rIn2ROut);

            //旋转区出口到工作站
            List<Long> rotate2Station = this.getHeavyPath(robot.getWareHouseId(), this.getSectionId(), out, destAddr);
            if (rotate2Station == null || rotate2Station.size() == 0) {
                this.setOrderError(Order.ERROR_HEAVY_PATH);
                return;
            }
            add2Path(robot2Pod2Station, rotate2Station);
            //logPath(robot2Pod);
            this.wcsPath.setRotatePod(true);
            this.wcsPath.setRotateTheta(arch);
        } else {//不到旋转区
            //pod到工作站
            List<Long> pod2Staion = this.getHeavyPath(robot.getWareHouseId(), this.getSectionId(), this.getPod().getAddress().getId(), destAddr);
            if (pod2Staion == null || pod2Staion.size() == 0) {
                this.setOrderError(Order.ERROR_HEAVY_PATH);
                return;
            }
            add2Path(robot2Pod2Station, pod2Staion);
            this.wcsPath.setRotateTheta(arch);//还是要加上角度，如果一样 pod不会转
            this.wcsPath.setRotatePod(false);
        }
        this.wcsPath.setSeriesPath(robot2Pod2Station);
        //order 的pod上升地址 取POD的实际地址，调度单的没用 TODO
        String upAddr = this.getPod().getAddress().getId();
        //无论有没有驮起 都发举升地址
        this.wcsPath.setPodUpAddress(Long.parseLong(upAddr));
        this.wcsPath.setPodDownAddress(0L);

        this.setEndAddr(Long.parseLong(destAddr));
    }

    private boolean outterAddressLocked(Address podAddr) {
        Address out = podAddr.getGroupOutterAddr();
        if(out == null){
            return false;
        }
        return out.getLockedBy() != 0L;
    }


    private String selectInAddr(List<String> rotateInAddrs, WorkStation workStation) {
        int i = workStation.inAddr1.get() <= workStation.inAddr2.get() ? 0 : 1;
        String select = rotateInAddrs.get(i);
        if (i == 0) {
            workStation.inAddr1.getAndIncrement();
        } else {
            workStation.inAddr2.getAndIncrement();
        }
        logger.debug("工作站" + workStation.getWorkStationId() + " 入口1排队数量是:" + workStation.inAddr1.get()
                + " 入口2排队数量是:" + workStation.inAddr2.get());
        return select;
    }


    @Override
    public void process() {
        if (this.getOrderError() > 0) {
            logger.error("调度单初始化失败，不能修改状态:" + this);
            this.setFlag(false);
            this.getRobot().setCurOrder(null);//小车不走
            //this.getRobot().setLastOrderId(null);
            this.getRobot().setAvaliable(true);
            this.getRobot().setStatus(Robot.IDLE);
            return;
        }
        logger.debug("正在执行:StationPodOrder.process: " + this);
        this.getRobot().setLastOrderId(this.getOrderId());
        this.addKV("TRIP_STATE", TripStatus.PROCESS);
        this.getJdbcRepository().updateBusinessObject(this);
        //更新明细状态
        updateTripPostions(OrderPositionStatus.Process.getType());
        //更新POD的目标位置
        updatePodTarget();
        //如果POD属于深层货架里头，将同组外面的节点Reserved掉
        updatePodOutterAddr();
        this.setFlag(true);
        //放到这里生成返回的调度单  异步save到数据库
        createBack2Storage();
        logger.debug("调度单:" + this.getOrderId() + " 状态修改完毕");
    }

    @Override
    public void finish() {
        logger.debug("StationPodOrder.finish::" + this);
        if (this.getIndex() == Order.BUFFER2STORAGE) {
            this.addKV("TRIP_STATE", TripStatus.FINISHED);
            this.getJdbcRepository().updateBusinessObject(this);
            if (this.getPod() != null) {
                logger.debug("释放调度单的POD:" + this.getPod().getPodName());
                this.getPodManager().finishMainOrder(this);
            }
        }

        /*if (this.getIndex() == Order.STATION2BUFFER
                && Objects.equals(this.getType(), TripType.STOW_POD)) {
            this.addKV("TRIP_STATE", TripStatus.LEAVING);
            this.getJdbcRepository().updateBusinessObject(this);

            //IB_ENROUTEPOD
            unBindEnroutePod("IB_ENROUTEPOD",this.getPod().getPodId());
        }

        if (this.getIndex() == Order.STATION2BUFFER && this.getPod() != null
                && Objects.equals(this.getType(), TripType.PICK_POD)) {
            this.addKV("TRIP_STATE", TripStatus.LEAVING);
            this.getJdbcRepository().updateBusinessObject(this);
            //TODO 沈玮的逻辑
            unBindEnroutePod("OB_ENROUTEPOD",this.getPod().getPodId());
        }*/
        if (this.getIndex() == Order.STATION2BUFFER) {
            this.addKV("TRIP_STATE", TripStatus.LEAVING);
            this.getJdbcRepository().updateBusinessObject(this);
            if (Objects.equals(this.getType(), TripType.STOW_POD)){
                //IB_ENROUTEPOD
                unBindEnroutePod("IB_ENROUTEPOD",this.getPod().getPodId());
            }else if (Objects.equals(this.getType(), TripType.PICK_POD)){
                unBindEnroutePod("OB_ENROUTEPOD",this.getPod().getPodId());
            }else if (Objects.equals(this.getType(), TripType.IBP_POD)
                    || Objects.equals(this.getType(), TripType.ICQA_POD)
                    || Objects.equals(this.getType(), TripType.OBP_POD)){
                unBindEnroutePod("PQA_ENROUTEPOD",this.getPod().getPodId());
            }

        }
        //更新明细状态
        updateTripPostions(OrderPositionStatus.Finished.getType());

        logger.debug("StationPodOrder.finish::完成结束状态");
    }

    public void unBindEnroutePod(String tableName, String podId) {
        EnroutePod enroutePod = new EnroutePod();
        enroutePod.setTable(tableName);
        Map<String,Object> con = new HashMap();
        con.put("POD_ID", podId);
        enroutePod.setDelCon(con);
        this.getJdbcRepository().deleteBo(enroutePod);
        logger.info(tableName+":表记录删除成功!:POD_ID=" + getPod().getPodId());
    }
    public static final String SQL_TRIPPOSITION_FACE = "SELECT ID FROM RCS_TRIPPOSITION WHERE TRIP_ID=? AND POD_USING_FACE=?";
    public static final String SQL_TRIPPOSITION_INDEX = "SELECT ID FROM RCS_TRIPPOSITION WHERE TRIP_ID=? AND POSITION_NO=?";
    private void updateTripPostions(String type) {
        if (this.getOrderPositions() != null && this.getOrderPositions().size() > 0) {
            OrderPosition orderPosition = orderPositions.get(0);
            if (this.getIndex()>-1) {
                /*Map con = new HashMap();
                con.put("POD_USING_FACE", orderPosition.getUseFace());
                con.put("TRIP_ID", this.getOrderId());
                orderPosition.setCon(con);
                orderPosition.addKV("TRIPPOSITION_STATE", type);*/
                //先查出记录 再按主键
                List<Map> rows = this.getJdbcRepository().queryBySql(SQL_TRIPPOSITION_FACE, this.getOrderId(),orderPosition.getUseFace());
                updateTripPosition(type, rows);
                //this.getJdbcRepository().updateRecords("RCS_TRIPPOSITION", value, con);

            } else if(this.getIndex() == Order.STATION2BUFFER || this.getIndex() == Order.BUFFER2STORAGE){
                /*Map con = new HashMap();
                con.put("POSITION_NO", this.getIndex());
                con.put("TRIP_ID", this.getOrderId());
                orderPosition.setCon(con);
                orderPosition.addKV("TRIPPOSITION_STATE", type);
                //this.getJdbcRepository().updateRecords("RCS_TRIPPOSITION", value, con);
                this.getJdbcRepository().updateBusinessObject(orderPosition);*/
                List<Map> rows = this.getJdbcRepository().queryBySql(SQL_TRIPPOSITION_INDEX, this.getOrderId(),orderPosition.getIndex());
                updateTripPosition(type, rows);
            }else{
                logger.error("Order:"+this.getOrderId()+"的明细单的POSITION_NO值有误:"+this.getIndex());
            }
        }else{
            logger.error("Order:"+this.getOrderId()+" 类型:"+this.getType()+"的明细单的数量为空或者不等于1");
        }
    }

    private void updateTripPosition(String type, List<Map> rows) {
        for (int i = 0; i < rows.size(); i++) {
            Map row = rows.get(i);
            OrderPosition op = new OrderPosition();
            op.setOrderPositionId(CommonUtils.parseString("ID",row));
            op.addKV("TRIPPOSITION_STATE", type);
            this.getJdbcRepository().updateBusinessObject(op);
        }
    }

    @Override
    public boolean isFinish() {
        if (this.getIndex() == Order.STATION2BUFFER || this.getIndex() == Order.BUFFER2STORAGE) {
            //到达位置就算结束
            return Objects.equals(this.getEndAddr(), Long.parseLong(getRobot().getAddressId()));
        } else {
            logger.debug("是否到达工作站? this.getEndAddr()" + this.getEndAddr()
                    +" getRobot().getAddressId():" + getRobot().getAddressId());
            if (Objects.equals(this.getEndAddr(), Long.parseLong(getRobot().getAddressId()))) {
                logger.debug("到达工作站的调度单需要手动结束!");
                //增加标志位
                this.setCanFinish(true);
            }
            return false;
        }

    }

    @Override
    public String toString() {
        return "StationPodOrder{" +
                "orderId=" + this.getOrderId() +
                " workStation=" + workStation +
                ", useFace='" + useFace + '\'' +
                ", index=" + index +
                ", type='" + type + '\'' +
                //", orderPositions=" + orderPositions +
                ", RCSMessage=" + this.getMessage() +
                ", RCSTime=" + this.getSend2RcsTime() +
                ", OrderError=" + OrderErrorMessage.getMsg(this.getOrderError()) +
                '}';
    }

    @Override
    public void reInitOrder() {
        this.initOrder();//目前先写到一起
    }

    @Override
    public void cancel() {
        super.cancel();
        if(this.getIndex() == Order.BUFFER2STORAGE){
            Long addr = this.getEndAddr();
            Address address = this.getWareHouseManager().getAddressByAddressCodeId(addr,this.getSectionId());
            logger.debug("返回存储区调度单出错:Order:"+this.getOrderId()+" 目标节点恢复:"+addr);
            if(address != null){
                address.robotLock(this.getRobot().getRobotId(),false);
            }
            this.getRobot().setAvaliable(true);
        }

    }

    @Override
    public String getType() {
        return type;
    }

    public static void main(String[] args) {
        Robot robot = new Robot();
        Long ll = robot.getLockedAddr();
        System.out.println(ll!=0);
    }
}
