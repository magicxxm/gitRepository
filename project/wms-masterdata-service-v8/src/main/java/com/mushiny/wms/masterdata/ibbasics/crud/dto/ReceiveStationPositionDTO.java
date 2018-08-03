package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationPosition;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationPositionDTO;

import javax.validation.constraints.NotNull;

public class ReceiveStationPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String positionState;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workStationPositionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String receiveStationId;

    private WorkStationPositionDTO workStationPosition;

    private ReceiveStationDTO receiveStation;

    public ReceiveStationPositionDTO() {
    }

    public ReceiveStationPositionDTO(ReceiveStationPosition entity) {
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

    public String getReceiveStationId() {
        return receiveStationId;
    }

    public void setReceiveStationId(String receiveStationId) {
        this.receiveStationId = receiveStationId;
    }

    public WorkStationPositionDTO getWorkStationPosition() {
        return workStationPosition;
    }

    public void setWorkStationPosition(WorkStationPositionDTO workStationPosition) {
        this.workStationPosition = workStationPosition;
    }

    public ReceiveStationDTO getReceiveStation() {
        return receiveStation;
    }

    public void setReceiveStation(ReceiveStationDTO receiveStation) {
        this.receiveStation = receiveStation;
    }
}
