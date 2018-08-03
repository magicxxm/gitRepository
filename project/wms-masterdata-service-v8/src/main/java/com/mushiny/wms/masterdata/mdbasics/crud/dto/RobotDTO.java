package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.Robot;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RobotDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String robot;

    @NotNull
    private String password;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String robotTypeId;

    private RobotTypeDTO robotType;

    private String hardware;

    private String software;

    private LocalDateTime priduction;

    private int acc = 0;

    private LocalDateTime recently;

    private int inbreak = 0;

    private int cold = 0;

    private int hot = 0;

    private int batteryNumber=1;

    public RobotDTO() {
    }

    public RobotDTO(Robot entity) {
        super(entity);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRobotTypeId() {
        return robotTypeId;
    }

    public void setRobotTypeId(String robotTypeId) {
        this.robotTypeId = robotTypeId;
    }

    public RobotTypeDTO getRobotType() {
        return robotType;
    }

    public void setRobotType(RobotTypeDTO robotType) {
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
