package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OB_REBINWALL", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NAME", "WAREHOUSE_ID"})
})
public class RebinWall extends BaseWarehouseAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "NUMBER_OF_ROWS", nullable = false)
    private int numberOfRows = 0;

    @Column(name = "NUMBER_OF_COLUMNS", nullable = false)
    private int numberOfColumns = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private RebinWallType rebinWallType;

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

    public RebinWallType getRebinWallType() {
        return rebinWallType;
    }

    public void setRebinWallType(RebinWallType rebinWallType) {
        this.rebinWallType = rebinWallType;
    }
}
