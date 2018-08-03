package com.mushiny.wms.tot.attendance.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.tot.attendance.domain.Attendance;

public class AttendanceDTO extends BaseDTO {
    private String employeeCode;
    private String employeeName;
    private String clockType;
    private String clockTime;
    private String clockMethod;
    private String clientId;
    private String warehouseId;
    private String message;

    public AttendanceDTO() {
    }

    public AttendanceDTO(Attendance entity) {
        super(entity);
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
