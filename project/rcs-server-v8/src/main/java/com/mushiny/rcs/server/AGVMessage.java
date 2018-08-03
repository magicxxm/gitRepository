/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.common.MachineInterface;
import com.aricojf.platform.mina.message.robot.*;
import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseExceptionMessage;
import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseMessage;
import com.aricojf.platform.mina.server.ServerManager;
import com.mingchun.mu.log.AGVMessageSendLogger;
import com.mingchun.mu.manager.IOriginCodes;
import com.mingchun.mu.mushiny.extra.function.RealTimeMessageLost;
import com.mingchun.mu.mushiny.kiva.path.IRotationArea;
import com.mingchun.mu.mushiny.kiva.path.RotationArea;
import com.mingchun.mu.mushiny.kiva.path.RotationAreaManager;
import com.mingchun.mu.mushiny.kiva.pod.IPod;
import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.RotateAreaManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.global.CommandActionTypeConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;
import com.mushiny.rcs.kiva.bus.RobotCommand;
import com.mushiny.rcs.kiva.bus.action.Charge30Action;
import com.mushiny.rcs.kiva.bus.action.RobotAction;
import com.mushiny.rcs.kiva.bus.action.Rotate11Action;
import com.mushiny.rcs.kiva.bus.action.Rotate13Action;
import com.mushiny.rcs.listener.AGVDataListener;
import com.mushiny.rcs.listener.AGVListener;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author aricochen
 */
public abstract class AGVMessage extends AGVSeriesPathContainer implements OnReceiveAGVAllMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(AGVMessage.class.getName());
    private int previousAGVStatus = AGVConfig.AGV_STATUS_NO_CHECK;
    private int AGVStatus = AGVConfig.AGV_STATUS_NO_CHECK;
    protected boolean isRunGlobalSeriesPath = false;
    protected SeriesPath currentGlobalSeriesPath;//目前正在运行的路径串
    protected int currentGlobalSeriesPathLength = 0;//目前正在运行的路径串长度
    protected String currentGlobalSeriesPathUUID;
    protected long lockedLastCellAddressCodeID = 0;

    protected long pathTargetAddressCodeID; //wcs 下发的端路径的终点

    private RobotRTMessage rtMessage;
    private RobotHeartBeatRequestMessage heartMessage;
    private RobotStatusMessage statusMessage;
    private RobotLoginRequestMessage loginMessage;
    private RobotErrorMessage errorMessage;
    private Robot2RCSActionCommandResponseMessage actionCommandReponseMessage;
    private List<AGVListener> agvListenerList = new CopyOnWriteArrayList();
    private List<AGVDataListener> agvDataListenerList = new CopyOnWriteArrayList();
    private AGVCheckItem agvCheckItem;

    public AGVMessage() {
        super();
        AGVStatus = AGVConfig.AGV_STATUS_STANDBY;
        agvCheckItem = new AGVCheckItem(this);
        isRunGlobalSeriesPath = false;
        rtMessage = new RobotRTMessage();
        heartMessage = new RobotHeartBeatRequestMessage();
        statusMessage = new RobotStatusMessage();
        loginMessage = new RobotLoginRequestMessage();
        errorMessage = new RobotErrorMessage();
        actionCommandReponseMessage = new Robot2RCSActionCommandResponseMessage();
        ServerManager.getMessageServerInstance().registeAGVAllMessageListener(this);
    }

    /*
     设置目前要处理的路径
     */
    private void setCurrentGlobalPath(SeriesPath globalBrokenPath) {

        //1.首先检测globalBrokenPath是否可走，如果可走，则OK
        CellNode unWalkedCellNode = checkSeriesPathWalked(globalBrokenPath);

        boolean isNeedRequestWCSPath = isNeedRequestPath(globalBrokenPath);

        LOG.info("是否需要重新请求路径:isNeedRequestWCSPath="+isNeedRequestWCSPath);
        if (unWalkedCellNode == null && (!isNeedRequestWCSPath)) {
            this.currentGlobalSeriesPath = globalBrokenPath;
            globalPathTargetSecondCellNode = currentGlobalSeriesPath.getCellNodeByIndex(globalBrokenPath.getCellListSize() - 2);
            currentGlobalSeriesPathUUID = currentGlobalSeriesPath.getUuid();
            currentGlobalSeriesPathLength = currentGlobalSeriesPath.getCellListSize();
            //--globalPathTargetCellNode = currentGlobalSeriesPath.getPathList().getLast();
            globalPathTargetCellNode = currentGlobalSeriesPath.getCellNodeByIndex(globalBrokenPath.getCellListSize() - 1);
            globalPathTargetAddressCodeID = globalPathTargetCellNode.getAddressCodeID();

            // mingchun.mu@mushniy.com ------------
            globalPathFirstCellNode = currentGlobalSeriesPath.getPathList().getFirst();
            // mingchun.mu@mushniy.com ------------




            fireOnSendGlobalPath2AGV(globalBrokenPath);
            //CellNode加入到指定路径
            for (CellNode cellNode : currentGlobalSeriesPath.getPathList()) {
                cellNode.setInGlobalPath(currentGlobalSeriesPath);
            }
            lockedLastCellAddressCodeID = 0;
            //改变AGV状态为AGV_STATUS_TASKED
            setAGVStatus(AGVConfig.AGV_STATUS_TASKED);
            getAgvCheckItem().setLastPositionChanageTime(System.currentTimeMillis());
            getAgvCheckItem().setLastLockCellTime(System.currentTimeMillis());
            LOG.debug("####(AGV_ID="+getID()+")当前运行的路径="+currentGlobalSeriesPath.toString());
        } else {
            //如果不可走，则清除目前RCS缓冲，同时通知WCS重新为本AGV规划路径
            LOG.warn("####AGV("+getID()+")路径中出现不可走的点或遇到cost值更改点，清除RCS缓冲，通知WCS重新规划路径");

            tempNextGlobalSeriesPath = null;
//            setRotationFinished(false);

            ((KivaAGV) this).clearRCSBufferPathCommand();
            setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);
            if(unWalkedCellNode != null){
                fireOnAGVSPUnWalkedCell(unWalkedCellNode);
            }
            fireOnAGVRequestWCSPath(); // 路径中有不可走点，重新请求路径
        }
    }

    /**
     * 是否需要重新请求路径
     * @param globalBrokenPath
     * @return true 需要重新下发， false 不需要重新下发
     */
    public boolean isNeedRequestPath(SeriesPath globalBrokenPath){
        boolean isCheckSeriesPathCost = checkSeriesPathCost(globalBrokenPath);
        if(!isCheckSeriesPathCost){ // 需要重新请求的条件1
            if(!isRequestPath()){ // 需要重新请求的条件2
                setRequestPath(true);
                LOG.info("AGV("+getID()+")下发长路径段("+globalBrokenPath+")遇到有cost值更改的点，需要重新请求！！！");
                return true;
            }
        }
        setRequestPath(false);
        return false;
    }

    /**
     * 需要第一个动作完成后才往下锁格
     * @param globalBrokenPath
     */
    public void setCurrentGlobalPath2(SeriesPath globalBrokenPath){
        CellNode cellNode = globalBrokenPath.getPathList().getFirst();
        boolean isPaneRotation = isPaneRotate(cellNode, globalBrokenPath);
        /*boolean isCharging = getAGVStatus() == AGVConfig.AGV_STATUS_POWERING;
        if(isCharging){
            LOG.info("AGV("+getID()+")小车在充电中，直接获取下段路径，无需提前旋转!!!");
        }*/

        if((!isRotationFinished()) && (!isPaneRotation) ){ //&& (!isCharging)
            SeriesPath firstNodeSeriesPath = new SeriesPath(globalBrokenPath.getUuid());
            firstNodeSeriesPath.addPathCell(cellNode);
            if(System.currentTimeMillis() - getFirstNodeSendTime() > AGVConfig.FIRST_NODE_SEND_TIME_INTERVAL){
                if(getPathResponse() != 0){
                    setPathResponse(-1);
                    sendPathMessage(firstNodeSeriesPath);
                    setLastSendedSeriesPath(firstNodeSeriesPath);
                    LOG.info("AGV("+getID()+")旋转动作没有完成， 并且没有收到转弯起始点路径应答包重复下发该点路径！");

                }
                setFirstNodeSendTime(System.currentTimeMillis());
            }
            setTempNextGlobalSeriesPath(globalBrokenPath);
            currentGlobalSeriesPath = null;
            isRunGlobalSeriesPath = false; // 并没有执行长路径，下次还得获取
            LOG.info("AGV("+getID()+")旋转动作没有完成， 重复下发该点路径！");
        }else{
            setCurrentGlobalPath(globalBrokenPath);
            setTempNextGlobalSeriesPath(null);
//            setRotationFinished(false);
            LOG.info("AGV("+getID()+")旋转动作已经完成， 锁格下发当前长路径段！");
        }
    }

    /**
     * 如果有清除路径动作命令下发，必须收到清除动作命令完成包才下发路径
     * @param globalBrokenPath
     */
    public void setCurrentGlobalPath3(SeriesPath globalBrokenPath){
        if(isSendClearActionCommand()){
            if(isPathClearActionFinished()){
                setSendClearActionCommand(false);
                setPathClearActionFinished(false);
                setCurrentGlobalPath2(globalBrokenPath);
                LOG.info("AGV("+getID()+")收到清除完成动作包，开始获取下发路径。。。");
            }else{
                setTempNextGlobalSeriesPath(globalBrokenPath);
                currentGlobalSeriesPath = null;
                isRunGlobalSeriesPath = false; // 并没有执行长路径，下次还得获取
                LOG.info("AGV("+getID()+")未收到清除动作完成包，等待中。。。");
            }
        }else{
            setCurrentGlobalPath2(globalBrokenPath);
        }
    }

    /**
     * 是否是旋转点
     * @param cellNode
     * @param globalBrokenPath
     * @return false
     */
    private boolean isPaneRotate(CellNode cellNode, SeriesPath globalBrokenPath) {
        if(cellNode == null){
            return false;
        }
        List<RobotAction> robotActionList = cellNode.getRobotActionList(globalBrokenPath);
        for(RobotAction robotAction : robotActionList){
            if(robotAction instanceof Rotate11Action){
                LOG.info("长路径("+globalBrokenPath+")起点为托盘旋转点("+cellNode.getAddressCodeID()+")，无需提前下发旋转动作！！！");
                return true;
            }
        }
        return false;
    }

    /**
     * 下发指定路径给小车
     * @param sp
     */
    public void sendPathMessage(SeriesPath sp) {
        AGVMessageSendLogger.logInfo(" - - - - > > > > AGV("+getID()+")路径起始点动作已下发：" + sp);
        RCS2RobotPathMessage pathMessage = new RCS2RobotPathMessage(getID());
        pathMessage.setSeriesPath(sp);
        pathMessage.toMessage();
        sendMessageToAGV(pathMessage);
        AGVMessageSendLogger.logInfo("####下发路径指令(AGV_ID=" + getID() + "):" + HexBinaryUtil.byteArrayToHexString2((byte[]) pathMessage.getMessage()));
    }

    /**
     * 核查将要下发的长路径段中是否有cost值更改的点
     * @param globalBrokenPath
     * @return false 有; true 没有
     */
    private boolean checkSeriesPathCost(SeriesPath globalBrokenPath) {
        if(globalBrokenPath == null){
            return false;
        }
        if(globalBrokenPath.getPathList() == null
                || globalBrokenPath.getPathList().size() == 0){
            return false;
        }
        for(CellNode cellNode : globalBrokenPath.getPathList()){
            if(cellNode.getAddressCodeID() != getCurrentAddressCodeID() && cellNode.isChangingCost()){
                LOG.error("下发AGV("+getID()+")的长路径段("+globalBrokenPath+")中存在，cost值更改的点("+cellNode.getAddressCodeID()+")，会重新请求路径！！！");
                return false;
            }
        }
        return true;
    }

    public void registeAGVListener(AGVListener listener) {
        if (listener != null) {
            if (!agvListenerList.contains(listener)) {
                agvListenerList.add(listener);
            }
        }
    }

    public void removeAGVListener(AGVListener listener) {
        if (listener != null) {
            if (agvListenerList.contains(listener)) {
                agvListenerList.remove(listener);
            }
        }
    }

    public void registeAGVDataListener(AGVDataListener listener) {
        if (listener != null) {
            if (!agvDataListenerList.contains(listener)) {
                agvDataListenerList.add(listener);
            }
        }
    }

    public void removeAGVDataListener(AGVDataListener listener) {
        if (listener != null) {
            if (agvDataListenerList.contains(listener)) {
                agvDataListenerList.remove(listener);
            }
        }
    }
//--------------------AGVListener调用----------------------------

    public void fireOnSendGlobalPath2AGV(SeriesPath globalPath) {
        for (AGVListener listener : agvListenerList) {
            listener.OnSendGlobalPath2AGV(this, globalPath);
        }
    }

    public void fireOnAGVUnLockedCell(LinkedList<CellNode> unLockedCellNodeList) {
        for (AGVListener listener : agvListenerList) {
            listener.OnAGVUnLockedCell(this, unLockedCellNodeList);
        }
    }

    public void fireOnAGVLockedCell(LinkedList<CellNode> lockedCellNodeList) {
        for (AGVListener listener : agvListenerList) {
            listener.OnAGVLockedCell(this, lockedCellNodeList);
        }
    }

    public  void  fireOnAGVArriveAtGlobalPathTargetCellCheck() {
        if (currentGlobalSeriesPath == null) {
            return;
        }
        if (!isRunGlobalSeriesPath) {
            return;
        }
        if (globalPathTargetCellNode == null) {
            return;
        }
        if (globalPathTargetAddressCodeID == currentAddressCodeID) {
            LOG.info(this.getID()+"号车  到达分段长路径终点：（"+globalPathTargetCellNode.getAddressCodeID()+"） currentGlobalSeriesPath="+currentGlobalSeriesPath);
            isRunGlobalSeriesPath = false;
            for (AGVListener listener : agvListenerList) {
                listener.OnAGVArriveAtGlobalPathTargetCell(this, currentGlobalSeriesPath);
            }
        }



        // mingchun.mu@mushiny.com -- 每次put进来的整条路径执行完后 状态才变为standby（1）
        if (pathTargetAddressCodeID == currentAddressCodeID) {

            // mingchum.mu@mushiny.com 到达充电路径终点， 响应充电装 - 准备对接
            boolean isCharging = false;
            if (globalPathTargetCellNode != null && currentGlobalSeriesPath!=null) {
                List<RobotAction> robotActionList = globalPathTargetCellNode.getRobotActionList(currentGlobalSeriesPath);
                if(robotActionList == null){
                    LOG.info("当前点（"+pathTargetAddressCodeID+", 目标点globalPathTargetCellNode（"+globalPathTargetCellNode.getAddressCodeID()+"））动作robotActionList="+robotActionList+", currentGlobalSeriesPath=("+currentGlobalSeriesPath+", uuid="+currentGlobalSeriesPath.getUuid()+")");
                }else {
                    for(RobotAction robotAction: robotActionList){
                        if(robotAction instanceof Charge30Action){
                            LOG.info(" < < - - - 到达充电点");
                            isCharging = true;
                            for(AGVListener agvListener: agvListenerList){
                                agvListener.onArrivedChargingPile(this, globalPathTargetCellNode.getAddressCodeID());
                            }
                            break;
                        }
                    }
                }
            }

            if(isCharging){
                setAGVStatus(AGVConfig.AGV_STATUS_POWERING);
            }else{
                setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);
            }
        }


    }

    public void fireOnAGVStatusChange(int oldStatus, int newStatus) {
        for (AGVListener listener : agvListenerList) {
            listener.OnAGVStatusChange(this, oldStatus, newStatus);
        }
    }

    public void fireOnAGVPositionChange(long oldAddressIDCode, long newAddressIDCode) {
//        getAgvCheckItem().setLastPositionChanageTime(System.currentTimeMillis()); // 能够创建锁格
        for (AGVListener listener : agvListenerList) {
            listener.OnAGVPositionChange(this, oldAddressIDCode, newAddressIDCode);
        }
    }

    //AGV重新连接到RCS
    public void fireOnAGVRepeatConnection2RCS() {
        for (AGVListener listener : agvListenerList) {
            listener.OnAGVRepeatConnection2RCS(this);
        }
    }

    /*
    AGV从新连接后地址码不在最后下发的范围之内异常,此时RCS终止所有此AGV路径缓存.
     */
    public void fireOnAGVRepeatConnection2RCS_PositionError() {
        for (AGVListener listener : agvListenerList) {
            listener.OnAGVRepeatConnection2RCS_PositionError(this);
        }
    }
    //路径中出现临时不可用的CELL

    public void fireOnAGVSPUnWalkedCell(CellNode cellNode) {
        for (AGVListener listener : agvListenerList) {
            listener.OnAGVSPUnWalkedCell(this, cellNode);
        }
    }

    /*
     AGV请求WCS重新规划路径
    发生场景：1.路径中出现临时不可走CELL
              2.锁格超时
     */
    public void fireOnAGVRequestWCSPath() {
        for (AGVListener listener : agvListenerList) {
            listener.OnAGVRequestWCSPath(this);
        }
    }

    //--------------------------AGVDataListner调用---------------------------------
    public void fireOnReceivedRTMessage(AGVMessage agv, RobotRTMessage rtMessage) {
        for (AGVDataListener listener : agvDataListenerList) {
            listener.onReceivedRTMessage(agv, rtMessage);
        }
    }

    public void fireOnReceivedHeartBeatMessage(AGVMessage agv, RobotHeartBeatRequestMessage heartMessage) {
        for (AGVDataListener listener : agvDataListenerList) {
            listener.onReceivedHeartBeatMessage(agv, heartMessage);
        }
    }

    public void fireOnReceivedStatusMessage(AGVMessage agv, RobotStatusMessage statusMessage) {
        for (AGVDataListener listener : agvDataListenerList) {
            listener.onReceivedStatusMessage(agv, statusMessage);
        }
    }

    public void fireOnReceivedErrorMessage(AGVMessage agv, RobotErrorMessage errorMessage) {
        for (AGVDataListener listener : agvDataListenerList) {
            listener.onReceivedErrorMessage(agv, errorMessage);
        }
    }

    public void fireOnReceivedLoginMessage(AGVMessage agv, RobotLoginRequestMessage loginMessage) {
        for (AGVDataListener listener : agvDataListenerList) {
            listener.onReceivedLoginMessage(agv, loginMessage);
        }
    }

    public void fireOnReceivedActionCommandResponseMessage(AGVMessage agv, Robot2RCSActionCommandResponseMessage actionCommandReponseMessage) {
        for (AGVDataListener listener : agvDataListenerList) {
            listener.onReceivedActionCommandResponseMessage(agv, actionCommandReponseMessage);
        }
    }

    //==============================================================================
    public void sendMessageToAGV(RobotMessage message) {
        if (getAGVStatus() == AGVConfig.AGV_STATUS_NO_CONNECTION) { // || getAGVStatus() == AGVConfig.AGV_STATUS_REPEAT_CONNECTION_ADD_ERROR 位置错误可以继续新任务
            LOG.error("####RCS下发AGV指令,但此AGV的TCP连接已经断开,忽略此指令!");
            return;
        }
        message.toMessage();
        getSession().write(message.getMessage());
        if (message.getFunctionWordCode() != RCS2RobotMessageTypeConfig.HEART_BEAT_RESPONSE_MESSAGE) {
            LOG.info("下发AGV(id="+getID()+", seesionID="+getSession().getId()+")数据包:" + HexBinaryUtil.byteArrayToHexString2((byte[]) message.getMessage()));
//            showAGVDebugMessage(LOG, "下发AGV(id="+getID()+", seesionID="+getSession().getId()+")数据包:" + HexBinaryUtil.byteArrayToHexString2((byte[]) message.getMessage()));
        }
    }
    public abstract SeriesPath getLastSendedSeriesPath();
    public abstract void setLastSendedSeriesPath(SeriesPath sp);

    /**
     * 路径应答包 -- 逻辑： 根据实时包下发路径后， 收到应答包， 若指定时间未收到， 重新下发，
     *      指定时间内收到， 继续根据实时包下发路径
     * @param data
     */
    @Override
    public void OnReceiveRobot2RCSResponseMessage(Robot2RCSResponseMessage data) {
        if(data != null && data.getRobotID() == getID()){
            if(data.getCommandWordBack() == RCS2RobotMessageTypeConfig.PATH_MESSAGE){
                LOG.info("收到路径正常回复包: "+HexBinaryUtil.byteArrayToHexString2((byte[]) data.getMessage()));
                if(getLastSendedSeriesPath() != null){
                    LOG.info("收到路径正常回复包:回复包地址码("+data.getAddressCodeID()+"), 已发送路径："+getLastSendedSeriesPath());
                    if(getLastSendedSeriesPath().getPathList().getFirst().getAddressCodeID() == data.getAddressCodeID()){
                        setPathResponse(0); // 收到路径正常回复包
                    }else{
                        setPathResponse(-1);
                    }
                }
            }
        }
    }
    /**
     * 路径异常应答包 -- 逻辑： 根据实时包下发路径后， 收到异常应答包， 发送清除小车路径命令（小车收到异常路径拼接路径错误， 将错误路径清除）， 重新下发原来路径。
     * @param data
     */
    @Override
    public void OnReceiveRobot2RCSResponseExceptionMessage(Robot2RCSResponseExceptionMessage data) {
        if(data.getRobotID() == getID()){
            if(data.getCommandWordBack() == RCS2RobotMessageTypeConfig.PATH_MESSAGE){
                LOG.info("收到路径异常回复包: "+HexBinaryUtil.byteArrayToHexString2((byte[]) data.getMessage()));
                if(getLastSendedSeriesPath() != null){
                    if(getLastSendedSeriesPath().getPathList().getFirst().getAddressCodeID() == data.getAddressCodeID()){
                        setPathResponse(1); // 收到路径异常回复包
                    }else{
                        setPathResponse(-1);
                    }
                }
            }
        }
    }

    /**
     * AGV实时消息
     * 实时包是当前地址码的信息  有实时包不发心跳包
     * @param data 实时消息
     */
    @Override
    public synchronized void onReceivedAGVRTMessage(RobotRTMessage data) {
        if (data.getRobotID() == getID()) {
            rtMessage = data;
//            checkAGVReConnection(rtMessage.getRobotID());
            checkAGVReConnection(data.getRobotID(), data.getMachine(), data.getAddressCodeID(), data.getSession());

            // mingchun.mu@mushiny.com  根据实时包更改当前小车绑定的podID ---------------
            rtMessage.toObject();
            setPodCodeID(rtMessage.getPodCodeID());
            // mingchun.mu@mushiny.com   ---------------

            if (rtMessage.getAddressCodeID() != currentAddressCodeID) {

                LOG.info("AGV("+getID()+")位置改变：旧码="+currentAddressCodeID+",新码="+rtMessage.getAddressCodeID());

                setPreviousAddressCodeID(currentAddressCodeID);
                currentAddressCodeID = rtMessage.getAddressCodeID();
//                currentCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(currentAddressCodeID);
                fireOnAGVPositionChange(getPreviousAddressCodeID(), currentAddressCodeID);
                ((KivaAGV) this).setSendSPCountForTimeout(0);//把尝试路径的发送次数清0
            }

            addRotationAreaOper();


        }
    }

    private void addRotationAreaOper(){

        lockCellOper();

        IRotationArea rotationArea = RotationAreaManager.getInstance().getRotationAreaByOutCellNode(this.getCurrentAddressCodeID());
        if(rotationArea != null){
            rotationArea.unlockRotationArea((KivaAGV) this);
        }
    }

    private void lockCellOper(){
        fireOnReceivedRTMessage(this, rtMessage);
        getAgvCheckItem().setLastRTTime(System.currentTimeMillis());
        getAgvCheckItem().setLastBeatOrRTTime(System.currentTimeMillis());
        agvGlobalCheck();
    }

    /**
     * AGV心跳消息
     * 心跳包是上一个地址码的信息  有实时包不发心跳包
     * @param data 心跳消息
     */
    @Override
    public synchronized void onReceivedAGVHeartBeatMessage(RobotHeartBeatRequestMessage data) {
        if (data.getRobotID() == getID()) {
            heartMessage = data;
//            checkAGVReConnection(heartMessage.getRobotID());
            checkAGVReConnection(data.getRobotID(), data.getMachine(), data.getAddressCodeID(), data.getSession());
          /*  if (heartMessage.getAddressCodeID() != currentAddressCodeID) {
                setPreviousAddressCodeID(currentAddressCodeID);
                currentAddressCodeID = heartMessage.getAddressCodeID();
                fireOnAGVPositionChange(getPreviousAddressCodeID(), currentAddressCodeID);
            }*/
            fireOnReceivedHeartBeatMessage(this, heartMessage);
            getAgvCheckItem().setLastBeatTime(System.currentTimeMillis());
            getAgvCheckItem().setLastBeatOrRTTime(System.currentTimeMillis());
            // 在没有实时包需要根据心跳包重新下发（eg：充电桩处）
            // -- 但是提前下发路径会影响到状态，导致重登录session不能正常合并
            // -- 去掉状态合并session，又导致路径发送丢失
            // -- 所以必须session一样再发路径
            if(this.getSession().equals(heartMessage.getSession())){
                agvGlobalCheck();
            }

        }
    }

    /**
     * AGV状态消息
     *
     * @param data 状态消息
     */
    @Override
    public void onReceivedAGVStatusMessage(RobotStatusMessage data) {
        if (data.getRobotID() == getID()) {
            statusMessage = data;

            // 周期性状态数据包，更新电池电压
            data.toObject();
            setBatteryVoltage(data.getBatteryVoltage() * 10);

            fireOnReceivedStatusMessage(this, statusMessage);
        }
    }

    public void errorPodHandle(){}

    /**
     * AGV故障消息
     *
     * @param data 故障消息
     */
    @Override
    public void onReceivedAGVErrorMessage(RobotErrorMessage data) {
        if (data.getRobotID() == getID()) {
            errorMessage = data;

            LOG.info(" < < < < - - - - 收到故障数据包1："+ errorMessage);
            LOG.info(" < < < < - - - - 收到故障数据包2："+ HexBinaryUtil.byteArrayToHexString2((byte[]) errorMessage.getMessage()));

            fireOnReceivedErrorMessage(this, errorMessage);

            kivaConfigToolModifyManager.modifyAGVMessage_onReceivedAGVErrorMessage(new IOriginCodes() {
                @Override
                public void usingOriginCodes() {
                    setAGVStatus(AGVConfig.AGV_STATUS_ERROR);
                }
            }, errorMessage, this);
        }
    }

    /*
     AGV动作命令回复消息
     */
    public void onReceivedAGVActionCommandResponseMessage(Robot2RCSActionCommandResponseMessage data) {
        if (data.getRobotID() == getID()) {
            actionCommandReponseMessage = data;
            actionCommandReponseMessage.toObject();
            RobotCommand actionCommand = actionCommandReponseMessage.getCommand();
            if (actionCommand == null) {
                LOG.error("####收到命令回复数据包为空！！！！");
                return;
            }
            LOG.debug("####收到命令回复数据包=" + actionCommand);
            fireOnReceivedActionCommandResponseMessage(this, actionCommandReponseMessage);
            if (actionCommand.getCommandCode() == CommandActionTypeConfig.START_COMMAND) {
                setAGVStatusOnPrevoius();
            }
            if (actionCommand.getCommandCode() == CommandActionTypeConfig.STOP_NEAR_CODE_COMMAND) {
                setAGVStatus(AGVConfig.AGV_STATUS_STOP_NEAR_CODE);
            }
            if (actionCommand.getCommandCode() == CommandActionTypeConfig.STOP_IMMEDIATELY_COMMAND) {
                setAGVStatus(AGVConfig.AGV_STATUS_STOP_IMMEDIATELY);
            }
            if (actionCommand.getCommandCode() == CommandActionTypeConfig.STOP_MOTO_POWER_COMMAND) {
                setAGVStatus(AGVConfig.AGV_STATUS_STOP_POWER);
            }
            if (actionCommand.getCommandCode() == CommandActionTypeConfig.CLEAR_PATH_COMMAND) {
                setAGVStatus(AGVConfig.AGV_STATUS_CLEAR_AGV_BUFFER_PATH);
                LOG.info("AGV("+getID()+")收到清除路径命令完成包。。。");
                setPathClearActionFinished(true); // 清除路径完成，可以重发路径

                //--setAGVStatus(AGVConfig.AGV_STATUS_CLEAR_RCS_BUFFER_PATH);
            }
            if (actionCommand.getCommandCode() == CommandActionTypeConfig.BEGIN_SLEEP_COMMAND) {
                setAGVStatus(AGVConfig.AGV_STATUS_SLEEPING);
            }
            if (actionCommand.getCommandCode() == CommandActionTypeConfig.STOP_SLEEP_COMMAND) {
                setAGVStatusOnPrevoius();
            }



        }
    }

    /**
     * AGV登录消息
     *
     * @param data 登录消息
     */
    @Override
    public void onReceivedAGVLoginMessage(RobotLoginRequestMessage data) {
        if (data.getMachine() == null) {
            LOG.error("##收到登录数据包，但无匹配AGV对象!!!!");
            return;
        }
        kivaConfigToolModifyManager.modifyAGVMessage_onReceivedAGVLoginMessage(new IOriginCodes() {
            @Override
            public void usingOriginCodes() {
                if (!isLogin() && getSession().equals(data.getSession())) {
                    loginMessage = data;
                    setID(loginMessage.getRobotID());
                    if (loginMessage.getAddressCodeID() != currentAddressCodeID) {
                        setPreviousAddressCodeID(currentAddressCodeID);
                        currentAddressCodeID = loginMessage.getAddressCodeID();
                        fireOnAGVPositionChange(getPreviousAddressCodeID(), currentAddressCodeID);
                    }
                    fireOnReceivedLoginMessage(AGVMessage.this, loginMessage);
                }
            }
        }, data, this);
    }



    /*
     全局路径检查
     */
    public synchronized void globalSeriesPathCheck() {
        if (isRunGlobalSeriesPath) {
            return;
        }

        SeriesPath nowSeriesPath = null;
        if(tempNextGlobalSeriesPath != null){
            nowSeriesPath = tempNextGlobalSeriesPath;
        }else {
            nowSeriesPath = getNextGlobalSeriesPath();
        }


        if (nowSeriesPath != null) {

            // 收到实时包时解锁前一个点和清空要走路径， 但是在这里已经更换路径， 所以提前清空要走路径， 解锁已经在位置改变中解锁
            /*if(globalPathTargetSecondCellNode!=null && currentGlobalSeriesPath!=null) {
                globalPathTargetSecondCellNode.setNoInGlobalPath(currentGlobalSeriesPath);
            }
            if (globalPathTargetCellNode != null && currentGlobalSeriesPath!=null) {
                globalPathTargetCellNode.setNoInGlobalPath(currentGlobalSeriesPath);
            }*/


            // 丢码导致解码不完全 -- 解决
            if(currentGlobalSeriesPath != null
                    && currentGlobalSeriesPath.getPathList() != null
                    && currentGlobalSeriesPath.getPathList().size() >= 2){
                List<CellNode> tempList = currentGlobalSeriesPath.getPathList();
                for(int i = 0, len = tempList.size(); i < len - 1; i++){
                    LOG.info("丢码导致解码不完全解锁格子("+tempList.get(i).getAddressCodeID()+")");
                    tempList.get(i).setUnLocked_MapLock(currentGlobalSeriesPath);
                }
            }

            if (globalPathTargetCellNode != null && currentGlobalSeriesPath!=null) {
//                LOG.info("从当前路径中移除终点（"+globalPathTargetCellNode.getAddressCodeID()+"） --> 当前路径："+currentGlobalSeriesPath);
                globalPathTargetCellNode.setNoInGlobalPath(currentGlobalSeriesPath);
            }
            if(globalPathTargetSecondCellNode!=null && currentGlobalSeriesPath!=null) {

                /*if(RotateAreaManager.getInstance().getRotateAreaByCellNode(globalPathTargetSecondCellNode) != null){
                    LOG.info("解锁旋转区的出口点（"+globalPathTargetSecondCellNode.getAddressCodeID()+"） --> 当前路径："+currentGlobalSeriesPath);
                    globalPathTargetSecondCellNode.setUnLocked_MapLock(currentGlobalSeriesPath);
                }*/
//                LOG.info("从当前路径中移除倒数第二点（"+globalPathTargetSecondCellNode.getAddressCodeID()+"） --> 当前路径："+currentGlobalSeriesPath);
               /* if (globalPathTargetAddressCodeID == currentAddressCodeID) {
                    if(this.equals(globalPathTargetSecondCellNode.getNowLockedAGV())){
                        globalPathTargetSecondCellNode.setUnLocked_MapLock(currentGlobalSeriesPath);
                    }
                }*/
                globalPathTargetSecondCellNode.setNoInGlobalPath(currentGlobalSeriesPath);


            }

            if(currentGlobalSeriesPath != null && currentGlobalSeriesPath.getPathList() != null && currentGlobalSeriesPath.getPathList().size() > 0){
                for(CellNode cellNode: currentGlobalSeriesPath.getPathList()){
                    cellNode.setNoInGlobalPath(currentGlobalSeriesPath);
                }
            }


            isRunGlobalSeriesPath = true;

            setCurrentGlobalPath3(nowSeriesPath);

            RealTimeMessageLost.getInstance().putAGVGlobalSeriesPath((KivaAGV) this, nowSeriesPath); // 打印丢失地址码

        }
    }

    /*
     运行全局路径并且锁格检查
     */
    public void runGlobalSeriesPathAndLockCellCheck() {

    }


    /*
    AGV全局检查
     */
    public synchronized void agvGlobalCheck() {
        //-- fireOnAGVArrivedAtLockPathTargetCellCheck();
        fireOnAGVArriveAtGlobalPathTargetCellCheck();
        updatePodPosition(); //更新pod位置
        globalSeriesPathCheck();
        updatePodPosition(); //更新pod位置
        runGlobalSeriesPathAndLockCellCheck();
    }


    private void unlockCell(Long ... longs){
        for(Long L : longs ){
            MapManager.getInstance().getMap().unlockCell(L);
        }
    }


    private void printPodPos(){
        StringBuilder stringBuilder = new StringBuilder();
        for(IPod p: PodManager.getInstance().getPodList()){
            stringBuilder.append(p);
            stringBuilder.append(",");
        }
        LOG.info("当前pod的所有位置："+stringBuilder);
    }

    protected synchronized void updatePodPosition(){
        // 如果到达举升点， 将小车置为举升状态
        if(currentGlobalSeriesPath != null
                && currentGlobalSeriesPath.getUpCellNode() != null
                && currentGlobalSeriesPath.getUpCellNode().getAddressCodeID() == getCurrentAddressCodeID()){ // rtMessage.getAddressCodeID() -- 有问题7车实时包445，但是rtMessage.getAddressCodeID()=290
            setLift(true);
            LOG.info("AGV("+getID()+")举升时的实时包rtMessage="+HexBinaryUtil.byteArrayToHexString2((byte[]) rtMessage.getMessage()));
            LOG.info("AGV("+getID()+")到达举升点("+getCurrentAddressCodeID()+")，更改举升状态isLift="+isLift()+"currentGlobalSeriesPath="+currentGlobalSeriesPath);
            //更新pod的位置
            IPod pod = PodManager.getInstance().getPod(MapManager.getInstance().getMap().getMapCellByAddressCodeID(getCurrentAddressCodeID()));

//            LOG.info("分段路径最后一个位置未获取到pod："+pod);

            if(pod != null){
                pod.setCellNode(null);
                currentGlobalSeriesPath.setUpCellNode(null); // 多个事实包取下一个实时包
//                printPodPos();
            }
        }
        // 如果到达下降点， 将小车置为下降状态
        if(currentGlobalSeriesPath != null
                && currentGlobalSeriesPath.getDownCellNode() != null
                && currentGlobalSeriesPath.getDownCellNode().getAddressCodeID() == getCurrentAddressCodeID()){
            setLift(false);
            LOG.info("AGV("+getID()+")下降时的实时包rtMessage="+HexBinaryUtil.byteArrayToHexString2((byte[]) rtMessage.getMessage()));
            LOG.info("AGV("+getID()+")到达下降点("+getCurrentAddressCodeID()+")，更改举升状态isLift="+isLift()+"currentGlobalSeriesPath="+currentGlobalSeriesPath);

            //更新pod的位置
            IPod pod = PodManager.getInstance().getPod(rtMessage.getPodCodeID());
            if(pod != null){
                pod.setCellNode(MapManager.getInstance().getMap().getMapCellByAddressCodeID(getCurrentAddressCodeID()));
                currentGlobalSeriesPath.setDownCellNode(null); // 多个事实包取下一个实时包
//                printPodPos();
            }
        }

    }

    /*
     则检测原来存在且处于AGV_STATUS_NO_CONNECTION状态的AGV,如果存在则合并AGV对象,
     说明此AGV是新断开后的连接;如果没有检测到则说明是新连接.
     此方法被调用于下情况:
     检测到心跳包\实时数据包
     重连连接后的一些影响:
     重新连接后,如果AGV所在的最新地址码在断开之前下发锁格路径范围之内:则继续执行
     原来的路径,如果不在范围之内,则无法也不进行处理任何路径.
     */
    public abstract void checkAGVReConnection(long tmpAGVID);
    public abstract void checkAGVReConnection(long robotID, MachineInterface machine, long addressCodeID, IoSession session);


    /*
     检测路径是否有不可走的CELL，
     此方法在运行目前路径之前进行进行调用
     */
    public abstract CellNode checkSeriesPathWalked(SeriesPath sp);

    /**
     * @return the AGV_STATUS
     */
    public int getAGVStatus() {
        return AGVStatus;
    }

    /**
     * @param AGVStatus the AGV_STATUS to set
     */
    public void setAGVStatus(int AGVStatus) {
        setPreviousAGVStatus(this.AGVStatus);
        fireOnAGVStatusChange(this.AGVStatus, AGVStatus);
        this.AGVStatus = AGVStatus;
    }

    /*
     设置AGV的状态为前一个状态
     */
    public void setAGVStatusOnPrevoius() {
        setAGVStatus(getPreviousAGVStatus());
    }

    /**
     * @return the agvCheckItem
     */
    public AGVCheckItem getAgvCheckItem() {
        return agvCheckItem;
    }

    /**
     * @return the PREVIOUS_AGV_STATUS
     */
    public int getPreviousAGVStatus() {
        return previousAGVStatus;
    }

    /**
     * @param previousAGVStatus the PREVIOUS_AGV_STATUS to set
     */
    public void setPreviousAGVStatus(int previousAGVStatus) {
        this.previousAGVStatus = previousAGVStatus;
    }

    public RobotLoginRequestMessage getLoginMessage() {
        return loginMessage;
    }
    public void setLoginMessage(RobotLoginRequestMessage loginMessage) {
        this.loginMessage = loginMessage;
    }

    public boolean isRunGlobalSeriesPath() {
        return isRunGlobalSeriesPath;
    }

    public void setRunGlobalSeriesPath(boolean runGlobalSeriesPath) {
        isRunGlobalSeriesPath = runGlobalSeriesPath;
    }

    public SeriesPath getCurrentGlobalSeriesPath() {
        return currentGlobalSeriesPath;
    }

    public void setCurrentGlobalSeriesPath(SeriesPath currentGlobalSeriesPath) {
        this.currentGlobalSeriesPath = currentGlobalSeriesPath;
    }
}
