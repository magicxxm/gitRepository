package com.mushiny.wcs.application.business.score;

import com.mushiny.wcs.application.business.common.CommonBusiness;
import com.mushiny.wcs.application.business.common.SystemPropertyBusiness;
import com.mushiny.wcs.application.business.dto.RobotChargerScore;
import com.mushiny.wcs.application.domain.Charger;
import com.mushiny.wcs.application.domain.MapNode;
import com.mushiny.wcs.application.domain.WCSRobot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class RobotChargerScoreBusiness {

    private final CommonBusiness commonBusiness;
    private final SystemPropertyBusiness systemPropertyBusiness;

    @Autowired
    public RobotChargerScoreBusiness(CommonBusiness commonBusiness,
                                     SystemPropertyBusiness systemPropertyBusiness) {
        this.commonBusiness = commonBusiness;
        this.systemPropertyBusiness = systemPropertyBusiness;
    }

    public List<RobotChargerScore> getScores(List<Charger> chargers,
                                             List<WCSRobot> robots,
                                             String warehouseId) {
        List<RobotChargerScore> robotChargerScores = new ArrayList<>();
        BigDecimal chargerConstant = BigDecimal.valueOf(systemPropertyBusiness.getDriveChargerConstant(warehouseId));
        for (Charger charger : chargers) {
            MapNode mapNode = commonBusiness.getChargerNode(charger);
            for (WCSRobot robot : robots) {

                BigDecimal fetch = BigDecimal.valueOf(
                        Math.abs(robot.getxPosition() - mapNode.getxPosition())
                                + Math.abs(robot.getyPosition() - mapNode.getyPosition()));
                BigDecimal laveBattery = BigDecimal.valueOf(100 - robot.getLaveBattery());
                BigDecimal score = fetch.subtract((chargerConstant.multiply(laveBattery)));
                RobotChargerScore robotChargerScore = new RobotChargerScore();
                robotChargerScore.setCharger(charger);
                robotChargerScore.setRobot(robot);
                robotChargerScore.setScore(score);
                robotChargerScores.add(robotChargerScore);
            }
        }
        return robotChargerScores;
    }
}
