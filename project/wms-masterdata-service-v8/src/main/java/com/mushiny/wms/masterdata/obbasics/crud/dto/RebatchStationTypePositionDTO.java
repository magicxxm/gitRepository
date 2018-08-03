package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationTypePosition;

public class RebatchStationTypePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private int positionIndex;

    private String positionState;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    private RebatchStationTypeDTO rebatchStationType;

    public RebatchStationTypePositionDTO() {
    }

    public RebatchStationTypePositionDTO(RebatchStationTypePosition entity) {
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public RebatchStationTypeDTO getRebatchStationType() {
        return rebatchStationType;
    }

    public void setRebatchStationType(RebatchStationTypeDTO rebatchStationType) {
        this.rebatchStationType = rebatchStationType;
    }
}
