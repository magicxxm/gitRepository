package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="WCS_ROBOT")
public class Robot extends BaseClientAssignedEntity {

    @Column(name="ROBOT_ID")
    private String robotId;

    @Column(name="VOLTAGE")
    private int voltage;

    @Column(name="LAVEBATTERY")
    private int laveBattery;

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public void setLaveBattery(int laveBattery) {
        this.laveBattery = laveBattery;
    }

    public String getRobotId() {
        return robotId;
    }

    public int getVoltage() {
        return voltage;
    }

    public int getLaveBattery() {
        return laveBattery;
    }
}
