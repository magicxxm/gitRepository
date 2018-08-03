package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWallType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ReBinWallTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private int numberOfRows;

    @NotNull
    private int numberOfColumns;

    private List<ReBinWallTypePositionDTO> positions = new ArrayList<>();

    private List<ReBinCellTypeDTO> rebinCellTypes = new ArrayList<>();

    public ReBinWallTypeDTO() {
    }

    public ReBinWallTypeDTO(ReBinWallType entity) {
        super(entity);
    }

    public List<ReBinWallTypePositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<ReBinWallTypePositionDTO> positions) {
        this.positions = positions;
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

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public List<ReBinCellTypeDTO> getRebinCellTypes() {
        return rebinCellTypes;
    }

    public void setRebinCellTypes(List<ReBinCellTypeDTO> rebinCellTypes) {
        this.rebinCellTypes = rebinCellTypes;
    }
}
