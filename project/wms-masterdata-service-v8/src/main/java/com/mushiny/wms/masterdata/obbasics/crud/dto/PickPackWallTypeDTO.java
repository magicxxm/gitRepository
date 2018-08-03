package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.CellIndex;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackFieldType;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackWallType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PickPackWallTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private List<PickPackFieldTypeDTO> pickPackFieldTypes = new ArrayList<>();

    private String pickPackFieldTypeId;

    private List<PickPackWallTypePositionDTO> positions = new ArrayList<>();

    private List<CellIndexDTO> cellPositions = new ArrayList<>();

    public PickPackWallTypeDTO() {
    }

    public PickPackWallTypeDTO(PickPackWallType entity) {
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

    public String getPickPackFieldTypeId() {
        return pickPackFieldTypeId;
    }

    public void setPickPackFieldTypeId(String pickPackFieldTypeId) {
        this.pickPackFieldTypeId = pickPackFieldTypeId;
    }

    public List<PickPackWallTypePositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<PickPackWallTypePositionDTO> positions) {
        this.positions = positions;
    }


    public List<PickPackFieldTypeDTO> getPickPackFieldTypes() {
        return pickPackFieldTypes;
    }

    public void setPickPackFieldTypes(List<PickPackFieldTypeDTO> pickPackFieldTypes) {
        this.pickPackFieldTypes = pickPackFieldTypes;
    }

    public List<CellIndexDTO> getCellPositions() {
        return cellPositions;
    }

    public void setCellPositions(List<CellIndexDTO> cellPositions) {
        this.cellPositions = cellPositions;
    }
}
