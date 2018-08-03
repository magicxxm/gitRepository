package mq;

import com.aricojf.platform.mina.message.robot.*;
import com.mingchun.mu.aricojf.platform.mina.message.robot.media.error.MediaErrorUtil;
import com.mingchun.mu.aricojf.platform.mina.message.robot.media.error.Robot2RCSMidiaErrorMessage;
import com.mingchun.mu.mushiny.common.ConstantErrorCode;
import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.AGVMessage;
import com.mushiny.rcs.server.KivaAGV;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * On 2017/6/30.
 *
 */
public class MQPublisher extends MQManager
{

    private static Logger LOG = LoggerFactory.getLogger(MQPublisher.class.getName());

    private ClearSendPath clearSendPath = new ClearSendPath();

    //1.实时消息发布
    public void publishRTMessage(RobotRTMessage data) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", data.getRobotID());
        message.put("rtTime", System.currentTimeMillis());
        message.put("addressCodeID", data.getAddressCodeID());
        message.put("addressCodeInfoX", data.getAddressCodeInfoX());
        message.put("addressCodeInfoY", data.getAddressCodeInfoY());
        message.put("addressCodeInfoTheta", data.getAddressCodeInfoTheta());
        message.put("podCodeID", data.getPodCodeID());
        message.put("podCodeInfoX", data.getPodCodeInfoX());
        message.put("podCodeInfoY", data.getPodCodeInfoY());
        message.put("podCodeInfoTheta", data.getPodCodeInfoTheta());
        message.put("sectionID", sectionID);
        message.put("speed", data.getSpeed());


        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_ROBOT_RT, null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->实时消息(AGV("+data.getRobotID()+"),addressCodeID("+data.getAddressCodeID()+"))："+message);
    }


    //2.状态消息发布
    public void publishStatusMessage(RobotStatusMessage data) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", data.getRobotID());
        message.put("sectionID", sectionID);
        message.put("statusTime", System.currentTimeMillis());

        message.put("laveBattery", data.getShengYuDianLiang());
        message.put("leftWheelMotorTemperature", data.getLeftWheelMotorTemperature());
        message.put("rightWheelMotorTemperature", data.getRightWheelMotorTemperature());
        message.put("rotationMotorTemperature", data.getRotationMotorTemperature());
        message.put("upMotorTemperature", data.getUpMotorTemperature());
        message.put("batteryTemperatureOne", data.getBatteryTemperatureOne());
        message.put("batteryTemperatureTwo", data.getBatteryTemperatureTwo());
        message.put("voltage", data.getBatteryVoltage() * 10);

        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_ROBOT_STATUS, null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->状态消息发布(AGV("+data.getRobotID()+"),laveBattery("+data.getShengYuDianLiang()+"))："+message);
    }
    //3.错误消息发布
    public void publishErrorMessage(RobotErrorMessage data) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", data.getRobotID());
        message.put("errorTime", System.currentTimeMillis());
        message.put("errorID", ConstantErrorCode.getConvertErrorCode(data.getErrorID()));
        message.put("errorStatus", data.getErrorStatus());
        message.put("podCodeID", data.getPreData());
        message.put("curPodID", data.getCurData());
        message.put("sectionID", sectionID);
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_ROBOT_ERROR, null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->错误消息发布(AGV("+data.getRobotID()+"))："+message);

        if(data.getErrorID() == ConstantErrorCode.POD_UNMATCH_ERROR_CODE
                || data.getErrorID() == ConstantErrorCode.POD_UNFIND_ERROR_CODE){
            Map<String, Object> scanPodErrorMessage = new HashMap<>();
            scanPodErrorMessage.put("robotID", data.getRobotID());
            scanPodErrorMessage.put("time", System.currentTimeMillis());
            scanPodErrorMessage.put("podCodeID", data.getPreData()); //要求的PODID
            scanPodErrorMessage.put("scanedPodID", data.getCurData()); //扫描的PODID
            scanPodErrorMessage.put("sectionID", sectionID);
            if(AGVManager.getInstance().getAGVByID(data.getRobotID()) != null){
                scanPodErrorMessage.put("addressCodeID", AGVManager.getInstance().getAGVByID(data.getRobotID()).getCurrentAddressCodeID());
            }
            channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_POD_ERRORPLACE, null, SerializationUtils.serialize((Serializable) scanPodErrorMessage));
            LOG.info("消息推送到mq-->扫不到pod错误消息发布(AGV("+data.getRobotID()+"))："+message);
        }

    }
    //4.心跳消息发布
    public void publishHeartBeatMessage(RobotHeartBeatRequestMessage data) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", data.getRobotID());
        message.put("heartBeatTime", System.currentTimeMillis());
        message.put("addressCode", data.getAddressCodeID());
        message.put("sectionID", sectionID);
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_ROBOT_HEART_BEAT, null, SerializationUtils.serialize((Serializable) message));
//        LOG.info("消息推送到mq-->心跳消息发布："+message);

    }
    //5.登录消息发布
    public void publishLoginMessage(RobotLoginRequestMessage data) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", data.getRobotID());
        message.put("loginTime", System.currentTimeMillis());
        message.put("password", data.getPassword());
        message.put("robotType", data.getRobotType());
        message.put("hardwareVersion", data.getHardvareVersion());
        message.put("softwareVersion", data.getSoftVersion());
        message.put("manufactureDate", data.getChuChangDate());
        message.put("TotalTime", data.getLeiJiShiChang());
        message.put("recentFixDate", data.getZuiJinWeiXiuShiJian());
        message.put("laveBattery", data.getShengYuDianLiang());
        message.put("addressCodeID", data.getAddressCodeID());
        message.put("addressCodeInfoX", data.getAddressCodeInfoX());
        message.put("addressCodeInfoY", data.getAddressCodeInfoY());
        message.put("addressCodeInfoTheta", data.getAddressCodeInfoTheta());
        message.put("podCodeID", data.getPodCodeID());
        message.put("podCodeInfoX", data.getPodCodeInfoX());
        message.put("podCodeInfoY", data.getPodCodeInfoY());
        message.put("podCodeInfoTheta", data.getPodCodeInfoTheta());
        message.put("selectedPodCodeID", data.getSelectedPodCodeID());
        message.put("upStatus", data.getJuShengZhuangTai());
        message.put("chargeStatus", data.getChongDianZhuangTai());
        message.put("errorID", data.getErrorID());
        message.put("signalQuality", data.getXinHaoZhiLiang());
        message.put("invasionDetection", data.getRuQinJianCeCeShu());
        message.put("coolReset", data.getCoolReset());
        message.put("hotReset", data.getHotReset());
        message.put("sectionID", sectionID);
        message.put("voltage", 58000);
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_ROBOT_LOGIN, null,
                SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->登录消息发布(AGV("+data.getRobotID()+"))："+message);

    }
    //6.ROBOT连接到RCS SERVER
    public void publishAGVConnect2RCS(AGVMessage agv, String topicName)throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        createAGVCommonMessage(agv, message);
        channel.basicPublish(COM_EXCHANGE, topicName, null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->ROBOT(AGV("+agv.getID()+"))连接到RCS："+message);

    }
    //7.ROBOT关闭到RCS SERVER
    public void publishCloseConnectionMessage(long robotID) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", robotID);
        KivaAGV kivaAGV = AGVManager.getInstance().getAGVByID(robotID);
        if(kivaAGV != null){
            message.put("ip", kivaAGV.getIP());
            message.put("port", kivaAGV.getPort());
        }
        message.put("time", System.currentTimeMillis());
        message.put("sectionID", sectionID);
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_ROBOT_CLOSE_CONNECTION, null,
                SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->ROBOT(AGV("+robotID+"))关闭到RCS："+message);

    }
    //发布地图信息请求
    public void publishVirtualMapRequest() throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        message.put("name", SubjectManager.RCS_MAP_REQUEST);
        message.put("requestTime", System.currentTimeMillis());
        message.put("sectionID", sectionID);
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_MAP_REQUEST, null,
                SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->发布地图信息请求："+message);

    }
    //AGV状态事件
    public void publishAGVStatus(long robotID,  int oldStatus, int newStatus, long time) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", robotID);
        KivaAGV kivaAGV = AGVManager.getInstance().getAGVByID(robotID);
        if(kivaAGV != null){
            message.put("ip", kivaAGV.getIP());
            message.put("port", kivaAGV.getPort());
        }
        message.put("newStatus", newStatus);
        message.put("AGVStatusInfo", AGVConfig.getAGVStatusInfo(newStatus));
        message.put("oldStatus", oldStatus);
        message.put("time", time);
        message.put("sectionID", sectionID);
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_AGV_STATUS, null,
                SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->AGV("+robotID+")状态事件："+message);

    }

    //AGV位置改变
    public void publishAGVPositionChange(long robotID,  long oldAddressIDCode, long newAddressIDCode, long time) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", robotID);
        message.put("previousAddress", oldAddressIDCode);
        message.put("currentAddress", newAddressIDCode);
        message.put("time", time);
        message.put("sectionID", sectionID);
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_AGV_POSITION_CHANGE, null,
                SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->AGV("+robotID+")位置改变："+message);

    }



    /**
     * AGV位置不改变超时
     * @param agv
     * @param paramMap
     * @throws IOException
     */
    public void publishAGVNoMoveTimeOut(KivaAGV agv, Map<String, Object> paramMap) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        createAGVCommonMessage(agv, message);
        message.put("lockedCellList", paramMap.get("lockedCellList"));
        channel.basicPublish(COM_EXCHANGE, SubjectManager.AGV_NOMOVE_TIMMEOUT, null,
                SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->AGV("+agv.getID()+")位置不改变超时："+message);
    }

    //AGV动作指令回复
    public void publishAGVActionCommondResponse(Long robotID, String topicName) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", robotID);
        message.put("sectionID", sectionID);
        message.put("time", System.currentTimeMillis());
        channel.basicPublish(COM_EXCHANGE, topicName, null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->AGV("+robotID+")动作指令回复："+message);

    }

    //AGV心跳或者实时包收到超时
    public void publishAGVBeatOrRTTimeout(Long robotID) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", robotID);
        message.put("sectionID", sectionID);
        message.put("time", System.currentTimeMillis());
        channel.basicPublish(COM_EXCHANGE, SubjectManager.AGV_HEART_RT_TIMEOUT,
                null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->AGV("+robotID+")心跳或者实时包收到超时："+message);

    }

    //AGV重新连接并且位置错误
    public void publishAGVReConnedAndPositionError(AGVMessage agv) throws IOException
    {
        Map<String, Object> message = new HashMap<>();
        createAGVCommonMessage(agv, message);
        channel.basicPublish(COM_EXCHANGE, SubjectManager.AGV_REPEAT_CONNECT2_RCS_POSITION_ERROR,
                null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->AGV("+agv.getID()+")重新连接并且位置错误："+message);

    }

    //AGV锁格超时或重新请求路径
    public void publishLockCellTimeoutOrRequestPath(Long robotID, long currentAddress, long waitingLockAddressCodeID, String topic) throws IOException {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", robotID);
        message.put("sectionID", sectionID);
        message.put("time", System.currentTimeMillis());
        message.put("currentAddress", currentAddress);
        if(PodManager.getInstance().getPod(MapManager.getInstance().getMap().getMapCellByAddressCodeID(waitingLockAddressCodeID)) != null){
            message.put("nextLockAddressPodCodeID", PodManager.getInstance().getPod(MapManager.getInstance().getMap().getMapCellByAddressCodeID(waitingLockAddressCodeID)).getPodCodeID());
            message.put("nextLockAddress", -1);
            CellNode cellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(waitingLockAddressCodeID);
            if(cellNode != null
                    && cellNode.isLocked()){
                message.put("nextLockAddress", cellNode.getAddressCodeID());
            }
        }else {
            AGVManager.getInstance().getAGVByID(robotID).clearBufferSP(); // 前面没有pod的清除之前路径
            message.put("nextLockAddress", waitingLockAddressCodeID);
        }
        channel.basicPublish(COM_EXCHANGE, topic,
                null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->AGV("+robotID+")锁格超时或重新请求路径："+message);
    }

    //AGV当前路径中有临时不可走点
    public void publishTempUnWalkCell(AGVMessage agvMessage, long unWalkCell) throws IOException {
        Map<String, Object> message = new HashMap<>();
        message.put("sectionID", sectionID);
        message.put("robotID", agvMessage.getID());
        message.put("currentAddress", agvMessage.getCurrentAddressCodeID());
        message.put("unWalkCell", unWalkCell);
        message.put("time", System.currentTimeMillis());
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_SP_UNWALK_CELL,
                null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->AGV("+agvMessage.getID()+")当前路径中有临时不可走点："+message);

    }

    private void createAGVCommonMessage(AGVMessage agv, Map<String, Object> message)
    {
        message.put("robotID", agv.getID());
        message.put("port", agv.getPort());
        message.put("ip", agv.getIP());
        message.put("sectionID", sectionID);
        message.put("currentAddress", agv.getCurrentAddressCodeID());
        message.put("time", System.currentTimeMillis());
    }


    // 2017 09 04
    // mingchun.mu@mushiny.com
    // 初始化地图 扫描地图上pod的位置
    public void publishInitPodInfo(Robot2RCSActionFinishedMessage message_) throws IOException
    {
        if(message_.getActedType() == 4){
            Map<String, Object> message = new HashMap<>();
            message.put("robotID", message_.getRobotID());
            message.put("sectionID", sectionID);
            message.put("addressCodeID", message_.getAddressCodeID());//完成动作时，小车所在地址码 4byte
            message.put("podCodeID", message_.getPodCodeID());//货架码ID 4byte
            message.put("podCodeInfoTheta", message_.getPodAfaceToward());// 货架A面朝向
            message.put("time", System.currentTimeMillis());
            channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_SCAN_POD,
                    null, SerializationUtils.serialize((Serializable) message));
            LOG.info("消息推送到mq-->初始化地图 AGV("+message_.getRobotID()+")扫描地图上pod的位置："+message);
        }

    }
    // 2017 09 04
    // mingchun.mu@mushiny.com
    // 动作完成信息推送
    public void publishActionFinishedInfo(Robot2RCSActionFinishedMessage message_) throws IOException
    {

        Map<String, Object> message = new HashMap<>();
        message.put("robotID", message_.getRobotID());
        message.put("sectionID", sectionID);
        message.put("actedType", message_.getActedType());// 完成动作类型 1byte
        message.put("addressCodeID", message_.getAddressCodeID());//完成动作时，小车所在地址码 4byte
        message.put("robotHeadToward", message_.getRobotHeadToward());//车头朝向 2byte
        message.put("podCodeID", message_.getPodCodeID());//货架码ID 4byte
        message.put("podAfaceToward", message_.getPodAfaceToward());// 货架A面朝向
        message.put("robotCurStatus", message_.getRobotCurStatus());// 小车当前状态
        message.put("robotCurStatusFlagBit", message_.getRobotCurStatusFlagBit());// 小车状态标识位
        message.put("time", System.currentTimeMillis());

        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_ACTION_FINISHED_COMMAND,
                null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->AGV("+message_.getRobotID()+")动作码完成信息："+message);

        if(message_.getActedType() == (byte) 0x08){
            publishStartCharging(message_.getAddressCodeID(), AGVManager.getInstance().getAGVByID(message_.getRobotID()));
        }

    }
    // mingchum.mu@mushiny.com  小车对接完成，通知充电桩可以开始充电
    public void publishStartCharging(long addressCodeID, KivaAGV kivaAGV) throws IOException{
        Map<String, Object> message = new HashMap<>();
        message.put("chargingAddressCodeID", addressCodeID);
        message.put("batteryVoltage", kivaAGV.getBatteryVoltage());
        message.put("batterManufacturerNumber", kivaAGV.getBatterManufacturerNumber());
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_CHARGING_PILE_START_CHARGING,
                null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->通知充电桩（地址码="+addressCodeID+"）开始充电！！！");
    }


    // 2017/09/22 11:15
    // mingchun.mu@mushiny.com
    // 到达充电点  要求充电桩准备对接
    public void publishChargingPileConnecting(long robotID, long chargingAddressCodeID) throws IOException
    {
        // mingchun.mu@mushiny.com  通知充电桩准备对接-----------------
        Map<String, Object> chargingPileMap = new HashMap<>();
        chargingPileMap.put("robotID", robotID);
        chargingPileMap.put("chargingAddressCodeID", chargingAddressCodeID);
        chargingPileMap.put("batterManufacturerNumber", AGVManager.getInstance().getAGVByID(robotID).getBatterManufacturerNumber());
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_CHARGING_PILE_START_CONNECTING, null,
                SerializationUtils.serialize((Serializable) chargingPileMap));
        LOG.info("消息推送到mq-->通知充电桩（地址码="+chargingAddressCodeID+"）准备对接！！！");
        // mingchun.mu@mushiny.com -----------------
    }


    /**
     * 美的错误响应
     * @param message_
     * @throws IOException
     */
    public void publishMediaErrorMessage(Robot2RCSMidiaErrorMessage message_) throws IOException {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", message_.getRobotID());
        message.put("sectionID", sectionID);
        message.put("time", System.currentTimeMillis());

        message.put("generalErrorID", message_.getGeneralErrorID());
        message.put("generalErrorList", MediaErrorUtil.getGeneralError(message_.getGeneralErrorID()));
        message.put("commonErrorID", message_.getCommonErrorID());
        message.put("commonErrorList", MediaErrorUtil.getCommonError(message_.getCommonErrorID()));
        message.put("seriousErrorID", message_.getSeriousErrorID());
        message.put("seriousErrorList", MediaErrorUtil.getSeriousError(message_.getSeriousErrorID()));
        message.put("logicErrorID", message_.getLogicErrorID());
        message.put("logicErrorList", MediaErrorUtil.getLogicError(message_.getLogicErrorID()));
        message.put("errorStatus", message_.getErrorStatus());

                channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_RESPONSE_MEDIA_ERROR,
                null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->Media故障数据包(AGV("+message_.getRobotID()+"))："+message);

    }

    /**
     * 配置信息回复
     * @param data
     * @throws IOException
     */
    public void publishResponseConfigMessage(RobotResponseConfigMessage data) throws IOException {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", data.getRobotID());
        message.put("sectionID", sectionID);
        message.put("time", System.currentTimeMillis());
        message.put("matchWord", data.getMatchWord());
        message.put("straightSpeed1", data.getStraightSpeed1());
        message.put("straightSpeed2", data.getStraightSpeed2());
        message.put("straightSpeed3", data.getStraightSpeed3());
        message.put("straightSpeed4", data.getStraightSpeed4());
        message.put("straightSpeed5", data.getStraightSpeed5());
        message.put("cornerSpeed1", data.getCornerSpeed1());
        message.put("cornerSpeed2", data.getCornerSpeed2());
        message.put("cornerSpeed3", data.getCornerSpeed3());
        message.put("acceleration", data.getAcceleration());
        message.put("dragAcceleration", data.getDragAcceleration());
        message.put("XP", data.getXP());
        message.put("XI", data.getXI());
        message.put("XD", data.getXD());
        message.put("thetaP", data.getThetaP());
        message.put("thetaI", data.getThetaI());
        message.put("thetaD", data.getThetaD());
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_RESPONSE_MEDIA_AGV_CONFIG_PARAMETERS,
                null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->AGV("+data.getRobotID()+")回读配置数据包："+message);
    }

    /**
     * 动作命令回复
     * @param data
     * @throws IOException
     */
    public void publishActionCommandMessage(Robot2RCSActionCommandResponseMessage data) throws IOException {
        Map<String, Object> message = new HashMap<>();
        message.put("robotID", data.getRobotID());
        message.put("sectionID", sectionID);
        message.put("time", System.currentTimeMillis());
        int actionType = -1;
        String actionTypeDesc = "";
        switch (data.getCommandCode()){
            case (byte) (0x01):
                actionType = 0;
                actionTypeDesc = "启动 -- 命令执行完成";
                break;
            case (byte)(0xf0):
                actionType = 1;
                actionTypeDesc = "停到最近的二维码，减速到0 -- 命令执行完成";
                break;
            case (byte) (0xf1):
                actionType = 2;
                actionTypeDesc = "急停 -- 命令执行完成";
                break;
            case (byte) (0xf2):
                actionType = 3;
                actionTypeDesc = "所有电机供电断电 -- 命令执行完成";
                break;
            case (byte)(0x10):
                actionType = 4;
                actionTypeDesc = "旋转(托盘固定) -- 命令执行完成";
                break;
            case (byte) (0x11):
                actionType = 5;
                actionTypeDesc = "旋转(托盘跟着转动) -- 命令执行完成";
                break;
            case (byte) (0x12):
                actionType = 6;
                actionTypeDesc = "旋转(托盘固定，且附带顶升) -- 命令执行完成";
                break;
            case (byte)(0x13):
                actionType = 7;
                actionTypeDesc = "旋转(托盘固定，且附带降落) -- 命令执行完成";
                break;
            case (byte) (0x20):
                actionType = 8;
                actionTypeDesc = "顶升 -- 命令执行完成";
                break;
            case (byte) (0x21):
                actionType = 9;
                actionTypeDesc = "下降 -- 命令执行完成";
                break;
            case (byte)(0x40):
                actionType = 10;
                actionTypeDesc = "开始休眠 -- 命令执行完成";
                break;
            case (byte) (0x41):
                actionType = 11;
                actionTypeDesc = "结束休眠 -- 命令执行完成";
                break;
            case (byte) (0x50):
                actionType = 12;
                actionTypeDesc = "已经清空下发的路径节点 -- 命令执行完成";
                break;
            case (byte)(0x30):
                actionType = 13;
                actionTypeDesc = "解锁 -- 命令执行完成";
                break;
            case (byte) (0x60):
                actionType = 14;
                actionTypeDesc = "锁定 -- 命令执行完成";
                break;
            default:
                break;
        }
        message.put("actionType", actionType);
        message.put("actionTypeDesc", actionTypeDesc);
        message.put("actionValue", data.getCommandParameter());
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_RESPONSE_ACTION_COMMAND,
                null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->AGV("+data.getRobotID()+")命令码回复数据包："+message);
    }

    // 发送解锁的格子
    public void publishUnlockedCell(long addressCodeID) throws IOException {
        Map<String, Object> message = new HashMap<>();
        message.put("sectionID", sectionID);
        message.put("time", System.currentTimeMillis());
        List<Long> addressCodeIDList = new ArrayList<>();
        addressCodeIDList.add(addressCodeID);
        message.put("unlockedCellList", addressCodeIDList);
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_UNLOCKED_CELL_LIST,
                null, SerializationUtils.serialize((Serializable) message));
        LOG.info("消息推送到mq-->解锁格子："+message);
    }

    public void publishAddressCodeIdLostInfo(long robotID, String lostMsg) throws IOException{
        Map<String, Object> message = new HashMap<>();
        message.put("sectionID", sectionID);
        message.put("time", System.currentTimeMillis());
        message.put("robotID", robotID);
        message.put("lostMsg", lostMsg);
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_ADDRESS_CODE_ID_LOST_INFO,
                null, SerializationUtils.serialize((Serializable) message));
    }

    public void publishRequestPath(AGVMessage agvMessage) throws IOException {
        Map<String, Object> message = new HashMap<>();
        message.put("sectionID", sectionID);
        message.put("time", System.currentTimeMillis());
        message.put("robotID", agvMessage.getID());
        message.put("addressCodeID", agvMessage.getCurrentAddressCodeID());
        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_AGV_REQUEST_PATH,
                null, SerializationUtils.serialize((Serializable) message));
        LOG.info("AGV("+agvMessage.getID()+")遇到不可走点，或遇到cost值增大点，重新请求路径！！！");
    }
}
