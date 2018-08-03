package com.mushiny.wms.outboundproblem.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.outboundproblem.domain.OBPStationType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class OBPStationTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private List<OBPStationTypePositionDTO> positions = new ArrayList<>();

    public OBPStationTypeDTO() {
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
}
