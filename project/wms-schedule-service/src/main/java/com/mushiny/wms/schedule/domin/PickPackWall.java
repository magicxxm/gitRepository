package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/4/26.
 */
@Entity
@Table(name = "OB_PICKPACKWALL")
public class PickPackWall extends BaseWarehouseAssignedEntity {

    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "NUMBER_OF_COLUMNS")
    private int numberOfColumns;

    @Column(name = "NUMBER_OF_ROWS")
    private int numberOfRows;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private PickPackWallType pickPackWallType;

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
}
