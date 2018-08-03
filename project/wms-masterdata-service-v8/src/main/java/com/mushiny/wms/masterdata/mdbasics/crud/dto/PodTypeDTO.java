package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.PodType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PodTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private int numberOfColumns = 0;

    private int numberOfRows = 0;

    private int height = 0;

    private int width = 0;

    private int depth = 0;

    private List<PodTypePositionDTO> positions = new ArrayList<>();

    public PodTypeDTO() {
    }

    public PodTypeDTO(PodType entity) {
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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<PodTypePositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<PodTypePositionDTO> positions) {
        this.positions = positions;
    }
}
