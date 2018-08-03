package com.mushiny.wms.tot.report.query.dto;

import java.io.Serializable;

/**
 * 打卡时间和类型
 */
public class Clock implements Serializable {
    private static final long serialVersionUID = 1L;

    private String clockType;
    private String clockTime;

    public Clock() {
    }

    public Clock(String clockType, String clockTime) {
        this.clockType = clockType;
        this.clockTime = clockTime;
    }

    public String getClockType() {
        return clockType;
    }

    public void setClockType(String clockType) {
        this.clockType = clockType;
    }

    public String getClockTime() {
        return clockTime;
    }

    public void setClockTime(String clockTime) {
        this.clockTime = clockTime;
    }
}
