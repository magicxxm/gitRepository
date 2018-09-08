package com.mushiny.wms.application.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "WCS_ROBOT")
public class WCSRobot extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "ROBOT_ID")
    private String robotId;

    @Column(name = "POD_ID")
    private String podId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "XPOSITION")
    private Integer xPosition;

    @Column(name = "YPOSITION")
    private Integer yPosition;

    @Column(name = "TYPEID")
    private String typeId;

    @Column(name = "IP")
    private String ip;

    @Column(name = "LAVEBATTERY")
    private Integer laveBattery;
    @Column(name = "VOLTAGE")
    private Integer voltage;

    public Integer getVoltage() {
        return voltage;
    }

    public void setVoltage(Integer voltage) {
        this.voltage = voltage;
    }

    @Column(name = "MOTORTEMPERATURE")
    private Integer motorTemperature;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "OLDSTATUS")
    private Integer oldStatus;

    @Column(name = "ADDRESSCODEID")
    private Integer addressCodeId;

    @Column(name = "TARGETADDRCODEID")
    private Integer targetAddrCodeId;

    @Column(name = "OLDADDRCODEID")
    private Integer oldAddrCodeId;

    @Column(name = "ERRORID")
    private Integer errorId;

    @Column(name = "ERRORSTATUS")
    private Integer errorStatus;

    @Column(name = "ZONE_ID")
    private String zoneId;

    @Column(name = "SECTION_ID")
    private String sectionId;

    @Column(name = "AGVTYPE")
    private String agvType;

    @Column(name = "AVAILABLE")
    private boolean available;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public Integer getTargetAddrCodeId() {
        return targetAddrCodeId;
    }

    public void setTargetAddrCodeId(Integer targetAddrCodeId) {
        this.targetAddrCodeId = targetAddrCodeId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getxPosition() {
        return xPosition;
    }

    public void setxPosition(Integer xPosition) {
        this.xPosition = xPosition;
    }

    public Integer getyPosition() {
        return yPosition;
    }

    public void setyPosition(Integer yPosition) {
        this.yPosition = yPosition;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getLaveBattery() {
        return laveBattery;
    }

    public void setLaveBattery(Integer laveBattery) {
        this.laveBattery = laveBattery;
    }

    public Integer getMotorTemperature() {
        return motorTemperature;
    }

    public void setMotorTemperature(Integer motorTemperature) {
        this.motorTemperature = motorTemperature;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Integer oldStatus) {
        this.oldStatus = oldStatus;
    }

    public Integer getAddressCodeId() {
        return addressCodeId;
    }

    public void setAddressCodeId(Integer addressCodeId) {
        this.addressCodeId = addressCodeId;
    }

    public Integer getOldAddrCodeId() {
        return oldAddrCodeId;
    }

    public void setOldAddrCodeId(Integer oldAddrCodeId) {
        this.oldAddrCodeId = oldAddrCodeId;
    }

    public Integer getErrorId() {
        return errorId;
    }

    public void setErrorId(Integer errorId) {
        this.errorId = errorId;
    }

    public Integer getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(Integer errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getAgvType() {
        return agvType;
    }

    public void setAgvType(String agvType) {
        this.agvType = agvType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
