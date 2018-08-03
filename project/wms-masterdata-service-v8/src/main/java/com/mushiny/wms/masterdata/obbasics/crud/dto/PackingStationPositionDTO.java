package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveDestinationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationTypeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStation;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationPosition;

import javax.validation.constraints.NotNull;

public class PackingStationPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String positionState;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workStationPositionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String packingStationId;

    private WorkStationPositionDTO workStationPosition;

    private PackingStationDTO packingStation;

    public PackingStationPositionDTO() {
    }

    public PackingStationPositionDTO(PackingStationPosition entity) {
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

    public String getPackingStationId() {
        return packingStationId;
    }

    public void setPackingStationId(String packingStationId) {
        this.packingStationId = packingStationId;
    }

    public WorkStationPositionDTO getWorkStationPosition() {
        return workStationPosition;
    }

    public void setWorkStationPosition(WorkStationPositionDTO workStationPosition) {
        this.workStationPosition = workStationPosition;
    }

    public PackingStationDTO getPackingStation() {
        return packingStation;
    }

    public void setPackingStation(PackingStationDTO packingStation) {
        this.packingStation = packingStation;
    }
}
