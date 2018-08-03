package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickingArea;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PickingAreaDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private List<PickingAreaPositionDTO> positions = new ArrayList<>();

    public PickingAreaDTO() {
    }

    public PickingAreaDTO(PickingArea entity) {
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

    public List<PickingAreaPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<PickingAreaPositionDTO> positions) {
        this.positions = positions;
    }
}
