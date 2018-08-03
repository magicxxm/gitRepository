package com.mushiny.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * Created by Tank.li on 2017/7/22.
 */
public class RobotStatusChangeListener implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();
        Robot robot = (Robot) evt.getSource();

        if("status".equals(propertyName)){
            Integer oldStatus = (Integer) oldValue;
            Integer newStatus = (Integer) newValue;
            if(!Objects.equals(oldStatus, newStatus)){
                robot.setPersistFlag(true);//确实发生变化 需要持久到数据库
            }
        }
        //更新地址
        if("addressId".equals(propertyName)){
            String oldAddress = (String) oldValue;
            String newAddress = (String) newValue;
            if(!Objects.equals(oldAddress,newAddress)){
                robot.setPersistFlag(true);//确实发生变化 需要持久到数据库
            }
        }
        //更新pod pod属性的变化指 朝向、id （换了个POD 或者转了个面）
        if("pod".equals(propertyName)){
            Pod oldPod = (Pod) oldValue;
            Pod newPod = (Pod) newValue;
            if(!Objects.equals(oldPod,newPod)){
                robot.setPersistFlag(true);//确实发生变化 需要持久到数据库
            }
        }
    }

    /*因为报文和状态都是一坨，所以拆不开 统一主动commit*/
    private final static Logger logger = LoggerFactory.getLogger(RobotStatusChangeListener.class);
    /**
     * 更新机器人状态
     * @param robot
     * @param oldStatus
     * @param newStatus
     */
    public void updateStatus(Robot robot, Integer oldStatus, Integer newStatus) {
        robot.addKV("oldStatus",oldStatus);
        robot.addKV("status",newStatus);
        logger.debug("小车Robot_id"+robot.getRobotId()+"状态更新结束,status:"+oldStatus+"==>"+newStatus);
    }

    /**
     * 更新小车地址 保险起见 把xy坐标更新了
     * @param robot
     * @param oldAddress
     * @param newAddress
     */
    public void updateAddress(Robot robot, String oldAddress, String newAddress) {
        robot.addKV("OLDADDRCODEID",oldAddress);
        robot.addKV("ADDRESSCODEID",newAddress);
        logger.debug("小车Robot_id"+robot.getRobotId()+"位置更新结束,address:"+oldAddress+"==>"+newAddress);
    }

    /**
     * 更新POD
     * @param robot
     * @param oldPod
     * @param newPod
     */
    public void updatePod(Robot robot, Pod oldPod, Pod newPod) {
        String oldPodId = oldPod==null? null:oldPod.getPodId();
        String newPodId = newPod==null? null:newPod.getPodId();
        robot.addKV("pod_id",newPodId);
        logger.debug("小车Robot_id"+robot.getRobotId()+"状态更新结束,pod_id:"+oldPodId+"==>"+newPodId);
    }
}
