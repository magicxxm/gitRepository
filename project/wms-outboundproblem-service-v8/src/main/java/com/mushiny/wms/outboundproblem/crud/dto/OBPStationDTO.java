package com.mushiny.wms.outboundproblem.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.outboundproblem.domain.OBPStation;
import com.mushiny.wms.outboundproblem.domain.common.WorkStation;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

public class OBPStationDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private String state;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    private OBPStationTypeDTO obpStationType;

    @ManyToOne
    private WorkStation workStation;

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

    public OBPStationDTO() {
    }

    public OBPStationDTO(OBPStation entity) {
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public OBPStationTypeDTO getObpStationType() {
        return obpStationType;
    }

    public void setObpStationType(OBPStationTypeDTO obpStationType) {
        this.obpStationType = obpStationType;
    }
}
