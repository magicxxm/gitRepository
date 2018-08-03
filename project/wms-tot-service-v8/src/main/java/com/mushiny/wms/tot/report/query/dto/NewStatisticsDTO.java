package com.mushiny.wms.tot.report.query.dto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tianyaoxie on 2017/6/27.
 * 前台员工打卡页面下方表格对应DTO
 */
public class NewStatisticsDTO implements Serializable{
    private String warehouseId;
    private String clientId; //客户经理的客户
    private String employeeCode;
    private String employeeName;
    private String ecWorkTime;     //有效工作时间
    private String totalClockTime; //总打卡时间
    private String ecWorkRate;     //有效工作率
    private List<Clock> zonesList = new LinkedList<>();//打卡类型和时间

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

    public String getEcWorkTime() {
        return ecWorkTime;
    }

    public void setEcWorkTime(String ecWorkTime) {
        this.ecWorkTime = ecWorkTime;
    }

    public String getTotalClockTime() {
        return totalClockTime;
    }

    public void setTotalClockTime(String totalClockTime) {
        this.totalClockTime = totalClockTime;
    }

    public String getEcWorkRate() {
        return ecWorkRate;
    }

    public void setEcWorkRate(String ecWorkRate) {
        this.ecWorkRate = ecWorkRate;
    }

    public List<Clock> getZonesList() {
        return zonesList;
    }

    public void setZonesList(List<Clock> zonesList) {
        this.zonesList = zonesList;
    }
    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
