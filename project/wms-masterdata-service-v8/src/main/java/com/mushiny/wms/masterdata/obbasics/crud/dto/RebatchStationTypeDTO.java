package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class RebatchStationTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private List<RebatchStationTypePositionDTO> positions = new ArrayList<>();

    private List<RebatchStationDTO>  rebatchStations=new ArrayList<>();

    public List<RebatchStationDTO> getRebatchStations() {
        return rebatchStations;
    }

    public void setRebatchStations(List<RebatchStationDTO> rebatchStations) {
        this.rebatchStations = rebatchStations;
    }

    public RebatchStationTypeDTO() {
    }

    public RebatchStationTypeDTO(RebatchStationType entity) {
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


    public List<RebatchStationTypePositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<RebatchStationTypePositionDTO> positions) {
        this.positions = positions;
    }

}


