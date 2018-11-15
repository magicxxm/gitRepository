package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_REBINWALLTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NAME", "WAREHOUSE_ID"})
})
public class RebinWallType extends BaseWarehouseAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "NUMBER_OF_ROWS", nullable = false)
    private int numberOfRows = 0;

    @Column(name = "NUMBER_OF_COLUMNS", nullable = false)
    private int numberOfColumns = 0;

    @OneToMany(mappedBy = "rebinWallType")
    @OrderBy("index ASC")
    private List<RebinWallTypePosition> positions = new ArrayList<>();

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

    public List<RebinWallTypePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<RebinWallTypePosition> positions) {
        this.positions = positions;
    }

}
