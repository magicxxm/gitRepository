package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStation;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class IBPStationTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private List<IBPStationTypePositionDTO> positions = new ArrayList<>();

    private List<IBPStationDTO> ibpStations=new ArrayList<>();

    public IBPStationTypeDTO() {
    }

    public List<IBPStationDTO> getIbpStations() {
        return ibpStations;
    }

    public void setIbpStations(List<IBPStationDTO> ibpStations) {
        this.ibpStations = ibpStations;
    }

    public IBPStationTypeDTO(IBPStationType entity) {
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

    public List<IBPStationTypePositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<IBPStationTypePositionDTO> positions) {
        this.positions = positions;
    }

}


