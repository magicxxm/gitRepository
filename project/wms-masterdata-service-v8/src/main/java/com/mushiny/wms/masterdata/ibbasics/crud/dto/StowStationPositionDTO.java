package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationPosition;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationPositionDTO;

import javax.validation.constraints.NotNull;

public class StowStationPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String positionState;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workStationPositionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stowStationId;

    private WorkStationPositionDTO workStationPosition;

    private StowStationDTO stowStation;

    public StowStationPositionDTO() {
    }

    public StowStationPositionDTO(StowStationPosition entity) {
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

    public String getStowStationId() {
        return stowStationId;
    }

    public void setStowStationId(String stowStationId) {
        this.stowStationId = stowStationId;
    }

    public StowStationDTO getStowStation() {
        return stowStation;
    }

    public void setStowStation(StowStationDTO stowStation) {
        this.stowStation = stowStation;
    }
}


