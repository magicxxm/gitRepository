package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPCell;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationPosition;

import javax.validation.constraints.NotNull;

public class OBPStationPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String positionState;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workStationPositionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stationId;

    private WorkStationPositionDTO workStationPosition;

    private OBPStationDTO obpStation;

    public OBPStationPositionDTO() {
    }

    public OBPStationPositionDTO(OBPStationPosition entity) {
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

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public OBPStationDTO getObpStation() {
        return obpStation;
    }

    public void setObpStation(OBPStationDTO obpStation) {
        this.obpStation = obpStation;
    }
}
