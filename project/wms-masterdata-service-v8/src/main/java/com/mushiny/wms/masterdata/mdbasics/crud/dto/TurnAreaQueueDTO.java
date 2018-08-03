package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.TurnAreaQueue;

import javax.validation.constraints.NotNull;

public class TurnAreaQueueDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private String turnAreaPositionId;

    @NotNull
    private String robotId;

    public TurnAreaQueueDTO() {
    }

    public TurnAreaQueueDTO(TurnAreaQueue entity) {
        super(entity);
    }

    public String getTurnAreaPositionId() {
        return turnAreaPositionId;
    }

    public void setTurnAreaPositionId(String turnAreaPositionId) {
        this.turnAreaPositionId = turnAreaPositionId;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }
}
