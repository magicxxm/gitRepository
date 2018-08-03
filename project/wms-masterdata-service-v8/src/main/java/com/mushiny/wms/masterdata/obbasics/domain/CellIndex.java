package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OB_PICKPACKWALLCELLINDEX")

public class CellIndex extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NUMBER", nullable = false)
    private int number;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PICK_PACK_WALL_TYPE_ID", nullable = false)
    private PickPackWallType pickPackWallType1;

    @Column(name = "CELL_INDEX", nullable = false)
    private int cellIndex;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public PickPackWallType getPickPackWallType1() {
        return pickPackWallType1;
    }

    public void setPickPackWallType1(PickPackWallType pickPackWallType1) {
        this.pickPackWallType1 = pickPackWallType1;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

}
