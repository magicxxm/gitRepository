package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationTypePosition;

public class IBPStationTypePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private int positionIndex;

    private String positionState;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stationTypeId;

    private IBPStationTypeDTO ibpStationType;

    public IBPStationTypePositionDTO() {
    }

    public IBPStationTypePositionDTO(IBPStationTypePosition entity) {
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

    public IBPStationTypeDTO getIbpStationType() {
        return ibpStationType;
    }

    public void setIbpStationType(IBPStationTypeDTO ibpStationType) {
        this.ibpStationType = ibpStationType;
    }
}
