/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.common.MachineInterface;
import com.aricojf.platform.mina.message.robot.*;
import com.mingchun.mu.aricojf.platform.mina.message.robot.media.error.Robot2RCSMidiaErrorMessage;
import com.mingchun.mu.aricojf.platform.mina.message.robot.response.RCS2RobotActionResponseMessage;
import com.mingchun.mu.manager.IOriginCodes;
import com.mingchun.mu.mushiny.kiva.pod.IPod;
import com.mingchun.mu.mushiny.kiva.pod.Pod;
import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.kiva.bus.*;
import org.apache.mina.core.session.IoSession;

import java.util.List;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class KivaAGV extends AGV {

    //AGV失去TCP连接前的状态
    //private int KivaAGVBeforeNoTCPConnectionStatus = 0;
    //位置不改变超时且带任务时，尝试发送路径的次数
    private int sendSPCountForTimeout=0;
    public KivaAGV() {
        super();
    }

    //启动
    public void startCommand() {
        StartCommand startCommand = new StartCommand();
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), startCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        setStopSendSeriesPathFlag(false);
        LOG.warn("####发送启动命令(AGV_ID=" + getID() + ")");
    }
    //停到最近二维码
    public void stopNearCodeCommand() {
        StopByNearCodeCommand stopNearCodeCommand = new StopByNearCodeCommand();
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), stopNearCodeCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        setStopSendSeriesPathFlag(true);
        LOG.warn("####发送停到最近二维码命令");
    }

    //急停
    public void stopImmediatelyCommand() {
        StopImmediatelyCommand stopImmediateCommand = new StopImmediatelyCommand();
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), stopImmediateCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        setStopSendSeriesPathFlag(true);
        LOG.warn("####发送急停命令");
    }

    //所有电机供电断电
    public void stopMotoPowerCommand() {
        StopMotoPowerCommand stopMotoPowerCommand = new StopMotoPowerCommand();
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), stopMotoPowerCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        setStopSendSeriesPathFlag(true);
        LOG.warn("####发送电机供电断电命令");
    }
    // 旋转(托盘固定)
    public void turn(short param){
        Rotate10Command rotate10Command = new Rotate10Command();
        rotate10Command.setCommandParameter(param);
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), rotate10Command);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送 旋转(托盘固定)");
    }
    // 旋转(托盘单独转动)
    public void turnplateRotate(short param){
        Rotate11Command rotate11Command = new Rotate11Command();
        rotate11Command.setCommandParameter(param);
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), rotate11Command);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送 旋转(托盘单独转动)");
    }
    // 旋转(托盘固定，且附带顶升)
    public void turnAndUp(short param){
        Rotate12Command rotate12Command = new Rotate12Command();
        rotate12Command.setCommandParameter(param);
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), rotate12Command);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送 旋转(托盘固定，且附带顶升)");
    }
    // 旋转(托盘固定，且附带下降)
    public void turnAndDown(short param){
        Rotate13Command rotate13Command = new Rotate13Command();
        rotate13Command.setCommandParameter(param);
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), rotate13Command);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送 旋转(托盘固定，且附带下降)");
    }
    // 顶升
    public void up(short param){
        UpCommand upCommand  = new UpCommand();
        upCommand.setCommandParameter(param);
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), upCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送 顶升");
    }
    // 下降
    public void down(short param){
        DownCommand downCommand  = new DownCommand();
        downCommand.setCommandParameter(param);
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), downCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送 下降");
    }

    //开始休眠
    public void beginSleepCommand() {
        if (getAGVStatus() == AGVConfig.AGV_STATUS_TASKED) {
            LOG.warn("####收到开始休眠命令,但目前AGV处于任务状态,不予执行!(AGV_ID=" + getID() + ")");
            return;
        }
        BeginSleepCommand beginSleepCommand = new BeginSleepCommand();
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), beginSleepCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送开始休眠命令(AGV_ID=" + getID() + ")");
    }

    //结束休眠
    public void stopSleepCommand() {
        StopSleepCommand stopSleepCommand = new StopSleepCommand();
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), stopSleepCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送停止休眠命令(AGV_ID=" + getID() + ")");
    }

    //清除AGV路径缓冲
    public void clearAGVBufferPathCommand() {
        ClearPathCommand clearPathCommand = new ClearPathCommand();
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), clearPathCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送AGV缓冲路径清除命令(AGV_ID=" + getID() + ")");
        setSendClearActionCommand(true);
    }

    // 开始充电
    public void startCharging() {
        StartChargingCommand startChargingCommand = new StartChargingCommand();
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), startChargingCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送 开始充电");
    }

    // 结束充电
    public void endCharging() {
        EndChargingCommand endChargingCommand = new EndChargingCommand();
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), endChargingCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送 结束充电");
    }







    /**
     * 清除错误故障
     * @param generalErrorID
     * @param commonErrorID
     * @param seriousErrorID
     * @param logicErrorID
     */
    public void clearMediaError(int generalErrorID, int commonErrorID, int seriousErrorID, int logicErrorID) {
        Robot2RCSMidiaErrorMessage mediaErrorMessage = new Robot2RCSMidiaErrorMessage(getID());
        mediaErrorMessage.setGeneralErrorID(generalErrorID);
        mediaErrorMessage.setCommonErrorID(commonErrorID);
        mediaErrorMessage.setSeriousErrorID(seriousErrorID);
        mediaErrorMessage.setLogicErrorID(logicErrorID);
        mediaErrorMessage.setErrorStatus(1);
        sendMessageToAGV(mediaErrorMessage);
    }

    //清除RCS路径缓冲
    public void clearRCSBufferPathCommand() {
        isRunGlobalSeriesPath=false;
        clearGlobalSeriesPath();
        clearCurrentGlobalSeriesPath();
       
        setAGVStatus(AGVConfig.AGV_STATUS_CLEAR_RCS_BUFFER_PATH);
        setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);
        LOG.warn("####执行RCS缓冲路径清除命令(AGV_ID=" + getID() + ")");
    }
    //清除全部缓冲路径
    @Override
     public synchronized void clearBufferSP() {
//        stopNearCodeCommand();  // mingchun.mu#mushiny.com  停到最近二维码先不用（车未实现功能）
        clearRCSBufferPathCommand();
//        clearAGVBufferPathCommand();
        setStopSendSeriesPathFlag(false);
        LOG.warn("####AGV停到最近二维码，且清除AGV,RCS缓冲路径(AGV_ID=" + getID() + ")");
    }

    //清除全部缓冲路径
    public synchronized void clearAndUnlockBufferSP() {
        clearBufferSP();

        clearAGVBufferPathCommand(); // 清除小车路径
        if(getCurrentSeriesPath() != null){ // 解锁已下发的当前路径
            for(CellNode cellNode : getCurrentSeriesPath().getPathList()){
                if(cellNode.getAddressCodeID() != getCurrentAddressCodeID()){
                    if(this.equals(cellNode.getAGV())){ // 只能解锁自己锁定的格子
                        cellNode.setUnLocked_MapLock(getCurrentSeriesPath());
                    }
                }
            }
        }
        setLastSendedSeriesPath(null); // 相同路径还能重复发
        setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);
    }

    //锁格超时，触发
     public void notifyLockTimeout() {
         clearBufferSP();
//         this.fireOnAGVRequestWCSPath();
     }


    //执行动作命令
    public void sendActionCommand(RobotCommand robotCommand) {
        RCS2RobotActionCommandMessage actionCommandMessage = new RCS2RobotActionCommandMessage(getID(), robotCommand);
        actionCommandMessage.toMessage();
        sendMessageToAGV(actionCommandMessage);
        LOG.warn("####发送动作命令消息=" + actionCommandMessage + "(AGV_ID=" + getID() + ")");
    }

    //发送心跳包
    public void sendBeatMessage() {
        RCSHeartBeatResponseMessage heartMessage = new RCSHeartBeatResponseMessage(getID());
        heartMessage.toMessage();
        sendMessageToAGV(heartMessage);
        //-- LOG.warn("####发送心跳消息(AGV_ID="+getID()+")");
    }

    //发送登录数据包回复
    public void sendLoginOKMessage() {
        //if (!isLogin()) {//保证登录的AGV不再重发登录回复
          //重新登录检测
//        checkAGVReConnection(getID());//在心跳和实时包处检测即可
        getAgvCheckItem().setLastBeatOrRTTime(System.currentTimeMillis());
        setLogin(true);
        RCS2RobotLoginResponseMessage loginRepsonseMessage = new RCS2RobotLoginResponseMessage(getID());
        loginRepsonseMessage.toMessage();
        sendMessageToAGV(loginRepsonseMessage);
        showAGVDebugMessage(LOG, "####发送登录回复消息:" + HexBinaryUtil.byteArrayToHexString2((byte[]) loginRepsonseMessage.getMessage()));
        //}
    }

    //发送激活包
    public void sendActiveMessage() {
        RCS2RobotActiveMessage activeMessage = new RCS2RobotActiveMessage(getID());
        activeMessage.toMessage();
        sendMessageToAGV(activeMessage);
        LOG.warn("####发送AGV激活消息(AGV_ID=" + getID() + ")");
    }

    /**
     * 发送行走参数配置包
     */
    public void sendWalkParameterConfigMessage(int weightClass){
        RCS2RobotWalkParameterConfigMessage walkParameterConfigMessage = new RCS2RobotWalkParameterConfigMessage(getID());
        walkParameterConfigMessage.setWeightClass(weightClass);
        walkParameterConfigMessage.toMessage();
        sendMessageToAGV(walkParameterConfigMessage);
        LOG.warn("####发送行走参数配置包(AGV_ID=" + getID() + ") - - - > > :" + HexBinaryUtil.byteArrayToHexString2((byte[]) walkParameterConfigMessage.getMessage()));
    }

    /*
     设置AGV为TCP连接断开状态
     */
    public void setAGVStatusOnTCPConnectionClose() {
        //if (getAGVStatus() != AGVConfig.AGV_STATUS_NO_CONNECTION) {
            //--setKivaAGVBeforeNoTCPConnectionStatus(getAGVStatus());
            setAGVStatus(AGVConfig.AGV_STATUS_NO_CONNECTION);//无TCP连接 
        //}
    }


    /*
     设置AGV恢复TCP连接之前的状态,如果目前的状态是非NO_TCP_CONNECTION,则不进行任何设置
     */
    public void setAGVStatusOnTCPConnectionReOpen() {
        if (getAGVStatus() == AGVConfig.AGV_STATUS_NO_CONNECTION) {
            //setAGVStatus(getKivaAGVBeforeNoTCPConnectionStatus());
            setAGVStatus(getPreviousAGVStatus());
        }
    }

    public String getAGVStatusInfo() {
        return AGVConfig.getAGVStatusInfo(getAGVStatus());
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
    public synchronized void checkAGVReConnection(long tmpAGVID) {
        if (currentAddressCodeID == 0) {
            return;
        }
        for (KivaAGV agv : AGVManager.getInstance().getAgvList()) {
            if (agv.getAGVStatus() != AGVConfig.AGV_STATUS_NO_CONNECTION) {
                continue;
            }
            if (agv.getID() == tmpAGVID) {
                LOG.warn("####  当前agvID="+getID()+", 检测到AGV(ID=" + tmpAGVID + ")重新登录! sessionID="+getSession().getId()+", 旧sessionID="+agv.getSession().getId());


                agv.setSession(getSession());
                agv.setRtTimeout(false);
                //此新的连接AGV离开目前的CELL
                fireOnAGVPositionChange(currentAddressCodeID, 0);
                //老的失去TCP连接AGV进入目前的CELL
                agv.fireOnAGVPositionChange(agv.getCurrentAddressCodeID(), currentAddressCodeID);
                agv.setCurrentAddressCodeID(currentAddressCodeID);

                setID(tmpAGVID);
                agv.setAGVStatusOnTCPConnectionReOpen();
                AGVManager.getInstance().removeAGVFromAGVList((KivaAGV) this);

                agv.fireOnAGVRepeatConnection2RCS();
                //确认最新的位置是否在"最后一次锁格路径范围之内",如果"在"则不预警,继续之前的状态;
                //如果"不在"
                //则上报WCS,且"清空"本AGV所有路径缓冲
                if (agv.getLastSendedSeriesPath() != null) {
                    boolean isOk = false;
                    for (CellNode cell : agv.getLastSendedSeriesPath().getPathList()) {
                        if (cell.getAddressCodeID() == currentAddressCodeID) {
                            agv.unlockCellFromSP();//原来是路径执行过程中,断开的->解锁
                            isOk = true;
                        }
                    }
                    if (!isOk) {
                        LOG.error("####AGV重新连接后，位置异常，上报WCS,RCS清除所有任务缓存！！");
                        agv.setStopSendSeriesPathFlag(false);
                        agv.clearRCSBufferPathCommand();

                        fireOnAGVRequestWCSPath(); // 重连位置错误清空路径后，需要重新请求路径

                        agv.setAGVStatus(AGVConfig.AGV_STATUS_REPEAT_CONNECTION_ADD_ERROR);
                        agv.fireOnAGVRepeatConnection2RCS_PositionError();
                    }else {
                        LOG.warn("####AGV重新连接后，继续执行原来的任务！！");
                        // mingchun.mu@mushiny.com  -- 移除断开走过的锁格格子， 并重新下发之前的锁格格子
                        agv.agvGlobalCheck();
                        agv.sendSeriesPath(agv.getLastSendedSeriesPath());
                        // mingchun.mu@mushiny.com  -----------------------

//                        agv.fireOnAGVRepeatConnection2RCS();

                        setAGVStatusOnTCPConnectionReOpen();
                    }
                }
                return;
            }
        }
    }


    public synchronized void checkAGVReConnection(long robotID, MachineInterface machine, long addressCodeID, IoSession session) {
        kivaConfigToolModifyManager.modifyKivaAGV_checkAGVReConnection(new IOriginCodes() {
            @Override
            public void usingOriginCodes() {
                checkAGVReConnection(robotID);
            }
        }, machine, addressCodeID, session, this);
    }





    /*
     检测指定的路径是否可走
     */
    public CellNode checkSeriesPathWalked(SeriesPath sp) {
        List<CellNode> unWalkedCellNodeList = MapManager.getInstance().getMap().getTmpUnWalkedCellNodeList();
        for (CellNode cellNode : sp.getPathList()) {
            if (unWalkedCellNodeList.contains(cellNode)) {
                LOG.info("AGV("+getID()+")路径("+sp+")中出现不可走点("+cellNode.getAddressCodeID()+")!!!");
                return cellNode;
            }
        }
        return null;
    }
//    //当AGV锁格超时
//    public void onAGVLockCellTimeout() {
//            //--((KivaAGV)this).clearRCSBufferPathCommand();
//            //--setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);
//            //fireOnAGVRequestWCSPath();
//    }
    

    public String toString() {
        String agvStr = "[";
        agvStr += "ID=" + getID() + ",";
        agvStr += "IP=" + getIP() + ",";
        agvStr += "PORT=" + getPort() + ",";
        if(getCurrentCellNode() != null){
            agvStr += "currentAdd=" + getCurrentCellNode().getAddressCodeID() + ",";
        }
        agvStr += "isLogin=" + isLogin() + ",";
        agvStr += "isTimeout=" + isRtTimeout() + ",";
        agvStr += "isStopSendSeriesPath=" + isStopSendSeriesPathFlag() + ",";
        agvStr += "status=" + getAGVStatusInfo() + ",";
        agvStr += "session open=" + getSession().toString();
        agvStr += "]";
        return agvStr;
    }

    /**
     * @return the KivaAGVBeforeNoTCPConnectionStatus
     */
//    public int getKivaAGVBeforeNoTCPConnectionStatus() {
//        return KivaAGVBeforeNoTCPConnectionStatus;
//    }
//
//    /**
//     * @param KivaAGVBeforeNoTCPConnectionStatus the
//     * KivaAGVBeforeNoTCPConnectionStatus to set
//     */
//    public void setKivaAGVBeforeNoTCPConnectionStatus(int KivaAGVBeforeNoTCPConnectionStatus) {
//        this.KivaAGVBeforeNoTCPConnectionStatus = KivaAGVBeforeNoTCPConnectionStatus;
//    }

    /**
     * @return the sendSPCountForTimeout
     */
    public int getSendSPCountForTimeout() {
        return sendSPCountForTimeout;
    }

    /**
     * @param sendSPCountForTimeout the sendSPCountForTimeout to set
     */
    public void setSendSPCountForTimeout(int sendSPCountForTimeout) {
        this.sendSPCountForTimeout = sendSPCountForTimeout;
    }


    @Override
    public void onReceiveRobot2RCSActionFinishedMessageListener(Robot2RCSActionFinishedMessage data) {
        if (data.getRobotID() == getID()) {
            RCS2RobotActionResponseMessage actionResponseMessage = new RCS2RobotActionResponseMessage(getID());
            actionResponseMessage.setCommandWordBack((byte) (data.getActedType() & 0xff));
            actionResponseMessage.toMessage();
            sendMessageToAGV(actionResponseMessage);
            LOG.info("收到动作完成数据包，做出回复:"+HexBinaryUtil.byteArrayToHexString2((byte[]) actionResponseMessage.getMessage()));

            if(data.getActedType() == 3){ // 转弯完成
                setRotationFinished(true);
                LOG.info("AGV("+getID()+")在地址码("+data.getAddressCodeID()+")转弯完成 isRotationFinished="+isRotationFinished());
            }

            // 命令码不在此
            /*if(data.getActedType() == 12){
                setPathClearActionFinished(true);
                LOG.info("AGV("+getID()+")在地址码("+data.getAddressCodeID()+")路径清空完成 isPathClearActionFinished="+isPathClearActionFinished());
            }*/
            /*if(data.getActedType() == 5){ // 顶升完成
                setLift(true);
                PodManager.getInstance().getPod(data.getPodCodeID()).setCellNode(null); // 顶升完成将pod从地图移除
                LOG.info("AGV("+getID()+")顶升完成，将POD("+data.getPodCodeID()+")从地图中("+data.getAddressCodeID()+")移除， 将小车置为顶升状态 isLift="+isLift());
            }
            if(data.getActedType() == 6){ // 下降完成
                setLift(false);
                PodManager.getInstance().getPod(data.getPodCodeID()).setCellNode(MapManager.getInstance().getMap().getMapCellByAddressCodeID(data.getAddressCodeID())); // 下降完成将pod放在地图中
                LOG.info("AGV("+getID()+")下降完成，将POD("+data.getPodCodeID()+")放在地图中("+data.getAddressCodeID()+")， 将小车置为下降状态 isLift="+isLift());
            }*/

            /*if(data.getActedType() == 6){ // 下降完成 -- 更新对应的pod到地图中
                IPod pod = PodManager.getInstance().getPod(data.getPodCodeID());
                if(pod == null && data.getPodCodeID() != 0){
                    PodManager.getInstance().addPod2Container(new Pod(data.getPodCodeID(), MapManager.getInstance().getMap().getMapCellByAddressCodeID(data.getAddressCodeID())));
                }
            }*/



        }
    }


    @Override
    public void onReceiveRobot2RCSMediaErrorMessageListener(Robot2RCSMidiaErrorMessage data) {

    }

    @Override
    public void onReceiveRobot2RCSResponseConfigMessageListener(RobotResponseConfigMessage data) {

    }
}
