package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class OBPStationTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;
    @NotNull
    private String type;
    private String description;

    private List<OBPStationTypePositionDTO> positions = new ArrayList<>();

    private List<OBPStationDTO> obpStations=new ArrayList<>();

    public OBPStationTypeDTO() {
    }

    public List<OBPStationDTO> getObpStations() {
        return obpStations;
    }

    public void setObpStations(List<OBPStationDTO> obpStations) {
        this.obpStations = obpStations;
    }

    public OBPStationTypeDTO(OBPStationType entity) {
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

    public List<OBPStationTypePositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<OBPStationTypePositionDTO> positions) {
        this.positions = positions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


