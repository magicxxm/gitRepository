package com.mushiny.wms.outboundproblem.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OBP_OBPWALL", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"})
})
public class OBPWall extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATE")
    private String state;

    @Column(name = "NUMBER_OF_COLUMNS", nullable = false)
    private int numberOfColumns;

    @Column(name = "NUMBER_OF_ROWS", nullable = false)
    private int numberOfRows;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private OBPWallType obpWallType;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public OBPWallType getObpWallType() {
        return obpWallType;
    }

    public void setObpWallType(OBPWallType obpWallType) {
        this.obpWallType = obpWallType;
    }
}
