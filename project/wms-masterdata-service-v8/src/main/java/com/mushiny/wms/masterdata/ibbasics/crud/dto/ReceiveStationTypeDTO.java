package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ReceiveStationTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private String receiveStationType;

    private List<ReceiveStationTypePositionDTO> positions = new ArrayList<>();

    private List<ReceiveStationDTO> receiveStations=new ArrayList<>();

    public ReceiveStationTypeDTO() {
    }

    public List<ReceiveStationDTO> getReceiveStations() {
        return receiveStations;
    }

    public void setReceiveStations(List<ReceiveStationDTO> receiveStations) {
        this.receiveStations = receiveStations;
    }

    public ReceiveStationTypeDTO(ReceiveStationType entity) {
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

    public String getReceiveStationType() {
        return receiveStationType;
    }

    public void setReceiveStationType(String receiveStationType) {
        this.receiveStationType = receiveStationType;
    }

    public List<ReceiveStationTypePositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<ReceiveStationTypePositionDTO> positions) {
        this.positions = positions;
    }
}
