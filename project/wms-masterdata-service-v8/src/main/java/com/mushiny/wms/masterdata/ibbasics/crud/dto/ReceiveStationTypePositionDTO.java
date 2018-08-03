package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationTypePosition;

import javax.validation.constraints.NotNull;

public class ReceiveStationTypePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private int positionIndex;

    private String positionState;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receiveDestinationId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receiveStationTypeId;

    private ReceiveDestinationDTO receiveDestination;

    private ReceiveStationTypeDTO receiveStationType;

    public ReceiveStationTypePositionDTO() {
    }

    public ReceiveStationTypePositionDTO(ReceiveStationTypePosition entity) {
        super(entity);
    }

    public String getReceiveDestinationId() {
        return receiveDestinationId;
    }

    public void setReceiveDestinationId(String receiveDestinationId) {
        this.receiveDestinationId = receiveDestinationId;
    }

    public String getReceiveStationTypeId() {
        return receiveStationTypeId;
    }

    public void setReceiveStationTypeId(String receiveStationTypeId) {
        this.receiveStationTypeId = receiveStationTypeId;
    }

    public ReceiveDestinationDTO getReceiveDestination() {
        return receiveDestination;
    }

    public void setReceiveDestination(ReceiveDestinationDTO receiveDestination) {
        this.receiveDestination = receiveDestination;
    }

    public ReceiveStationTypeDTO getReceiveStationType() {
        return receiveStationType;
    }

    public void setReceiveStationType(ReceiveStationTypeDTO receiveStationType) {
        this.receiveStationType = receiveStationType;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public String getPositionState() {
        return positionState;
    }

    public void setPositionState(String positionState) {
        this.positionState = positionState;
    }
}
