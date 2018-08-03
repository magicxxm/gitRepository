package com.mushiny.beans;

import com.mushiny.beans.enums.AddressStatus;
import com.mushiny.beans.enums.AddressType;
import com.mushiny.beans.order.Order;
import com.mushiny.beans.order.StationPodOrder;
import com.mushiny.business.RobotManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 计算热度使用的Pod对象 跟数据库存储没关系
 * Created by Tank.li on 2017/6/25.
 */
public class Pod extends BaseObject{
    private final static Logger logger = LoggerFactory.getLogger(Pod.class);

    private String podId;
    private String podName;

    private Long rcsPodId;//RCS扫码出来的POD

    private long lockedBy;//被哪个小车锁定,在初始化时锁定

    public long getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(long lockedBy) {
        this.lockedBy = lockedBy;
    }

    //pod绑定的Robot 在收到实时包时更新
    private Robot robot;
    /*POD朝向 角度*/
    private int direct;

    //private String addressId;
    private String sectionId;
    private String wareHouseId;
    private String clientId;
    //最终可能要去的位置排序 如果被占用就取下一个
    private List<Address> favorAddrs;
    private double hot = 0.0;
    //排序后换成Z值的HOT,跟位置一起排序算得
    private int zHot = 0;
    //pod当前位置
    private Address address;

    //pod当前移动的目标位置，一旦调度单生成目的地后，会更新POD的目标位置==调度单的目标位置
    private String movTargetAddrId;

    private Set<PodPosition> podPositions;
    private Address ideaTarget;
    private int isNewCreated;

    public void setIsNewCreated(int isNewCreated) {
        this.isNewCreated = isNewCreated;
    }

    public int getIsNewCreated() {
        return isNewCreated;
    }

    public Long getRcsPodId() {
        return rcsPodId;
    }

    public void setRcsPodId(Long rcsPodId) {
        this.rcsPodId = rcsPodId;
    }

    public String getMovTargetAddrId() {
        return movTargetAddrId;
    }

    public void setMovTargetAddrId(String movTargetAddrId) {
        this.movTargetAddrId = movTargetAddrId;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public List<Address> getFavorAddrs() {
        return favorAddrs;
    }

    public void setFavorAddrs(List<Address> favorAddrs) {
        this.favorAddrs = favorAddrs;
    }

    public Address getIdeaTarget() {
        return ideaTarget;
    }

    public void setIdeaTarget(Address ideaTarget) {
        this.ideaTarget = ideaTarget;
    }

    public int getzHot() {
        return zHot;
    }

    public void setzHot(int zHot) {
        this.zHot = zHot;
    }

    public double getHot() {
        return hot;
    }

    public void setHot(double hot) {
        this.hot = hot;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        logger.debug("设置POD位置:"+address);
        this.address = address;

    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    /*public String getId() {
        return addressId;
    }

    public void setId(String addressId) {
        this.addressId = addressId;
    }*/

    public String getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Set<PodPosition> getPodPositions() {
        return podPositions;
    }

    public void setPodPositions(Set<PodPosition> podPositions) {
        this.podPositions = podPositions;
    }

    @Override
    public Object getId() {
        return this.podId;
    }

    public static final String TABLE = "MD_POD";
    @Override
    public String getTable() {
        return TABLE;
    }

    public static final String IDNAME = "ID";

    @Override
    public String getIdName() {
        return IDNAME;
    }

    @Override
    public String toString() {
        return "Pod{" +
                "podName='" + podName + '\'' +
                ", rcsPodId=" + rcsPodId +
                ", lockedBy=" + lockedBy +
                ", robot=" + (robot==null? "":robot.getRobotId()) +
                ", direct=" + direct +
                ", isNewCreated=" + isNewCreated +
                //", sectionId='" + sectionId + '\'' +
                ", address=" + (address==null? "":address.getId()+":"+address.getNodeState()) +
                //", \n 热度: favorAddrs=" + favorAddrs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pod pod = (Pod) o;

        if (direct != pod.direct) return false;
        return podId.equals(pod.podId);
    }

    @Override
    public int hashCode() {
        int result = podId.hashCode();
        result = 31 * result + direct;
        return result;
    }

    /**
     * 同步锁定POD
     * @param order
     * @return
     */
    public synchronized boolean lockPod(Order order) {
        Long robotId = order.getRobot().getRobotId();
        //已经被其他车锁定
        if(lockedBy!= 0 && lockedBy != robotId){
            return false;
        }
        //锁定该POD
        this.lockedBy = robotId;
        return true;
    }
}
