package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStation;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ReceiveStationDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private String operatorId;

    private UserDTO user;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workstationId;

    private ReceiveStationTypeDTO receiveStationType;

    private WorkStationDTO workstation;

    private List<ReceiveStationPositionDTO> positions = new ArrayList<>();

    public ReceiveStationDTO() {
    }

    public ReceiveStationDTO(ReceiveStation entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public ReceiveStationTypeDTO getReceiveStationType() {
        return receiveStationType;
    }

    public void setReceiveStationType(ReceiveStationTypeDTO receiveStationType) {
        this.receiveStationType = receiveStationType;
    }

    public List<ReceiveStationPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<ReceiveStationPositionDTO> positions) {
        this.positions = positions;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    public WorkStationDTO getWorkstation() {
        return workstation;
    }

    public void setWorkstation(WorkStationDTO workstation) {
        this.workstation = workstation;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
