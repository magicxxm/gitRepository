package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackFieldType;

import javax.validation.constraints.NotNull;

public class PickPackFieldTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private int numberOfColumns = 0;

    private int numberOfRows = 0;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pickPackCellTypeId;

    private PickPackCellTypeDTO pickPackCellType;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pickPackWallTypeId;

    private PickPackWallTypeDTO pickPackWallType;

    public PickPackFieldTypeDTO() {
    }

    public PickPackFieldTypeDTO(PickPackFieldType entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public String getPickPackCellTypeId() {
        return pickPackCellTypeId;
    }

    public void setPickPackCellTypeId(String pickPackCellTypeId) {
        this.pickPackCellTypeId = pickPackCellTypeId;
    }

    public PickPackCellTypeDTO getPickPackCellType() {
        return pickPackCellType;
    }

    public void setPickPackCellType(PickPackCellTypeDTO pickPackCellType) {
        this.pickPackCellType = pickPackCellType;
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
}
