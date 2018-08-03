package com.mushiny.beans.order;

import com.mushiny.beans.Charger;
import com.mushiny.beans.Pod;
import com.mushiny.beans.Robot;
import com.mushiny.beans.enums.OrderErrorMessage;
import com.mushiny.beans.enums.TripStatus;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import com.mushiny.mq.ISender;
import com.mushiny.mq.MessageSender;
import org.apache.http.client.utils.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Tank.li on 2017/9/5.
 */
public class ChargerDriveOrder extends Order {

    public static final int CHARGER_MUSHINY = 1;
    public static final int CHARGER_MUSHINY3 = 3;
    public static final int CHARGER_MIDIA = 2;

    private Charger charger;

    public Charger getCharger() {
        return charger;
    }

    public void setCharger(Charger charger) {
        this.charger = charger;
    }

    @Override
    public void sendMessage2Rcs() {
        if(this.wcsPath.getSeriesPath() == null || this.wcsPath.getSeriesPath().size() ==0){
            logger.error("调度单初始化失败，不能发送消息:"+this);
            return;
        }
        Map message = this.genWcsMessage();
        message.put("chargerID",this.getCharger().getRcsChargerId());
        message.put("chargerType",this.getCharger().getChargerType());
        message.put("batterManufacturerNumber", this.getRobot().getBatteryNumber());
        int type = this.getCharger().getChargerType();
        switch (type){
            case CHARGER_MUSHINY :
            case CHARGER_MUSHINY3 :
                MessageSender.sendMapMessage(message, ISender.WCS_RCS_AGV_CHARGE);
                break;
            case CHARGER_MIDIA :
                message.put("mac",this.getCharger().getMac());
                message.put("chargerAddr",Long.parseLong(this.getCharger().getChargerAddr()));
                MessageSender.sendMapMessage(message, ISender.WCS_RCS_MIDEA_CHARGE);
                break;
            default:
                logger.error("没有这种充电桩类型:"+type);
        }
        //MessageSender.sendMapMessage(message, ISender.WCS_RCS_AGV_CHARGE);
        this.setMessage(JsonUtils.map2Json(message));
        this.setSend2RcsTime(DateUtils.formatDate(new Date(System.currentTimeMillis())));
    }

    @Override
    public void initOrder() {
        //获取终点
        String endAddr = this.charger.getAddressCodeId();
        this.setEndAddr(Long.parseLong(endAddr));

        Long srcAddr = Long.parseLong(getRobot().getAddressId());
        this.wcsPath.setSrcAddr(srcAddr);
        this.wcsPath.setEndAddr(Long.parseLong(endAddr));
        Integer sourceVertex = CommonUtils.long2Int(srcAddr);
        Integer targetVertex = CommonUtils.long2Int(getEndAddr());
        List<Long> path = this.getWebApiBusiness().getEmptyPath(this.getWareHouseId(),getSectionId(),sourceVertex,targetVertex);
        if(path==null || path.size()==0 || path.size() == 1){
            this.setOrderError(Order.ERROR_EMPTY_PATH);
        }
        this.wcsPath.setSeriesPath(path);
        //旋转角度
        this.wcsPath.setRotateTheta(this.getCharger().getDirect());
    }

    @Override
    public void process() {
        if(this.wcsPath.getSeriesPath() == null || this.wcsPath.getSeriesPath().size() ==0){
            logger.error("调度单初始化失败，不能发送消息:"+this);
            return;
        }
        this.addKV("TRIP_STATE", TripStatus.PROCESS);
        this.getJdbcRepository().updateBusinessObject(this);
    }

    @Override
    public void finish() {
        if(this.wcsPath.getSeriesPath() == null || this.wcsPath.getSeriesPath().size() ==0){
            logger.error("调度单初始化失败，不能发送消息:"+this);
            return;
        }
        this.addKV("TRIP_STATE", TripStatus.FINISHED);
        this.getJdbcRepository().updateBusinessObject(this);
    }

    @Override
    public boolean isFinish() {
        //return Objects.equals(Long.parseLong(this.getRobot().getAddressId()), this.getEndAddr());
        logger.debug("位移包不结束调度单，从充电时间电量判断去结束!"+this);
        if(Objects.equals(Long.parseLong(this.getRobot().getAddressId()), this.getEndAddr())
                && this.getRobot().getStatus() == Robot.CHARGING){
            this.setCanFinish(true);
        }
        return false;
    }

    @Override
    public String getType() {
        return "ChargerDrive";
    }

    @Override
    public void setType(String type) {

    }

    @Override
    public String toString() {
        return "ChargerDriveOrder{" +
                "msg=" + this.getMessage() +
                "sendTime=" + this.getSend2RcsTime() +
                "charger=" + charger +
                ", wcsPath=" + wcsPath +
                ", orderId=" + this.getOrderId() +
                ", orderError=" + OrderErrorMessage.getMsg(this.getOrderError()) +
                ", type=" + this.getType() +
                '}';
    }

    @Override
    public void reInitOrder() {
        this.initOrder();
    }

    @Override
    public Pod getPod() {
        return null;
    }
}
