package com.mushiny.wms.tot.attendance.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TOT_ATTENDANCE")
public class Attendance extends BaseEntity {
    @Column(name = "EMPLOYEE_CODE", nullable = false)
    private  String employeeCode;
    @Column(name = "EMPLOYEE_NAME", nullable = false)
    private  String employeeName;
    @Column(name = "CLOCK_TYPE", nullable = false)
    private  String clockType;
    @Column(name = "CLOCK_TIME")
    private  String clockTime;
    @Column(name = "CLOCK_METHOD", nullable = false)
    private  String clockMethod;
    @Column(name = "CLIENT_ID", nullable = false)
    private  String clientId;
    @Column(name = "WAREHOUSE_ID",nullable = false)
    private String warehouseId;



    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public String getClockMethod() {
        return clockMethod;
    }

    public void setClockMethod(String clockMethod) {
        this.clockMethod = clockMethod;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
}
