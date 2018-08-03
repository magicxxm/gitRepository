/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.aricojf.platform.mina.common.Client;
import com.mushiny.kiva.map.CellNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AGV属性
 *
 * @author 陈庆余 <13469592826@163.com>
 */
public class AGVProperty extends Client {

    private Logger LOG = LoggerFactory.getLogger(AGVProperty.class.getName());
    private boolean login = false;
    private boolean rtTimeout = false;
    private boolean taskTimeout = false;
    private volatile boolean lockTimeout = false;


    private long sendedPathTime = System.currentTimeMillis(); // 发送路径的时间
    private int pathResponse = -1; // 路径应答： -1 没有收到路径应答； 0 正常收到路径应答； 1 收到异常路径应答
    /*
    是否处于举升状态

    到达举升点就将小车置为举升状态(小车到达举升点不举升，表明没有扫到pod，清除路径，将小车置为下降状态)， 到达下降点就将小车置为下降状态。
     */
    private boolean isLift;

    private boolean isRotationFinished; // 是否旋转完成
    private boolean isRequestPath; // 是否已经重新请求了一次路径 -- 获取路径仅请求一次

    private boolean isPathClearActionFinished; // 小车是否清除动作完成
    private boolean isSendClearActionCommand; // 是否下发清除动作命令

    private long firstNodeSendTime; // 路径第一个点下发时间

    private volatile int batteryVoltage; // 电池电压
    private volatile int batterManufacturerNumber; // 制造厂商编号


    private int lockCellsNum; // 锁格数目


    //------------------是否DEBUG------------------
    private boolean debug = true;

    private long previousAddressCodeID = 0;
    protected int addressCodeIDCount = 0;//previousAddressCodeID和currentAddressCodeID相同次数计数
    protected long currentAddressCodeID = 0;
    protected int maxAddressCodeIDCount = 20;
    protected CellNode previousCellNode;
    protected CellNode currentCellNode;//目前AGV所在的CELL
    protected CellNode lockPathTargetCellNode;
    protected CellNode globalPathFirstCellNode;
    protected CellNode globalPathTargetCellNode;
    protected CellNode globalPathTargetSecondCellNode;
    
    protected long globalPathTargetAddressCodeID;
    protected long globalPathTargetSecondeAddressCodeID;
    
    protected short addressCodeInfoX;//地址码信息，X坐标，2字节有符号整数
    protected short addressCodeInfoY;//地址码信息，Y坐标，2字节有符号整数
    protected float addressCodeInfoTheta;//地址码信息，θ角度，为4字节单精度浮点型
    protected long podCodeID;//货架码ID
    protected short podCodeInfoX;//货架码信息，X坐标，2字节有符号整数
    protected short podCodeInfoY;//货架码信息，Y坐标，2字节有符号整数
    protected float podCodeInfoTheta;//货架码信息，θ角度，为4字节单精度浮点
    protected short speed;//
    protected int shengYuDianLiang;
    protected long motorTemperature;
    protected int errorID;
    protected short errorStatus;
    protected long password;
    protected short robotType;
    protected long selectedPodCodeID;//绑定货架ID
    protected short juShengZhuangTai;//举升状态
    protected short chongDianZhuangTai;//充电状态
    protected short xinHaoZhiLiang;//信号质量
    protected int ruQinJianCeCeShu;//入侵检测次数
    protected int coolReset;//冷复位次数
    protected int hotReset;//热复位次数

    public AGVProperty() {
        super();

    }

   

    public long getCurrentAddressCodeID() {
        return currentAddressCodeID;
    }
    public void setCurrentAddressCodeID(long currentAddressCodeID) {
        this.currentAddressCodeID = currentAddressCodeID;
    }
    public short getAddressCodeInfoX() {
        return addressCodeInfoX;
    }
    public void setAddressCodeInfoX(short addressCodeInfoX) {
        this.addressCodeInfoX = addressCodeInfoX;
    }
    public short getAddressCodeInfoY() {
        return addressCodeInfoY;
    }
    public void setAddressCodeInfoY(short addressCodeInfoY) {
        this.addressCodeInfoY = addressCodeInfoY;
    }
    public float getAddressCodeInfoTheta() {
        return addressCodeInfoTheta;
    }
    public void setAddressCodeInfoTheta(float addressCodeInfoTheta) {
        this.addressCodeInfoTheta = addressCodeInfoTheta;
    }
    public long getPodCodeID() {
        return podCodeID;
    }
    public void setPodCodeID(long podCodeID) {
        this.podCodeID = podCodeID;
    }
    public short getPodCodeInfoX() {
        return podCodeInfoX;
    }
    public void setPodCodeInfoX(short podCodeInfoX) {
        this.podCodeInfoX = podCodeInfoX;
    }
    public short getPodCodeInfoY() {
        return podCodeInfoY;
    }
    public void setPodCodeInfoY(short podCodeInfoY) {
        this.podCodeInfoY = podCodeInfoY;
    }
    public float getPodCodeInfoTheta() {
        return podCodeInfoTheta;
    }
    public void setPodCodeInfoTheta(float podCodeInfoTheta) {
        this.podCodeInfoTheta = podCodeInfoTheta;
    }
    public short getSpeed() {
        return speed;
    }
    public void setSpeed(short speed) {
        this.speed = speed;
    }
    public CellNode getCurrentCellNode() {
        return currentCellNode;
    }
    public void setCurrentCellNode(CellNode currentCellNode) {
        this.previousCellNode = this.currentCellNode;
        this.currentCellNode = currentCellNode;
        //--LOG.debug("=======AGV目前currentCellNode="+currentCellNode);
    }
    public CellNode getPreviousCellNode() {
        return previousCellNode;
    }
    public void setPreviousCellNode(CellNode previousCellNode) {
        this.previousCellNode = previousCellNode;
    }
    public boolean isLogin() {
        return login;
    }
    public void setLogin(boolean login) {
        this.login = login;
    }
    public boolean isRtTimeout() {
        return rtTimeout;
    }
    public void setRtTimeout(boolean rtTimeout) {
        this.rtTimeout = rtTimeout;
    }
    public boolean isDebug() {
        return debug;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    public void showAGVDebugMessage(Logger LOG,String message) {
        if(isDebug()) {
            LOG.debug(message);
        }
    }
    public long getPreviousAddressCodeID() {
        return previousAddressCodeID;
    }
    public void setPreviousAddressCodeID(long previousAddressCodeID) {
        this.previousAddressCodeID = previousAddressCodeID;
    }
    public boolean isTaskTimeout() {
        return taskTimeout;
    }
    public void setTaskTimeout(boolean taskTimeout) {
        this.taskTimeout = taskTimeout;
    }
    public boolean isLockTimeout() {
        return lockTimeout;
    }
    public void setLockTimeout(boolean lockTimeout) {
        this.lockTimeout = lockTimeout;
    }
    public long getSendedPathTime() {
        return sendedPathTime;
    }
    public void setSendedPathTime(long sendedPathTime) {
        this.sendedPathTime = sendedPathTime;
    }
    public int getPathResponse() {
        return pathResponse;
    }
    public void setPathResponse(int pathResponse) {
        this.pathResponse = pathResponse;
    }
    public boolean isLift() {
        return isLift;
    }
    public void setLift(boolean lift) {
        isLift = lift;
    }

    public boolean isRotationFinished() {
        return isRotationFinished;
    }

    public void setRotationFinished(boolean rotationFinished) {
        isRotationFinished = rotationFinished;
    }

    public boolean isRequestPath() {
        return isRequestPath;
    }

    public void setRequestPath(boolean requestPath) {
        isRequestPath = requestPath;
    }

    public long getFirstNodeSendTime() {
        return firstNodeSendTime;
    }

    public void setFirstNodeSendTime(long firstNodeSendTime) {
        this.firstNodeSendTime = firstNodeSendTime;
    }

    public boolean isPathClearActionFinished() {
        return isPathClearActionFinished;
    }

    public void setPathClearActionFinished(boolean pathClearActionFinished) {
        isPathClearActionFinished = pathClearActionFinished;
    }

    public boolean isSendClearActionCommand() {
        return isSendClearActionCommand;
    }

    public void setSendClearActionCommand(boolean sendClearActionCommand) {
        isSendClearActionCommand = sendClearActionCommand;
    }

    public int getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(int batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public int getBatterManufacturerNumber() {
        return batterManufacturerNumber;
    }

    public void setBatterManufacturerNumber(int batterManufacturerNumber) {
        this.batterManufacturerNumber = batterManufacturerNumber;
    }

    public int getLockCellsNum() {
        return lockCellsNum;
    }

    public void setLockCellsNum(int lockCellsNum) {
        this.lockCellsNum = lockCellsNum;
    }
}
