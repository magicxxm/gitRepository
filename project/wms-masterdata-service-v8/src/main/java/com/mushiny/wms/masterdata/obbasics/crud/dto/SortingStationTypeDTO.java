package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class SortingStationTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private List<SortingStationTypePositionDTO> positions = new ArrayList<>();

    private List<SortingStationDTO> sortStations=new ArrayList<>();

    public List<SortingStationDTO> getSortStations() {
        return sortStations;
    }

    public void setSortStations(List<SortingStationDTO> sortStations) {
        this.sortStations = sortStations;
    }

    public SortingStationTypeDTO() {
    }

    public SortingStationTypeDTO(SortingStationType entity) {
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


    public List<SortingStationTypePositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<SortingStationTypePositionDTO> positions) {
        this.positions = positions;
    }

}


