package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationTypePosition;

import javax.validation.constraints.NotNull;

public class StowStationTypePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private int positionIndex;

    @NotNull
    private String positionState;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stowStationTypeId;

    private StowStationTypeDTO stowStationType;

    public StowStationTypePositionDTO() {
    }

    public StowStationTypePositionDTO(StowStationTypePosition entity) {
        super(entity);
    }

    public String getPositionState() {
        return positionState;
    }

    public void setPositionState(String positionState) {
        this.positionState = positionState;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public String getStowStationTypeId() {
        return stowStationTypeId;
    }

    public void setStowStationTypeId(String stowStationTypeId) {
        this.stowStationTypeId = stowStationTypeId;
    }

    public StowStationTypeDTO getStowStationType() {
        return stowStationType;
    }

    public void setStowStationType(StowStationTypeDTO stowStationType) {
        this.stowStationType = stowStationType;
    }
}


