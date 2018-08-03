package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationTypePosition;

public class OBPStationTypePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private int positionIndex;

    private String positionState;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stationTypeId;

    private OBPStationTypeDTO obpStationType;

    public OBPStationTypePositionDTO() {
    }

    public OBPStationTypePositionDTO(OBPStationTypePosition entity) {
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

    public String getStationTypeId() {
        return stationTypeId;
    }

    public void setStationTypeId(String stationTypeId) {
        this.stationTypeId = stationTypeId;
    }

    public OBPStationTypeDTO getObpStationType() {
        return obpStationType;
    }

    public void setObpStationType(OBPStationTypeDTO obpStationType) {
        this.obpStationType = obpStationType;
    }
}
