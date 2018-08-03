package com.mushiny.beans;

import com.mushiny.beans.enums.AddressStatus;
import com.mushiny.business.WareHouseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Created by Tank.li on 2017/6/26.
 */
public class Address extends BaseObject {
    //当前位置的货架 pod 和 车 robot
    private Pod pod;

    private Robot robot;

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    private long lockedBy;//被哪个小车锁定,在返回存储区时锁定

    public long getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(long lockedBy) {
        this.lockedBy = lockedBy;
    }

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String pkId;

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    private String classValue;
    private String classDir;

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    public String getClassDir() {
        return classDir;
    }

    public void setClassDir(String classDir) {
        this.classDir = classDir;
    }

    private AddressGroup addressGroup;//所属组 可以是空的

    public AddressGroup getAddressGroup() {
        return addressGroup;
    }

    public void setAddressGroup(AddressGroup addressGroup) {
        this.addressGroup = addressGroup;
    }

    private Address groupOutterAddr;//指向同组外部
    private Address groupInnerAddr;//指向同组内部

    public Address getGroupOutterAddr() {
        return groupOutterAddr;
    }

    public void setGroupOutterAddr(Address groupOutterAddr) {
        this.groupOutterAddr = groupOutterAddr;
    }

    public Address getGroupInnerAddr() {
        return groupInnerAddr;
    }

    public void setGroupInnerAddr(Address groupInnerAddr) {
        this.groupInnerAddr = groupInnerAddr;
    }

    private String id;//ADDRESSCODEID

    private String sectionId;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    private int xPosition;//地标位置
    private int yPosition;//地标位置


    public static final String TABLE = "WD_NODE";
    @Override
    public String getTable() {
        return TABLE;
    }

    public static final String IDNAME = "ADDRESSCODEID";

    @Override
    public String getIdName() {
        return IDNAME;
    }

    public Address(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    public Address() {
    }

    //相对得分值
    private double score;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    //距离热度 zc 跟最近工位的常量距离，仓库生成后一般不会变动
    private int hot;

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    private String nodeState;//格子状态 Available/Reserved/Occupied/Unavailable

    private String wareHouseId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public String getNodeState() {
        return nodeState;
    }

    private final static Logger logger = LoggerFactory.getLogger(Address.class);

    public void setNodeState(String nodeState) {
        logger.debug("设置地址格Address["+this.getId()+"]状态是:"+nodeState);
        if(!Objects.equals(this.nodeState, nodeState)){
            this.addKV("NODE_STATE", nodeState);
        }
        this.nodeState = nodeState;
    }

    public String getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();;
        if (this.getAddressGroup() != null) {
            AddressGroup addressGroup = this.getAddressGroup();
            List<Address> addresses = addressGroup.getGroupAddrs();

            sb.append("[");
            for (int i = 0; i < addresses.size(); i++) {
                Address address = addresses.get(i);
                if (i < addresses.size() - 1) {
                    sb.append(address.getId()).append(",");
                } else {
                    sb.append(address.getId());
                }
            }
            sb.append("]");
        }
        return "Address{" +
                "id='" + id + '\'' +
                ", addressGroup=" + sb +
                ", AddressType=" + this.getType() +
                ", lockedBy=" + lockedBy +
                ", pod=" + (pod == null ? "" : pod.getPodName()) +
                ", robot=" + (robot == null ? "" : robot.getRobotId()) +
                ", nodeState='" + nodeState + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return false;
        }
        Address addr = (Address) obj;
        return Objects.equals(this.getId(),addr.getId());
    }

    /**
     * 锁定解锁格子只能同步试试
     * @param robotId     当前车
     * @param lock_unlock 锁定或解锁
     * @return
     */
    public synchronized boolean robotLock(long robotId, boolean lock_unlock) {
        if (lock_unlock) {
            if ((this.getLockedBy()==robotId || this.getLockedBy()==0)
                    && !this.getNodeState().equals(AddressStatus.OCCUPIED)){
                this.setLockedBy(robotId);
                this.setNodeState(AddressStatus.RESERVED);
                logger.info("小车"+robotId+"锁定目标:"+this.getId()+"成功!");
                return true;
            }
            logger.info("小车"+robotId+"锁定目标:"+this.getId()+"失败!");
            return false;
        } else {
            if ((this.getLockedBy()==robotId) && this.getNodeState().equals(AddressStatus.RESERVED)){
                this.setLockedBy(0L);
                this.setNodeState(AddressStatus.AVALIABLE);
                logger.info("小车"+robotId+"解锁目标:"+this.getId()+"成功!");
                return true;
            }
            logger.info("小车"+robotId+"解锁目标:"+this.getId()+"失败!");
            return false;
        }
    }
}
