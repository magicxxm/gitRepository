package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "MD_ROBOT")
public class Robot extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "ROBOT_ID", nullable = false)
    private String robot;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ROBOT_TYPE_ID", nullable = false)
    private RobotType robotType;

    @Column(name = "HARDWARE_VERSION")
    private String hardware;

    @Column(name = "SOFTWARE_VERSION")
    private String software;

    @Column(name = "PRODUCTION_DATE")
    private LocalDateTime priduction;

    @Column(name = "ACC_DURATION")
    private int acc;

    @Column(name = "RECENTLY_MAINTAIN_TIME")
    private LocalDateTime recently;

    @Column(name = "INBREAK_DETECT_TIMES")
    private int inbreak;

    @Column(name = "COLD_RESET_TIMES")
    private int cold;

    @Column(name = "HOT_RESET_TIMES")
    private int hot;

    @Column(name = "BATTERY_NUMBER")
    private int batteryNumber;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RobotType getRobotType() {
        return robotType;
    }

    public void setRobotType(RobotType robotType) {
        this.robotType = robotType;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public LocalDateTime getPriduction() {
        return priduction;
    }

    public void setPriduction(LocalDateTime priduction) {
        this.priduction = priduction;
    }

    public int getAcc() {
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public LocalDateTime getRecently() {
        return recently;
    }

    public void setRecently(LocalDateTime recently) {
        this.recently = recently;
    }

    public int getInbreak() {
        return inbreak;
    }

    public void setInbreak(int inbreak) {
        this.inbreak = inbreak;
    }

    public int getCold() {
        return cold;
    }

    public void setCold(int cold) {
        this.cold = cold;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public String getRobot() {
        return robot;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }

    public int getBatteryNumber() {
        return batteryNumber;
    }

    public void setBatteryNumber(int batteryNumber) {
        this.batteryNumber = batteryNumber;
    }
}
