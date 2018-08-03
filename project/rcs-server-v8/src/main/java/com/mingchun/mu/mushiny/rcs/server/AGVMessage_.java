/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*

package com.mingchun.mu.mushiny.rcs.server;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.common.MachineInterface;
import com.aricojf.platform.mina.message.robot.*;
import com.aricojf.platform.mina.server.ServerManager;
import com.mingchun.mu.manager.IOriginCodes;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.path.RotateAreaManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.global.CommandActionTypeConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;
import com.mushiny.rcs.kiva.bus.RobotCommand;
import com.mushiny.rcs.kiva.bus.action.Charge30Action;
import com.mushiny.rcs.kiva.bus.action.RobotAction;
import com.mushiny.rcs.listener.AGVDataListener;
import com.mushiny.rcs.listener.AGVListener;
import com.mushiny.rcs.server.AGVCheckItem;
import com.mushiny.rcs.server.AGVSeriesPathContainer;
import com.mushiny.rcs.server.KivaAGV;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
public abstract class AGVMessage_ extends AGVSeriesPathContainer implements OnReceiveAGVAllMessageListener {

    private static final Logger LOG = Logger.getLogger(AGVMessage_.class.getName());
    private int previousAGVStatus = AGVConfig.AGV_STATUS_NO_CHECK;
    private int AGVStatus = AGVConfig.AGV_STATUS_NO_CHECK;
    protected boolean isRunGlobalSeriesPath = false;
    protected SeriesPath currentGlobalSeriesPath;//目前正在运行的路径串
    protected int currentGlobalSeriesPathLength = 0;//目前正在运行的路径串长度
    protected String currentGlobalSeriesPathUUID;
    protected long lockedLastCellAddressCodeID = 0;

    protected long pathTargetAddressCodeID; //wcs 下发的端路径的终点
    protected boolean isSendedCurrentGlobalSeriesPath = false;//当前全局路径是否下发过

    private RobotRTMessage rtMessage;
    private RobotHeartBeatRequestMessage heartMessage;
    private RobotStatusMessage statusMessage;
    private RobotLoginRequestMessage loginMessage;
    private RobotErrorMessage errorMessage;
    private Robot2RCSActionCommandResponseMessage actionCommandReponseMessage;
    private List<AGVListener> agvListenerList = new CopyOnWriteArrayList();
    private List<AGVDataListener> agvDataListenerList = new CopyOnWriteArrayList();
    private AGVCheckItem agvCheckItem;

    public AGVMessage_() {
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

    */
/*
     设置目前要处理的路径
     *//*

    private void setCurrentGlobalPath(SeriesPath globalBrokenPath) {
        //1.首先检测globalBrokenPath是否可走，如果可走，则OK
        CellNode unWalkedCellNode = checkSeriesPathWalked(globalBrokenPath);
        if (unWalkedCellNode == null) {
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
            LOG.warn("####路径中出现不可走的点，清除RCS缓冲，通知WCS重新规划路径");
            ((KivaAGV) this).clearRCSBufferPathCommand();
            setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);
            fireOnAGVSPUnWalkedCell(unWalkedCellNode);
            fireOnAGVRequestWCSPath();
        }
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
            LOG.info(this.getID()+"号车  到达分段长路径终点：（"+globalPathTargetCellNode.getAddressCodeID()+"）");
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
        getAgvCheckItem().setLastPositionChanageTime(System.currentTimeMillis());
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

    */
/*
    AGV从新连接后地址码不在最后下发的范围之内异常,此时RCS终止所有此AGV路径缓存.
     *//*

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

    */
/*
     AGV请求WCS重新规划路径
    发生场景：1.路径中出现临时不可走CELL
              2.锁格超时
     *//*

    public void fireOnAGVRequestWCSPath() {
        for (AGVListener listener : agvListenerList) {
            listener.OnAGVRequestWCSPath(this);
        }
    }

//--------------------------AGVDataListner调用---------------------------------
    public void fireOnReceivedRTMessage(AGVMessage_ agv, RobotRTMessage rtMessage) {
        for (AGVDataListener listener : agvDataListenerList) {
            listener.onReceivedRTMessage(agv, rtMessage);
        }
    }

    public void fireOnReceivedHeartBeatMessage(AGVMessage_ agv, RobotHeartBeatRequestMessage heartMessage) {
        for (AGVDataListener listener : agvDataListenerList) {
            listener.onReceivedHeartBeatMessage(agv, heartMessage);
        }
    }

    public void fireOnReceivedStatusMessage(AGVMessage_ agv, RobotStatusMessage statusMessage) {
        for (AGVDataListener listener : agvDataListenerList) {
            listener.onReceivedStatusMessage(agv, statusMessage);
        }
    }

    public void fireOnReceivedErrorMessage(AGVMessage_ agv, RobotErrorMessage errorMessage) {
        for (AGVDataListener listener : agvDataListenerList) {
            listener.onReceivedErrorMessage(agv, errorMessage);
        }
    }

    public void fireOnReceivedLoginMessage(AGVMessage_ agv, RobotLoginRequestMessage loginMessage) {
        for (AGVDataListener listener : agvDataListenerList) {
            listener.onReceivedLoginMessage(agv, loginMessage);
        }
    }

    public void fireOnReceivedActionCommandResponseMessage(AGVMessage_ agv, Robot2RCSActionCommandResponseMessage actionCommandReponseMessage) {
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
            showAGVDebugMessage(LOG, "下发AGV(id="+getID()+", seesionID="+getSession().getId()+")数据包:" + HexBinaryUtil.byteArrayToHexString2((byte[]) message.getMessage()));
        }
    }

    */
/**
     * AGV实时消息
     *
     * @param data 实时消息
     *//*

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
                setPreviousAddressCodeID(currentAddressCodeID);
                currentAddressCodeID = rtMessage.getAddressCodeID();
                fireOnAGVPositionChange(getPreviousAddressCodeID(), currentAddressCodeID);
            }
            fireOnReceivedRTMessage(this, rtMessage);
            getAgvCheckItem().setLastRTTime(System.currentTimeMillis());
            getAgvCheckItem().setLastBeatOrRTTime(System.currentTimeMillis());
            agvGlobalCheck();

        }
    }

    */
/**
     * AGV心跳消息
     *
     * @param data 心跳消息
     *//*

    @Override
    public synchronized void onReceivedAGVHeartBeatMessage(RobotHeartBeatRequestMessage data) {
        if (data.getRobotID() == getID()) {
            heartMessage = data;
//            checkAGVReConnection(heartMessage.getRobotID());
            checkAGVReConnection(data.getRobotID(), data.getMachine(), data.getAddressCodeID(), data.getSession());
          */
/*  if (heartMessage.getAddressCodeID() != currentAddressCodeID) {
                setPreviousAddressCodeID(currentAddressCodeID);
                currentAddressCodeID = heartMessage.getAddressCodeID();
                fireOnAGVPositionChange(getPreviousAddressCodeID(), currentAddressCodeID);
            }*//*

            fireOnReceivedHeartBeatMessage(this, heartMessage);
            getAgvCheckItem().setLastBeatTime(System.currentTimeMillis());
            getAgvCheckItem().setLastBeatOrRTTime(System.currentTimeMillis());
            agvGlobalCheck();

        }
    }

    */
/**
     * AGV状态消息
     *
     * @param data 状态消息
     *//*

    @Override
    public void onReceivedAGVStatusMessage(RobotStatusMessage data) {
        if (data.getRobotID() == getID()) {
            statusMessage = data;
            fireOnReceivedStatusMessage(this, statusMessage);
        }
    }

    */
/**
     * AGV故障消息
     *
     * @param data 故障消息
     *//*

    @Override
    public void onReceivedAGVErrorMessage(RobotErrorMessage data) {
        if (data.getRobotID() == getID()) {
            errorMessage = data;
            fireOnReceivedErrorMessage(this, errorMessage);
            setAGVStatus(AGVConfig.AGV_STATUS_ERROR);
        }
    }

    */
/*
     AGV动作命令回复消息
     *//*

    public void onReceivedAGVActionCommandResponseMessage(Robot2RCSActionCommandResponseMessage data) {
        if (data.getRobotID() == getID()) {
            actionCommandReponseMessage = data;
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

    */
/**
     * AGV登录消息
     *
     * @param data 登录消息
     *//*

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
                    fireOnReceivedLoginMessage(AGVMessage_.this, loginMessage);
                }
            }
        }, data, this);
    }



    */
/*
     全局路径检查
     *//*

    private int podCounts;
    public  void globalSeriesPathCheck() {
        if (isRunGlobalSeriesPath) {
            return;
        }
//        SeriesPath nowSeriesPath = getNextGlobalSeriesPath();
        // mingchun.mu@mushiny.com  -->  如果pod扫描错误(currentGlobalSeriesPath 最后一个点为扫描点)  清空路径  不在执行
        SeriesPath nowSeriesPath = null;
        if(currentGlobalSeriesPath != null && currentGlobalSeriesPath.getUpCellNode() != null && globalPathTargetCellNode.getAddressCodeID() == currentAddressCodeID){
            LOG.info(" AGV("+getID()+")  podID扫描正确条件：currentAddressCodeID="+currentAddressCodeID+", getUpCellNode="+currentGlobalSeriesPath.getUpCellNode().getAddressCodeID()+", podCodeID="+podCodeID+", currentGlobalSeriesPath.getUpPodID()="+currentGlobalSeriesPath.getUpPodID()+", currentGlobalSeriesPath="+currentGlobalSeriesPath);
            if(currentAddressCodeID == currentGlobalSeriesPath.getUpCellNode().getAddressCodeID()
                    && podCodeID == currentGlobalSeriesPath.getUpPodID()){
                nowSeriesPath = getNextGlobalSeriesPath();
                podCounts = 0;
                LOG.info(" AGV("+getID()+")  扫描到正确的pod("+podCodeID+"), 路径获取成功:"+nowSeriesPath);
            }else{
                if(podCounts < 20){
                    podCounts++;
                    return;
                }
                LOG.error("pod扫描错误(正确："+currentGlobalSeriesPath.getUpPodID()+",扫到pod："+podCodeID+" 停在此位置  ！！！");
                currentGlobalSeriesPath.setUpCellNode(null);
                seriesPathLinkedList.clear();
                setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);
                podCounts = 0;
                return;
            }
        }else{
            nowSeriesPath = getNextGlobalSeriesPath();
        }
        // mingchun.mu@mushiny.com   -----------------


        if (nowSeriesPath != null) {
            if (globalPathTargetCellNode != null && currentGlobalSeriesPath!=null) {
//                LOG.info("从当前路径中移除终点（"+globalPathTargetCellNode.getAddressCodeID()+"） --> 当前路径："+currentGlobalSeriesPath);
                globalPathTargetCellNode.setNoInGlobalPath(currentGlobalSeriesPath);
            }
            if(globalPathTargetSecondCellNode!=null && currentGlobalSeriesPath!=null) {

                if(RotateAreaManager.getInstance().getRotateAreaByCellNode(globalPathTargetSecondCellNode) != null){
//                    LOG.info("解锁旋转区的出口点（"+globalPathTargetSecondCellNode.getAddressCodeID()+"） --> 当前路径："+currentGlobalSeriesPath);
                    globalPathTargetSecondCellNode.setUnLocked_MapLock(currentGlobalSeriesPath);
                }
//                LOG.info("从当前路径中移除倒数第二点（"+globalPathTargetSecondCellNode.getAddressCodeID()+"） --> 当前路径："+currentGlobalSeriesPath);
                if (globalPathTargetAddressCodeID == currentAddressCodeID) {
                    if(this.equals(globalPathTargetSecondCellNode.getNowLockedAGV())){
                        globalPathTargetSecondCellNode.setUnLocked_MapLock(currentGlobalSeriesPath);
                    }
                }
                globalPathTargetSecondCellNode.setNoInGlobalPath(currentGlobalSeriesPath);


            }

            if(currentGlobalSeriesPath != null && currentGlobalSeriesPath.getPathList() != null && currentGlobalSeriesPath.getPathList().size() > 0){
                for(CellNode cellNode: currentGlobalSeriesPath.getPathList()){
                    cellNode.setNoInGlobalPath(currentGlobalSeriesPath);
                }
            }


            setCurrentGlobalPath(nowSeriesPath);
            isSendedCurrentGlobalSeriesPath = false;
            isRunGlobalSeriesPath = true;
        }
    }

    */
/*
     运行全局路径并且锁格检查
     *//*

    public void runGlobalSeriesPathAndLockCellCheck() {

    }


    */
/*
    AGV全局检查
     *//*

    public synchronized void agvGlobalCheck() {
        //-- fireOnAGVArrivedAtLockPathTargetCellCheck();
        fireOnAGVArriveAtGlobalPathTargetCellCheck();
        globalSeriesPathCheck();
        runGlobalSeriesPathAndLockCellCheck();
    }

    */
/*
     则检测原来存在且处于AGV_STATUS_NO_CONNECTION状态的AGV,如果存在则合并AGV对象,
     说明此AGV是新断开后的连接;如果没有检测到则说明是新连接.
     此方法被调用于下情况:
     检测到心跳包\实时数据包
     重连连接后的一些影响:
     重新连接后,如果AGV所在的最新地址码在断开之前下发锁格路径范围之内:则继续执行
     原来的路径,如果不在范围之内,则无法也不进行处理任何路径.
     *//*

    public abstract void checkAGVReConnection(long tmpAGVID);
    public abstract void checkAGVReConnection(long robotID, MachineInterface machine, long addressCodeID, IoSession session);


    */
/*
     检测路径是否有不可走的CELL，
     此方法在运行目前路径之前进行进行调用
     *//*

    public abstract CellNode checkSeriesPathWalked(SeriesPath sp);

    */
/**
     * @return the AGV_STATUS
     *//*

    public int getAGVStatus() {
        return AGVStatus;
    }

    */
/**
     * @param AGVStatus the AGV_STATUS to set
     *//*

    public void setAGVStatus(int AGVStatus) {
        setPreviousAGVStatus(this.AGVStatus);
        fireOnAGVStatusChange(this.AGVStatus, AGVStatus);
        this.AGVStatus = AGVStatus;
    }

    */
/*
     设置AGV的状态为前一个状态
     *//*

    public void setAGVStatusOnPrevoius() {
        setAGVStatus(getPreviousAGVStatus());
    }

    */
/**
     * @return the agvCheckItem
     *//*

    public AGVCheckItem getAgvCheckItem() {
        return agvCheckItem;
    }

    */
/**
     * @return the PREVIOUS_AGV_STATUS
     *//*

    public int getPreviousAGVStatus() {
        return previousAGVStatus;
    }

    */
/**
     * @param previousAGVStatus the PREVIOUS_AGV_STATUS to set
     *//*

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
*/
