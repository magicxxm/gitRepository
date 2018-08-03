package com.mushiny.wms.tot.report.query.dto;

import java.io.Serializable;

/**
 * Created by Laptop-8 on 2017/7/2.
 * 暂无用
 */
public class ClockTimeDTO implements Serializable{
    private String clockTime;
    private String employeeCode;
    private String clockType;

    public String getClockTime() {
        return clockTime;
    }

    public void setClockTime(String clockTime) {
        this.clockTime = clockTime;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getClockType() {
        return clockType;
    }

    public void setClockType(String clockType) {
        this.clockType = clockType;
    }
}
