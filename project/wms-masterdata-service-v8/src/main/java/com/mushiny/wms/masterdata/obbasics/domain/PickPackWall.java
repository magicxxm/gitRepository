package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_PICKPACKWALL")
public class PickPackWall extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "NUMBER_OF_COLUMNS", nullable = false)
    private int numberOfColumns = 0;

    @Column(name = "NUMBER_OF_ROWS", nullable = false)
    private int numberOfRows = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private PickPackWallType pickPackWallType;

    @OneToMany(mappedBy = "pickPackWall", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PickPackCell> pickPackCell = new ArrayList<>();

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

    public PickPackWallType getPickPackWallType() {
        return pickPackWallType;
    }

    public void setPickPackWallType(PickPackWallType pickPackWallType) {
        this.pickPackWallType = pickPackWallType;
    }

    public List<PickPackCell> getPickPackCell() {
        return pickPackCell;
    }

    public void setPickPackCell(List<PickPackCell> pickPackCell) {
        this.pickPackCell = pickPackCell;
    }
}
