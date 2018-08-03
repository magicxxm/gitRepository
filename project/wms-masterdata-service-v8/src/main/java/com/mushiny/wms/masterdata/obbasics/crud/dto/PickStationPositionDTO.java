package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationPosition;

import javax.validation.constraints.NotNull;

public class PickStationPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String positionState;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workStationPositionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pickStationId;

    private WorkStationPositionDTO workStationPosition;

    private PickStationDTO pickStation;

    public PickStationPositionDTO() {
    }

    public PickStationPositionDTO(PickStationPosition entity) {
        super(entity);
    }


    public String getPositionState() {
        return positionState;
    }

    public void setPositionState(String positionState) {
        this.positionState = positionState;
    }

    public String getWorkStationPositionId() {
        return workStationPositionId;
    }

    public void setWorkStationPositionId(String workStationPositionId) {
        this.workStationPositionId = workStationPositionId;
    }

    public WorkStationPositionDTO getWorkStationPosition() {
        return workStationPosition;
    }

    public void setWorkStationPosition(WorkStationPositionDTO workStationPosition) {
        this.workStationPosition = workStationPosition;
    }

    public String getPickStationId() {
        return pickStationId;
    }

    public void setPickStationId(String pickStationId) {
        this.pickStationId = pickStationId;
    }

    public PickStationDTO getPickStation() {
        return pickStation;
    }

    public void setPickStation(PickStationDTO pickStation) {
        this.pickStation = pickStation;
    }
}
