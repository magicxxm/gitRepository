package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_TURNAREAQUEUE")
public class TurnAreaQueue extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "TURNAREAPOSITION_ID")
    private String turnAreaPosition;

    @Column(name = "ROBOT_ID")
    private String robot;

    public String getTurnAreaPosition() {
        return turnAreaPosition;
    }

    public void setTurnAreaPosition(String turnAreaPosition) {
        this.turnAreaPosition = turnAreaPosition;
    }

    public String getRobot() {
        return robot;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }
}
