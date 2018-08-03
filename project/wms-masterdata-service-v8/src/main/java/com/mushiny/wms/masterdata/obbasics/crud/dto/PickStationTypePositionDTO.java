package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationTypePosition;

public class PickStationTypePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private int positionIndex;

    private String positionState;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pickStationTypeId;

    private PickStationTypeDTO pickStationType;

    public PickStationTypePositionDTO() {
    }

    public PickStationTypePositionDTO(PickStationTypePosition entity) {
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

    public String getPickStationTypeId() {
        return pickStationTypeId;
    }

    public void setPickStationTypeId(String pickStationTypeId) {
        this.pickStationTypeId = pickStationTypeId;
    }

    public PickStationTypeDTO getPickStationType() {
        return pickStationType;
    }

    public void setPickStationType(PickStationTypeDTO pickStationType) {
        this.pickStationType = pickStationType;
    }
}
