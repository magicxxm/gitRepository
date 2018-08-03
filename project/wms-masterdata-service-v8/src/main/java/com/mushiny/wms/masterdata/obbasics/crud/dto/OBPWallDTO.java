package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPWall;

import javax.validation.constraints.NotNull;

public class OBPWallDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private String state;

    @NotNull
    private int numberOfColumns;

    @NotNull
    private int numberOfRows;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    private OBPWallTypeDTO obpWallType;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String obpCellTypeId;

    public OBPWallDTO() {
    }

    public OBPWallDTO(OBPWall entity) {
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public OBPWallTypeDTO getObpWallType() {
        return obpWallType;
    }

    public void setObpWallType(OBPWallTypeDTO obpWallType) {
        this.obpWallType = obpWallType;
    }

    public String getObpCellTypeId() {
        return obpCellTypeId;
    }

    public void setObpCellTypeId(String obpCellTypeId) {
        this.obpCellTypeId = obpCellTypeId;
    }
}
