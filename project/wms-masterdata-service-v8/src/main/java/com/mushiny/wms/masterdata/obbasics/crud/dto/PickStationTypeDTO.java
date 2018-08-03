package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PickStationTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)

    private String pickPackWallTypeId;

    private String pickStationType;
    private String isPrintFHD;
    private String isPrintZXD;
    private String isPrintQD;


    private PickPackWallTypeDTO pickPackWallType;

    private List<PickStationTypePositionDTO> positions = new ArrayList<>();

    private List<PickPackWallDTO> pickpackWalls=new ArrayList<>();

    private List<PickStationDTO> pickStations=new ArrayList<>();

    public PickStationTypeDTO() {
    }

    public List<PickPackWallDTO> getPickpackWalls() {
        return pickpackWalls;
    }

    public void setPickpackWalls(List<PickPackWallDTO> pickpackWalls) {
        this.pickpackWalls = pickpackWalls;
    }

    public List<PickStationDTO> getPickStations() {
        return pickStations;
    }

    public void setPickStations(List<PickStationDTO> pickStations) {
        this.pickStations = pickStations;
    }

    public PickStationTypeDTO(PickStationType entity) {
        super(entity);
    }

    public String getPickStationType() {
        return pickStationType;
    }

    public void setPickStationType(String pickStationType) {
        this.pickStationType = pickStationType;
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


    public List<PickStationTypePositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<PickStationTypePositionDTO> positions) {
        this.positions = positions;
    }

    public String getPickPackWallTypeId() {
        return pickPackWallTypeId;
    }

    public void setPickPackWallTypeId(String pickPackWallTypeId) {
        this.pickPackWallTypeId = pickPackWallTypeId;
    }

    public PickPackWallTypeDTO getPickPackWallType() {
        return pickPackWallType;
    }

    public void setPickPackWallType(PickPackWallTypeDTO pickPackWallType) {
        this.pickPackWallType = pickPackWallType;
    }

    public String getIsPrintFHD() {
        return isPrintFHD;
    }

    public void setIsPrintFHD(String isPrintFHD) {
        this.isPrintFHD = isPrintFHD;
    }

    public String getIsPrintZXD() {
        return isPrintZXD;
    }

    public void setIsPrintZXD(String isPrintZXD) {
        this.isPrintZXD = isPrintZXD;
    }

    public String getIsPrintQD() {
        return isPrintQD;
    }

    public void setIsPrintQD(String isPrintQD) {
        this.isPrintQD = isPrintQD;
    }
}


