package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinStationType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ReBinStationTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rebinWallTypeId;

    private List<ReBinStationDTO> rebinStations = new ArrayList<>();

    private List<ReBinWallDTO> rebinWalls = new ArrayList<>();

    private ReBinWallTypeDTO rebinWallType;

    public ReBinStationTypeDTO() {
    }

    public ReBinStationTypeDTO(ReBinStationType entity) {
        super(entity);
    }

    public String getRebinWallTypeId() {
        return rebinWallTypeId;
    }

    public void setRebinWallTypeId(String rebinWallTypeId) {
        this.rebinWallTypeId = rebinWallTypeId;
    }

    public ReBinWallTypeDTO getRebinWallType() {
        return rebinWallType;
    }

    public void setRebinWallType(ReBinWallTypeDTO rebinWallType) {
        this.rebinWallType = rebinWallType;
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

    public List<ReBinStationDTO> getRebinStations() {
        return rebinStations;
    }

    public void setRebinStations(List<ReBinStationDTO> rebinStations) {
        this.rebinStations = rebinStations;
    }

    public List<ReBinWallDTO> getRebinWalls() {
        return rebinWalls;
    }

    public void setRebinWalls(List<ReBinWallDTO> rebinWalls) {
        this.rebinWalls = rebinWalls;
    }
}
