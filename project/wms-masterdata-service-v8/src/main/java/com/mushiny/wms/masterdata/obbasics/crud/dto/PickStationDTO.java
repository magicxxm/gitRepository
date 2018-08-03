package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickStation;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PickStationDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    private PickStationTypeDTO pickStationType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String operatorId;

    private UserDTO user;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workstationId;

    private WorkStationDTO workstation;

    private List<PickStationPositionDTO> positions = new ArrayList<>();

    public PickStationDTO() {
    }

    public PickStationDTO(PickStation entity) {
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



    public List<PickStationPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<PickStationPositionDTO> positions) {
        this.positions = positions;
    }


    public PickStationTypeDTO getPickStationType() {
        return pickStationType;
    }

    public void setPickStationType(PickStationTypeDTO pickStationType) {
        this.pickStationType = pickStationType;
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
}
