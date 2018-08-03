package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationPosition;

import javax.validation.constraints.NotNull;

public class SortingStationPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String positionState;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workStationPositionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stationId;

    private WorkStationPositionDTO workStationPosition;

    private SortingStationDTO sortingStation;

    public SortingStationPositionDTO() {
    }

    public SortingStationPositionDTO(SortingStationPosition entity) {
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

    public SortingStationDTO getSortingStation() {
        return sortingStation;
    }

    public void setSortingStation(SortingStationDTO sortingStation) {
        this.sortingStation = sortingStation;
    }
}
