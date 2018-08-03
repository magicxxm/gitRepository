package com.mushiny.wms.internaltool.web.dto;

import java.io.Serializable;

public class RobotDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String robotId;

    private int voltage ;

    private int laveBattery;

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
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
