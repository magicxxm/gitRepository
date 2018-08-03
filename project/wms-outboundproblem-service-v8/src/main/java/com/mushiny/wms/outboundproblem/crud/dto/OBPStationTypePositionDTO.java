package com.mushiny.wms.outboundproblem.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.outboundproblem.domain.OBPStationTypePosition;

public class OBPStationTypePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private int positionIndex;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String obpStationTypeId;

    private OBPStationTypeDTO obpStationType;

    public OBPStationTypePositionDTO() {
    }

    public OBPStationTypePositionDTO(OBPStationTypePosition entity) {
        super(entity);
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public String getObpStationTypeId() {
        return obpStationTypeId;
    }

    public void setObpStationTypeId(String obpStationTypeId) {
        this.obpStationTypeId = obpStationTypeId;
    }

    public OBPStationTypeDTO getObpStationType() {
        return obpStationType;
    }

    public void setObpStationType(OBPStationTypeDTO obpStationType) {
        this.obpStationType = obpStationType;
    }
}
