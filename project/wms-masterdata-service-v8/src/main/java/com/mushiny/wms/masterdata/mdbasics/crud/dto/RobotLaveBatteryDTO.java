package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import java.io.Serializable;

public class RobotLaveBatteryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int robotId;

    private int voltage ;

    private int laveBattery;

    public int getRobotId() {
        return robotId;
    }

    public void setRobotId(int robotId) {
        this.robotId = robotId;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public int getLaveBattery() {
        return laveBattery;
    }

    public void setLaveBattery(int laveBattery) {
        this.laveBattery = laveBattery;
    }
}
