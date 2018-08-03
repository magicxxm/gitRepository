package com.mushiny.beans.order;

import com.mushiny.beans.Address;
import com.mushiny.beans.Pod;
import com.mushiny.beans.Robot;
import com.mushiny.beans.Section;
import com.mushiny.beans.enums.AddressStatus;
import com.mushiny.beans.enums.TripStatus;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import com.mushiny.mq.ISender;
import com.mushiny.mq.MessageSender;
import org.apache.http.client.utils.DateUtils;

import java.util.*;

/**
 * Created by Tank.li on 2017/9/5.
 */
public class EmptyRunOrder extends Order {



    private Long endAddr;

    public Long getEndAddr() {
        return endAddr;
    }

    public void setEndAddr(Long endAddr) {
        this.endAddr = endAddr;
    }

    /*
    Long	robotID
    Long	sectionID
    Long	time
    Long	podUpAddress
    Long	podDownAddress
    Boolean	isRotatePod
    Int	rotateTheta
    List<Long>	seriesPath*/
    @Override
    public void sendMessage2Rcs() {
        Map msg = genWcsMessage();
        MessageSender.sendMapMessage(msg, ISender.WCS_RCS_AGV_SERIESPATH);
        this.setMessage(JsonUtils.map2Json(msg));
        this.setSend2RcsTime(DateUtils.formatDate(new Date(System.currentTimeMillis())));
    }

    @Override
    public void initOrder() {
        if (this.endAddr == 0L){
            lockRandomStorage();//锁定最近存储区
        }
        this.wcsPath.setEndAddr(this.endAddr);
        Long srcAddr = Long.parseLong(getRobot().getAddressId());
        this.wcsPath.setSrcAddr(srcAddr);
        Integer sourceVertex = CommonUtils.long2Int(srcAddr);
        Integer targetVertex = CommonUtils.long2Int(endAddr);
        List<Long> path = this.getWebApiBusiness().getEmptyPath(this.getWareHouseId(),getSectionId(),sourceVertex,targetVertex);
        this.wcsPath.setSeriesPath(path);
    }

    /**
     * 所有存储位的随机
     */
    private void lockRandomStorage() {
        Section section = this.getWareHouseManager().getSectionById(this.getSectionId());
        List<Address> storageAddrs = section.getStorageAddrs();
        Random random = new Random();

        //获取小车小车位置映射表
        Map robots = this.getRobotManager().getRegistRobots();
        Iterator<Robot> iterator = robots.values().iterator();
        Map<String,Robot> idleRobots = new HashMap();
        while (iterator.hasNext()) {
            Robot next = iterator.next();
            if(next.isAvaliable() && next.getStatus() == Robot.IDLE) {
                //小车的地址
                idleRobots.put(next.getAddressId(), next);
            }
        }

        List<Integer> list = new ArrayList();
        for (int i = 0; i < storageAddrs.size(); i++) {
            Integer ranInt = random.nextInt(storageAddrs.size());
            if(!list.contains(ranInt)){
                list.add(ranInt);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            Integer integer = list.get(i);
            //从第一个节点判断
            if(integer < storageAddrs.size()){
               Address address = storageAddrs.get(integer);
               //只要车不在存储位、其他车目标地址不在存储位
                //并且该存储位上有货架（很重要，不能去空闲位置 容易挡道）
                // 并且该存储位的货架没有分配调度任务(New 或 Available)
                //可能存在延迟 避免不了冲突
               if(idleRobots.get(address.getId()) == null && address.getLockedBy() == 0L
                       && Objects.equals(address.getNodeState(), AddressStatus.OCCUPIED)
                       && noTripOnAddr(address)){
                    address.robotLock(this.getRobot().getRobotId(),true);
                    this.setEndAddr(Long.parseLong(address.getId()));
               }
            }
        }
    }

    private static final String TRIP_ON_ADDRESS = "SELECT 1 FROM RCS_TRIP,MD_POD " +
            "WHERE RCS_TRIP.POD_ID=MD_POD.ID " +
            "AND RCS_TRIP.TRIP_STATE IN (?,?,?) AND POD_ID=?";
    private boolean noTripOnAddr(Address address) {
        Pod pod = this.getPodManager().getPodByAddress(address.getId(),address.getSectionId());
        if (pod == null){
            return false;
        }
        List rows = this.getJdbcRepository().queryBySql(TRIP_ON_ADDRESS,
                TripStatus.AVAILABLE,TripStatus.NEW,TripStatus.PROCESS,pod.getPodId());
        return rows==null || rows.size() == 0;
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
        Address endAddr = this.getWareHouseManager().getAddressByAddressCodeId(this.endAddr,this.getSectionId());
        if (endAddr != null) {
            endAddr.setLockedBy(0L); //TODO
        }
    }

    @Override
    public boolean isFinish() {
        Long end = this.getWcsPath().getEndAddr();
        return Objects.equals(Long.parseLong(this.getRobot().getAddressId()), endAddr)
                || Objects.equals(Long.parseLong(this.getRobot().getAddressId()), end);
    }

    @Override
    public String getType() {
        return "EmptyRun";
    }

    @Override
    public void setType(String type) {

    }

    @Override
    public void reInitOrder() {
        this.initOrder();//一样处理
    }

    @Override
    public String toString() {
        return "EmptyRun{" +
                "msg=" + this.getMessage() +
                "sendTime=" + this.getSend2RcsTime() +
                //", wcsPath=" + wcsPath +
                ", orderId=" + this.getOrderId() +
                ", type=" + this.getType() +
                '}';
    }
}
