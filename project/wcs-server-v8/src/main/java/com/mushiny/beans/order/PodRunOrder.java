package com.mushiny.beans.order;

import com.mushiny.beans.Address;
import com.mushiny.beans.enums.AddressStatus;
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
public class PodRunOrder extends Order {

    private Long endAddr;

    public Long getEndAddr() {
        return endAddr;
    }

    public void setEndAddr(Long endAddr) {
        this.endAddr = endAddr;
    }


    @Override
    public void sendMessage2Rcs() {
        Map message = this.genWcsMessage();
        MessageSender.sendMapMessage(message, ISender.WCS_RCS_AGV_SERIESPATH);
        this.setMessage(JsonUtils.map2Json(message));
        this.setSend2RcsTime(DateUtils.formatDate(new Date(System.currentTimeMillis())));
    }

    @Override
    public void initOrder() {
        if (this.getPod().getLockedBy() != 0L
                && this.getPod().getLockedBy() != this.getRobot().getRobotId()) {//未被释放
            this.setOrderError(Order.ERROR_POD);
            return;
        }

        boolean locked = this.getPod().lockPod(this);
        if (!locked) {
            logger.error("调度单锁定POD失败! RobotId:" + this.getRobot().getRobotId()
                    + "未锁定pod:" + this.getPod().getPodName() + " 已 locked by: " + this.getPod().getLockedBy());
            this.setOrderError(Order.ERROR_POD);
            return;
        }

        this.wcsPath.setEndAddr(this.endAddr);
        Long srcAddr = Long.parseLong(getRobot().getAddressId());
        this.wcsPath.setSrcAddr(srcAddr);
        //第一阶段任务 空车
        Integer sourceVertex = CommonUtils.long2Int(srcAddr);
        Integer targetVertex = Integer.parseInt(this.getPod().getAddress().getId());

        List<Long> path = this.getWebApiBusiness().getEmptyPath(this.getWareHouseId(), getSectionId(), sourceVertex, targetVertex);
        if (path == null || path.size() == 0) {
            this.setOrderError(Order.ERROR_EMPTY_PATH);
            return;
        }
        //第二阶段任务 重车
        if (!this.lockEndAddr()) {
            logger.debug("PodRun没有定义目标地址或无法锁定该目标地址,通过热度算法生成");
            String addr = this.getPodManager().computeTargetAddress(
                    this.getPod().getRcsPodId() + "", this.getPod().getSectionId());
            if(CommonUtils.isEmpty(addr) || addr.equals("")){
                logger.error(this.getOrderId()+"的POD="+this.getPod().getPodName()+":热度算法没有生成目标地址");
                this.setOrderError(Order.ERROR_NO_HOTADDRESS);
                return;
            }
            endAddr = Long.parseLong(addr);
        }
        logger.debug("PodRun的目标是:" + endAddr);
        Address destAddr= this.getWareHouseManager().getAddressByAddressCodeId(endAddr, this.getSectionId());
        //destAddr.setNodeState(AddressStatus.RESERVED);//不参与计算了
        boolean lockAddr = destAddr.robotLock(this.getRobot().getRobotId(),true);
        if (!lockAddr) {
            logger.info("目标格子："+endAddr+"锁定失败！destAddr.getLockedBy():"+destAddr.getLockedBy());
            this.setOrderError(Order.ERROR_LOCK_ENDADDR);
            return;
        }
        //恢复原先锁定的节点 设置新的目标地址
        Long preLocked = this.getRobot().getLockedAddr();
        //如果运算出来的节点不一样 需要恢复原先节点
        if(preLocked != 0L && !Objects.equals(preLocked,endAddr)){
            logger.info("小车"+this.getRobot().getRobotId()+"的原目标地址:"
                    + preLocked + " 新目标地址:"+endAddr+"不相同!");
            Address preLockedAddr= this.getWareHouseManager()
                    .getAddressByAddressCodeId(preLocked, this.getSectionId());
            /*if(preLockedAddr.getLockedBy() == this.getRobot().getRobotId()){
                preLockedAddr.setLockedBy(0);
                preLockedAddr.setNodeState(AddressStatus.AVALIABLE);
            }*/
            if(preLockedAddr!=null){
                preLockedAddr.robotLock(this.getRobot().getRobotId(),false);
            }
        }
        this.getRobot().setLockedAddr(endAddr);
        //结束设置小车新的目标地址
        this.wcsPath.setEndAddr(this.endAddr);
        //trip表设置回存储区的终点
        logger.debug("PodRunOrder-init更新:tripID="+this.getOrderId()+" , endAddress="+endAddr);
        boolean re = updateEndAddr2Database(this.getOrderId(), endAddr+"");
        if (re){
            logger.debug("PodRunOrder-init更新成功！tripID="+this.getOrderId()+" , endAddress="+endAddr);
        }else{
            logger.debug("PodRunOrder-init更新失败！tripID="+this.getOrderId()+" , endAddress="+endAddr);
        }
        //执行数据库锁定
        //this.getJdbcRepository().updateBusinessObject(dest);
        logger.debug("目标格子:" + endAddr + "已经被锁定!");
        Integer dest = CommonUtils.long2Int(endAddr);
        List<Long> path2 = this.getWebApiBusiness().getHeavyPath(this.getWareHouseId(), getSectionId(), targetVertex, dest);
        if (path2 == null || path2.size() == 0) {
            this.setOrderError(Order.ERROR_HEAVY_PATH);
            unlockEndAddress();
            return;
        }

        this.add2Path(path, path2);
        //this.setEndAddr();
        wcsPath.setPodUpAddress(Long.parseLong(this.getPod().getAddress().getId()));
        wcsPath.setPodDownAddress(endAddr);
        this.wcsPath.setSeriesPath(path);
    }


    @Override
    public void process() {
        this.addKV("TRIP_STATE", TripStatus.PROCESS);
        this.getJdbcRepository().updateBusinessObject(this);
        //更新POD和目标地址信息
        updatePodTarget();
        //如果POD属于深层货架里头，将同组外面的节点Reserved掉
        updatePodOutterAddr();
    }

    @Override
    public void finish() {
        this.addKV("TRIP_STATE", TripStatus.FINISHED);
        this.getJdbcRepository().updateBusinessObject(this);
        if (this.getPod() != null) {
            this.getPodManager().finishMainOrder(this);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        Long addr = this.getEndAddr();
        if(addr == 0L){
            this.setOrderError(Order.ERROR_NO_HOTADDRESS);
            return;
        }
        Address address = this.getWareHouseManager().getAddressByAddressCodeId(addr, this.getSectionId());
        logger.debug("返回存储区调度单出错:Order:" + this.getOrderId() + " 目标节点恢复:" + addr);
        if(address != null){
            address.robotLock(this.getRobot().getRobotId(),false);
        }
        this.getRobot().setAvaliable(true);
    }

    @Override
    public boolean isFinish() {
        Long orderEnd = this.getWcsPath().getEndAddr();
        logger.debug(this.getOrderId()+"this.getRobot().getAddressId():"+this.getRobot().getAddressId());
        logger.debug(this.getOrderId()+"orderEnd:" + orderEnd);
        logger.debug(this.getOrderId()+"endAddr:" + endAddr);
        if ((Objects.equals(Long.parseLong(this.getRobot().getAddressId()), endAddr)
                || Objects.equals(orderEnd,Long.parseLong(this.getRobot().getAddressId())))
                && Objects.equals(this.getRobot().getPod(), this.getPod())) {//必须托起POD
            this.setCanFinish(true);
            return true;
        }
        return false;
    }

    @Override
    public String getType() {
        return "PodRun";
    }

    @Override
    public void setType(String type) {
        logger.error("不允许设置类型");
    }

    @Override
    public void reInitOrder() {
        this.initOrder();//同一样处理
    }

    @Override
    public String toString() {
        return "PodRunOrder{" +
                "msg=" + this.getMessage() +
                "sendTime=" + this.getSend2RcsTime() +
                "pod=" + this.getPod() +
                ", getWcsPath=" + this.getWcsPath() +
                ", orderId=" + this.getOrderId() +
                ", type=" + this.getType() +
                '}';
    }
}
