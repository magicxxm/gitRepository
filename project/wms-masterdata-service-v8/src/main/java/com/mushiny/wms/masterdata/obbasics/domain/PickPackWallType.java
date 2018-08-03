package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_PICKPACKWALLTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"})
})
public class PickPackWallType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OrderBy("orderIndex")
    @OneToMany(mappedBy = "pickPackWallType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PickPackWallTypePosition> positions = new ArrayList<>();

    @OneToMany(mappedBy = "pickPackWallType1", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<CellIndex> cellIndex = new ArrayList<>();

    public void addPosition(PickPackWallTypePosition position) {
        getPositions().add(position);
        position.setPickPackWallType(this);
    }
    public void addCellIndex(CellIndex position) {
        getCellIndex().add(position);
        position.setPickPackWallType1(this);
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

    public List<PickPackWallTypePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PickPackWallTypePosition> positions) {
        this.positions = positions;
    }

    public List<CellIndex> getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(List<CellIndex> cellIndex) {
        this.cellIndex = cellIndex;
    }
}
