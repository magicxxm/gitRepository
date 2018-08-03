package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationPosition;

import javax.validation.constraints.NotNull;

public class RebatchStationPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String positionState;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workStationPositionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stationId;

    private WorkStationPositionDTO workStationPosition;

    private RebatchStationDTO rebatchStation;

    public RebatchStationPositionDTO() {
    }

    public RebatchStationPositionDTO(RebatchStationPosition entity) {
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

    public RebatchStationDTO getRebatchStation() {
        return rebatchStation;
    }

    public void setRebatchStation(RebatchStationDTO rebatchStation) {
        this.rebatchStation = rebatchStation;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
