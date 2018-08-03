package com.mingchun.mu.manager;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.common.MachineInterface;
import com.aricojf.platform.mina.message.robot.RCS2RobotLoginResponseMessage;
import com.aricojf.platform.mina.message.robot.RobotErrorMessage;
import com.aricojf.platform.mina.message.robot.RobotLoginRequestMessage;
import com.mingchun.mu.mushiny.common.ConstantErrorCode;
import com.mingchun.mu.mushiny.kiva.path.RotationAreaManager;
import com.mingchun.mu.mushiny.kiva.path.TriangleRotateAreaNewManager;
import com.mingchun.mu.mushiny.kiva.pod.IPod;
import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mingchun.mu.mushiny.rcs.server.KivaAGVModifier;
import com.mingchun.mu.mushiny.rcs.wcs.WCSSeriesPathDifferentDistanceCellTool;
import com.mingchun.mu.mushiny.rcs.wcs.WCSSeriesPathSameNodeUpDown;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.listener.RCSListenerManager;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.AGVMessage;
import com.mushiny.rcs.server.KivaAGV;
import com.mushiny.rcs.wcs.WCSSeriesPath;
import com.mushiny.rcs.wcs.WCSeriesPathTool;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kiva配置工具管理器
 * Created by mingchun.mu@mushiny.com on 2017/9/20.
 */
public class KivaConfigToolModifyManager {
    private Logger LOG = LoggerFactory.getLogger(KivaConfigToolModifyManager.class.getName());
    private static KivaConfigToolModifyManager instance = null;
    private KivaConfigToolModifyManager() {
    }
    private synchronized static void initInstance(){
        if(instance == null){
            instance = new KivaConfigToolModifyManager();
        }
    }
    public synchronized static KivaConfigToolModifyManager getInstance(){
        if(instance == null){
            initInstance();
        }
        return instance;
    }
// -----------------------------------------------------------------------------------------------------------------



    private boolean isUsingModifingCodes = true; //是否使用修改过代码块
    private AGVManager agvManager = AGVManager.getInstance();


    /**
     * 上下路径添加podID参数
     *
     * @param originCodes
     * @param params
     */
    public void modifyWCSSeriesPath(IOriginCodes originCodes, Object... params){
        if(!isUsingModifingCodes){
            originCodes.usingOriginCodes();
        }else{
//            WCSeriesPathTool wcsTool = new WCSeriesPathAddPodTool((WCSSeriesPath)params[0]);
//            WCSeriesPathTool wcsTool = new WCSSeriesPathDifferentDistanceCellTool((WCSSeriesPath)params[0]);
            WCSeriesPathTool wcsTool = new WCSSeriesPathSameNodeUpDown((WCSSeriesPath)params[0]);
            wcsTool.analysis();
        }
    }

    /**
     * 通过登录包id不能查到agv  表明agv新登录，否则重新登录
     *
     * @param originCodes
     * @param loginRequestMessage
     * @param agvMessage
     */
    public void modifyAGVMessage_onReceivedAGVLoginMessage(IOriginCodes originCodes, RobotLoginRequestMessage loginRequestMessage, AGVMessage agvMessage){
        if(!isUsingModifingCodes){
            originCodes.usingOriginCodes();
        }else{

            KivaAGV kivaAGV = agvManager.getAGVByID(loginRequestMessage.getRobotID());
            if(kivaAGV != null){ // 小车存在  属于重新登陆

                /*if(agvMessage != null && agvMessage.getID() == 0){  // 只有重登陆才会产生
                    LOG.info("移除多余监听器AGV（ID=0）  LOGING_AGVID=("+loginRequestMessage.getRobotID()+")。。。");
                    RCSListenerManager.getInstance().removeAGVAllMessageListener(agvMessage);
                    RCSListenerManager.getInstance().removeAGVLoginMessageListener(agvMessage);
                    return;
                }*/

                kivaAGV.setSendClearActionCommand(false);
                kivaAGV.setPathClearActionFinished(false);
                LOG.info("AGV("+kivaAGV.getID()+")重新登录，未回复的清除动作命令取消。。。");

                // 发送路径之前先回复登录包， 只有登陆过的小车有可能未走完路径 --------------
                RCS2RobotLoginResponseMessage loginResponseMessage = new RCS2RobotLoginResponseMessage(kivaAGV.getID());
                loginResponseMessage.toMessage();
                loginRequestMessage.getSession().write(loginResponseMessage.getMessage());
                LOG.info("发送路径之前先回复登录包：loginResponseMessage="+ HexBinaryUtil.byteArrayToHexString2((byte[]) loginResponseMessage.getMessage()));
                // 发送路径之前先回复登录包， 只有登陆过的小车有可能未走完路径 --------------
                if(agvMessage.getID() == loginRequestMessage.getRobotID()){ // 合并agv
                    if(agvMessage.getCurrentAddressCodeID() != loginRequestMessage.getAddressCodeID()){
                        // 解锁小车以前位置 (如果解锁路径没有小车位置)
                        if(kivaAGV.getCurrentCellNode() != null){
                            if(kivaAGV.equals(kivaAGV.getCurrentCellNode().getNowLockedAGV())){
                                kivaAGV.getCurrentCellNode().setUnLocked();
                                kivaAGV.getCurrentCellNode().setAGVOut(kivaAGV);
                                LOG.info("解锁小车以前位置 (如果解锁路径没有小车位置)，解锁格子("+kivaAGV.getCurrentCellNode().getAddressCodeID()+")");
                            }
                        }
                    }


                    // 重新发送登陆包统一让wcs在指定的实时包位置发送新路径
                    KivaAGV tempKivaAGV = (KivaAGV) agvMessage;
                    tempKivaAGV.setLastSendedSeriesPath(null);
                    tempKivaAGV.clearBufferSP();
                    LOG.info("收到登陆包，清除AGV("+tempKivaAGV.getID()+")rcs内部所有路径，由wcs在指定实时包重发路径！！！");

                    // 在同一个位置按急停，重新下发，非执行路径中按急停，重启小车
                    if(loginRequestMessage.getSession().equals(agvMessage.getSession())
                            && agvMessage.getCurrentAddressCodeID() == loginRequestMessage.getAddressCodeID()){ // 小车按下急停后重新发送登陆包，必须在当前位置再重发
                        KivaAGV tempAGV = (KivaAGV) agvMessage;
                        if(tempAGV.getLastSendedSeriesPath() != null){
                            tempAGV.sendSeriesPath(tempAGV.getLastSendedSeriesPath());
                        }
                    }else{ // 小车断开连接 重新发送登陆包
                        agvMessage.checkAGVReConnection(loginRequestMessage.getRobotID(), loginRequestMessage.getMachine(), loginRequestMessage.getAddressCodeID(), loginRequestMessage.getSession());// KivaAGV 将会调用子类方法
                        agvMessage.setLift(false);
                        LOG.info("AGV("+agvMessage.getID()+")重新登录，更改举升状态isLift="+agvMessage.isLift());
                    }

                    agvMessage.fireOnReceivedLoginMessage(agvMessage, loginRequestMessage); // 登录回复
                }
            }else{ // 对于新登录的小车
                if (!agvMessage.isLogin() && agvMessage.getSession().equals(loginRequestMessage.getSession())) {
                    agvMessage.setLoginMessage(loginRequestMessage);
                    agvMessage.setID(loginRequestMessage.getRobotID());
                    if (loginRequestMessage.getAddressCodeID() != agvMessage.getCurrentAddressCodeID()) {
                        agvMessage.setPreviousAddressCodeID(agvMessage.getCurrentAddressCodeID());
                        agvMessage.setCurrentAddressCodeID(loginRequestMessage.getAddressCodeID());

                        // 如果刚登陆的车在选转区，锁定旋转区
                        if(TriangleRotateAreaNewManager.getInstance().isInTriangleRotateAreaNew(loginRequestMessage.getAddressCodeID())){
                            TriangleRotateAreaNewManager.getInstance().getTriangleRotateAreaNewByCellNode(loginRequestMessage.getAddressCodeID()).lockRotateArea((KivaAGV) agvMessage);
                        }

                        if(RotationAreaManager.getInstance().isInRotationArea(loginRequestMessage.getAddressCodeID())){
                            RotationAreaManager.getInstance().getRotationArea(loginRequestMessage.getAddressCodeID()).lockRotationArea((KivaAGV) agvMessage);
                        }


                        agvMessage.fireOnAGVPositionChange(agvMessage.getPreviousAddressCodeID(), agvMessage.getCurrentAddressCodeID());
                    }
                    agvMessage.fireOnReceivedLoginMessage(agvMessage, loginRequestMessage); // 登录回复
                }
            }
        }
    }

    public void modifyAGVMessage_onReceivedAGVRTMessage(IOriginCodes originCodes, MachineInterface machine, long addressCodeID, AGVMessage agvMessage) {
        if(!isUsingModifingCodes){
            originCodes.usingOriginCodes();
        }else{
            KivaAGVModifier kivaAGVModifier = new KivaAGVModifier();
        }
    }
    public void modifyKivaAGV_checkAGVReConnection(IOriginCodes originCodes, MachineInterface machine, long addressCodeID, IoSession session, KivaAGV kivaAGV) {
        if(!isUsingModifingCodes){
            originCodes.usingOriginCodes();
        }else{
            KivaAGVModifier kivaAGVModifier = new KivaAGVModifier();
            kivaAGVModifier.checkAGVReConnection(machine, addressCodeID, session, kivaAGV);
        }
    }

    /**
     * 故障码错误修改
     * @param originCodes
     * @param errorMessage
     * @param agv
     */
    public void modifyAGVMessage_onReceivedAGVErrorMessage(IOriginCodes originCodes, RobotErrorMessage errorMessage, AGVMessage agv){
        if(!isUsingModifingCodes){
            originCodes.usingOriginCodes();
        }else{

            errorMessage.toObject();
            if(errorMessage.getErrorID() == ConstantErrorCode.POD_UNMATCH_ERROR_CODE
                    || errorMessage.getErrorID() == ConstantErrorCode.POD_UNFIND_ERROR_CODE){


                LOG.info("没有扫到或不是目的pod，podCodeID="+errorMessage.getCurData());
                IPod pod = PodManager.getInstance().getPod(errorMessage.getCurData());
                if(pod != null){
                    pod.setCellNode(MapManager.getInstance().getMap().getMapCellByAddressCodeID(agv.getCurrentAddressCodeID()));
                    LOG.info("将未扫到的pod("+errorMessage.getCurData()+")更新到原位置("+agv.getCurrentAddressCodeID()+")");
//                    printPodPos();
                }


                agv.errorPodHandle();
                LOG.info("pod扫描错误，清空路径！！！  errorMessage="+errorMessage);

                agv.setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);
            }
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

    public boolean isUsingModifingCodes() {
        return isUsingModifingCodes;
    }
    public void setUsingModifingCodes(boolean usingModifingCodes) {
        isUsingModifingCodes = usingModifingCodes;
    }
}
