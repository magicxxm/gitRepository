package com.mushiny.wms.application.business.dto;

import com.mushiny.wms.application.domain.Charger;
import com.mushiny.wms.application.domain.WCSRobot;

import java.io.Serializable;
import java.math.BigDecimal;

public class RobotChargerScore implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal score;

    private WCSRobot robot;

    private Charger charger;

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public WCSRobot getRobot() {
        return robot;
    }

    public void setRobot(WCSRobot robot) {
        this.robot = robot;
    }

    public Charger getCharger() {
        return charger;
    }

    public void setCharger(Charger charger) {
        this.charger = charger;
    }
}
