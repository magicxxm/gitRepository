package com.mushiny.beans;

import com.mushiny.beans.enums.OrderErrorMessage;
import com.mushiny.beans.order.Order;
import com.mushiny.comm.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.Map;

/**
 * Created by Tank.li on 2017/6/26.
 */
public class Robot extends BaseObject {
    //收到上一条消息的时间，如果多个队列的消息不一样 只接受最新的那条
    private long lastMsgTime;//可能存在并发的问题

    private String pkId;//物理主键

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    private Map queueMsg;

    public Map getQueueMsg() {
        return queueMsg;
    }

    public void setQueueMsg(Map queueMsg) {
        this.queueMsg = queueMsg;
    }

    public long getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(long lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    private Logger logger = LoggerFactory.getLogger("Robot"+this.getRobotId());

    public void debug(String message){
        logger.debug(message);
    }

    private String lastOrderId;//保留上一个执行的OrderID，用于查找
    /*记录三个值 用于查询*/
    private String lastPath;

    private String lastOrderError;

    private String lastOrderPod;

    private Long lockedAddr = 0L;//当前锁定的目标地址，如果为0 表示没锁定

    //目前只在从缓冲点返回存储区时使用 因为数据库异步更新不及时
    private int orderIndex;
    //尝试重新充电时使用
    private int startLaveBattery;//开始充电时的电量
    private int chargingRetryNum;//重试次数，目前最多3次
    private int isChargingRetry;//1代表重试；0无
    //为哪个工作站服务
    private String workStationId;

    public Long getLockedAddr() {
        return lockedAddr;
    }

    public void setLockedAddr(Long lockedAddr) {
        this.lockedAddr = lockedAddr;
    }

    public void setWorkStationId(String workStationId) {
        this.workStationId = workStationId;
    }

    public String getWorkStationId() {
        return workStationId;
    }

    public void setIsChargingRetry(int isChargingRetry) {
        this.isChargingRetry = isChargingRetry;
    }

    public int getIsChargingRetry() {
        return isChargingRetry;
    }

    public void setChargingRetryNum(int chargingRetryNum) {
        this.chargingRetryNum = chargingRetryNum;
    }

    public int getChargingRetryNum() {
        return chargingRetryNum;
    }

    public void setStartLaveBattery(int startLaveBattery) {
        this.startLaveBattery = startLaveBattery;
    }

    public int getStartLaveBattery() {
        return startLaveBattery;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public String getLastPath() {
        return lastPath;
    }

    public void setLastPath(String lastPath) {
        this.lastPath = lastPath;
    }

    public String getLastOrderError() {
        return lastOrderError;
    }

    public void setLastOrderError(String lastOrderError) {
        this.lastOrderError = lastOrderError;
    }

    public String getLastOrderPod() {
        return lastOrderPod;
    }

    public void setLastOrderPod(String lastOrderPod) {
        this.lastOrderPod = lastOrderPod;
    }

    public String getLastOrderId() {
        return lastOrderId;
    }

    public void setLastOrderId(String lastOrderId) {
        this.lastOrderId = lastOrderId;
    }

    @Override
    public Object getId() {
        return this.getPkId();
    }
    public static final String TABLE = "WCS_ROBOT";
    @Override
    public String getTable() {
        return TABLE;
    }

    public static final String IDNAME = "ID";
    //public static final String IDNAME = "ROBOT_ID";

    @Override
    public String getIdName() {
        return IDNAME;
    }

    private boolean persistFlag;//状态是否需要持久化的标记

    public boolean isPersistFlag() {
        return persistFlag;
    }

    public void setPersistFlag(boolean persistFlag) {
        this.persistFlag = persistFlag;
    }

    //状态监听
    private PropertyChangeSupport propertySupport = new PropertyChangeSupport(this);

    public static final int NOTCHECKED = -9;
    public static final int ERROR = -1;
    public static final int SLEEPING = 0;
    public static final int IDLE = 1;
    public static final int WORKING = 2;
    public static final int CHARGING = 3;
    public static final int STOP2NEAREST = 4;//停到最近二维码
    public static final int HB_RT_TIMEOUT = 7; //心跳超时
    public static final int NOMOVETIMEOUT = 8;  //位置改变超时
    public static final int URGENSTOP = 10;  //急停状态
    public static final int ALLMOTORCUTDOWN = 13;  //所有电机断电
    public static final int CLEARAGVCACHEDPATH = 16;  //清空AGV缓存路径
    public static final int CLEARRCSCACHEDPATH = 19;  //清理RCS缓存路径
    public static final int OFFLINE = 21;//断开链接
    public static final int RECONNECTERROR = 24;  //重连错误

    private boolean avaliable;
    //充电时间
    private long chargingTime;

    private long robotId;//发给小车时 是个long型的
    private String sectionId;//小车的当前所在的section
    private String password;
    private String wareHouseId = "DEFAULT";

    //如果没有 就取上一个地址
    private String addressId;
    //小车目标位置
    private String targetAddrCodeId;
    /*
    未检查	                -9
    错误状态	                -1
    等待任务                	1
    睡眠状态                	0
    已赋予任务状态         	2
    充电状态                	3
    停到最近二维码，减速到0	4
    心跳或实时数据超时	        7
    位置不改变超时	        8
    急停状态                	10
    所有电机断电          	13
    清除AGV缓存路径       	16
    清除RCS缓存路径       	19
    断开连接状态          	21
    断线重连状态          	24
    * */

    /**
     * 当前电量
     */
    private int laveBattery;
    //电池电压
    private int voltage;
    //满充时间戳
    private long fullChargeTime;
    //充电次数
    private int chargeNum;
    private boolean fullChargeFlag;
    //登录时间戳
    private long loginTime;
    //电池厂家编号
    private int batteryNumber;

    public int getBatteryNumber() {
        return batteryNumber;
    }

    public void setBatteryNumber(int batteryNumber) {
        this.batteryNumber = batteryNumber;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public boolean getFullChargeFlag() {
        return fullChargeFlag;
    }

    public void setFullChargeFlag(boolean fullChargeFlag) {
        this.fullChargeFlag = fullChargeFlag;
    }

    public long getFullChargeTime() {
        return fullChargeTime;
    }

    public void setFullChargeTime(long fullChargeTime) {
        this.fullChargeTime = fullChargeTime;
    }

    public int getChargeNum() {
        return chargeNum;
    }

    public void setChargeNum(int chargeNum) {
        this.chargeNum = chargeNum;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        if(this.voltage!=voltage){
            this.addKV("VOLTAGE",voltage);
        }
        this.voltage = voltage;
    }

    public int getLaveBattery() {
        return laveBattery;
    }

    public void setLaveBattery(int laveBattery) {
        if(this.laveBattery!=laveBattery){
            this.addKV("LAVEBATTERY",laveBattery);
        }
        this.laveBattery = laveBattery;
    }

    private int oldStatus;

    private int status;
    //是否驮了pod，哪个pod
    private Pod pod;
    /*1A 2B 3C 4D*/

    //当前工作调度单明细
    private Order curOrder;

    /*===================以下是get/set===========================================*/

    public String getTargetAddrCodeId() {
        return targetAddrCodeId;
    }

    public Order getCurOrder() {
        return curOrder;
    }

    public void setCurOrder(Order curOrder) {
        this.curOrder = curOrder;
    }

    public void setTargetAddrCodeId(String targetAddrCodeId) {
        this.targetAddrCodeId = targetAddrCodeId;
    }

    public boolean isAvaliable() {
        return avaliable;
    }

    public void setAvaliable(boolean avaliable) {
        this.avaliable = avaliable;
    }

    public long getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(long chargingTime) {
        this.chargingTime = chargingTime;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getRobotId() {
        return robotId;
    }

    public void setRobotId(long robotId) {
        this.robotId = robotId;
    }

    public String getWareHouseId() {
        if(wareHouseId == null){
            wareHouseId = "DEFAULT";
        }
        return wareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        String oldAddressId = this.addressId;
        this.addressId = addressId;
        this.propertySupport.firePropertyChange("addressId",oldAddressId,addressId);
    }

    public int getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(int oldStatus) {
        if(this.oldStatus != oldStatus){
            this.addKV("OLDSTATUS",oldStatus);
        }
        this.oldStatus = oldStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if(this.status!=status){
            this.addKV("STATUS",status);
        }
        this.status = status;
    }

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
        //this.propertySupport.firePropertyChange("pod",oldPod,pod);
    }



    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }


    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return "Robot{" +
                "avaliable=" + avaliable +
                ", robotId=" + robotId +
                //", sectionId='" + sectionId + '\'' +
                ", lastOrderId='" + lastOrderId + '\'' +
                ", lastOrderError='" + lastOrderError + '\'' +
                ", lastOrderPod='" + lastOrderPod + '\'' +
                ", lastPath='" + lastPath + '\'' +
                ", addressId='" + addressId + '\'' +
                ", loginTime='" + CommonUtils.convert2String(new Date(loginTime), "YYYY-MM-dd HH:mm:ss") + '\'' +
                ", batteryNumber=" + batteryNumber +
                ", laveBattery=" + laveBattery +
                ", voltage=" + voltage +
                ", chargeNum=" + chargeNum +
                ", chargingTime=" + CommonUtils.convert2String(new Date(chargingTime), "YYYY-MM-dd HH:mm:ss") +
                ", fullChargeTime=" + CommonUtils.convert2String(new Date(fullChargeTime), "YYYY-MM-dd HH:mm:ss") +
                ", fullChargeFlag=" + fullChargeFlag +
                ", status=" + status +
                ", pod=" + pod +
                ", curOrder=" + curOrder +
                '}';
    }

    public static void main(String[] args) {
        Robot robot = new Robot();
        robot.setStatus(Robot.CHARGING);
        robot.addPropertyChangeListener(new RobotStatusChangeListener());
        robot.setStatus(Robot.IDLE);//值确切发生变化时 才会调用
    }
}
