package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;

import com.mushiny.wms.masterdata.obbasics.domain.PackingStationTypePosition;

import javax.validation.constraints.NotNull;

public class PackingStationTypePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private int positionIndex;

    private String positionState;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String PackingStationTypeId;

    private PackingStationTypeDTO packingStationType;

    public PackingStationTypePositionDTO() {
    }

    public PackingStationTypePositionDTO(PackingStationTypePosition entity) {
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

    public String getPackingStationTypeId() {
        return PackingStationTypeId;
    }

    public void setPackingStationTypeId(String packingStationTypeId) {
        PackingStationTypeId = packingStationTypeId;
    }

    public PackingStationTypeDTO getPackingStationType() {
        return packingStationType;
    }

    public void setPackingStationType(PackingStationTypeDTO packingStationType) {
        this.packingStationType = packingStationType;
    }
}
