package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinStation;

import javax.validation.constraints.NotNull;

public class ReBinStationDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    private String operatorId;

    private UserDTO user;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workstationId;

    private ReBinStationTypeDTO rebinStationType;

    private WorkStationDTO workstation;

    public ReBinStationDTO() {
    }

    public ReBinStationDTO(ReBinStation entity) {
        super(entity);
    }

    public ReBinStationTypeDTO getRebinStationType() {
        return rebinStationType;
    }

    public void setRebinStationType(ReBinStationTypeDTO rebinStationType) {
        this.rebinStationType = rebinStationType;
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

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
