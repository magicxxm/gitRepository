package com.mushiny.beans.order;

import com.mushiny.beans.Section;
import com.mushiny.beans.enums.TripStatus;
import com.mushiny.business.WebApiBusiness;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import com.mushiny.mq.ISender;
import com.mushiny.mq.MessageSender;
import org.apache.http.client.utils.DateUtils;

import java.util.*;

/**
 * pod巡检的调度单
 * Created by Tank.li on 2017/9/19.
 */
public class PodScanOrder extends Order {

    private String type = "PodScan";
    //小车奔跑路径
    private List<Long> path;

    private Long startScan;//
    private Long endScan;//

    public Long getStartScan() {
        return startScan;
    }

    public void setStartScan(Long startScan) {
        this.startScan = startScan;
    }

    public Long getEndScan() {
        return endScan;
    }

    public void setEndScan(Long endScan) {
        this.endScan = endScan;
    }

    public List<Long> getPath() {
        return path;
    }

    public void setPath(List<Long> path) {
        this.path = path;
    }

    @Override
    public void sendMessage2Rcs() {
        Map msg = new HashMap();
        msg.put("robotID",this.getRobot().getRobotId());
        Section section = this.getWareHouseManager().getSectionById(this.getRobot().getSectionId());
        msg.put("sectionID",section.getRcs_sectionId());
        msg.put("time", System.currentTimeMillis());
        msg.put("path",this.wcsPath.getSeriesPath());
        msg.put("startScan",this.getStartScan());
        msg.put("endScan",this.getEndScan());

        MessageSender.sendMapMessage(msg, ISender.WCS_RCS_PODSCAN_PATH);

        this.setMessage(JsonUtils.map2Json(msg));
        this.setSend2RcsTime(DateUtils.formatDate(new Date(System.currentTimeMillis())));
    }

    @Override
    public void initOrder() {
        logger.debug("初始化扫描POD的路径:" + this.getType() + ":" + this.getPath());
        this.wcsPath.setEndAddr(this.getEndAddr());
        Long srcAddr = Long.parseLong(getRobot().getAddressId());
        this.wcsPath.setSrcAddr(srcAddr);
        //第一阶段任务 空车
        Integer sourceVertex = CommonUtils.long2Int(srcAddr);
        //第一个节点是路径的第一个节点
        if(this.getPath() != null && this.getPath().size() == 0){
            this.setOrderError(Order.ERROR_EMPTY_PATH);
            return;
        }
        this.setStartScan(this.getPath().get(0));
        this.setEndScan(this.getPath().get(this.getPath().size()-1));

        Integer targetVertex = Integer.parseInt(this.getPath().get(0)+"");
        //从重车走路径，不钻货架
        List<Long> path = this.getWebApiBusiness().getHeavyPath(this.getWareHouseId(),getSectionId(),sourceVertex,targetVertex);
        if(path==null || path.size()==0){
            this.setOrderError(Order.ERROR_HEAVY_PATH);
            return;
        }

        this.add2Path(path,this.getPath());
        wcsPath.setPodUpAddress(0L);
        wcsPath.setPodDownAddress(0L);
        this.wcsPath.setSeriesPath(path);
        WebApiBusiness.logPath(path,"全");
    }

    @Override
    public void process() {
        this.addKV("TRIP_STATE", TripStatus.PROCESS);
        this.getJdbcRepository().updateBusinessObject(this);
    }

    @Override
    public void finish() {
        this.addKV("TRIP_STATE", TripStatus.FINISHED);
        this.getJdbcRepository().updateBusinessObject(this);
    }

    @Override
    public String toString() {
        return "PodScanOrder{" +
                "type='" + type + '\'' +
                ", msg =" + this.getMessage() +
                ", sendTime =" + this.getMessage() +
                ", startScan=" + startScan +
                ", endScan=" + endScan +
                ", path=" + path +
                ", RCSTime=" + this.getSend2RcsTime() +
                ", robot=" + (this.getRobot()==null?"":this.getRobot().getRobotId()) +
                '}';
    }

    @Override
    public void reInitOrder() {
        this.initOrder();
    }

    @Override
    public boolean isFinish() {
       return  Objects.equals(this.getRobot().getAddressId(),this.getEndScan()+"");
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
