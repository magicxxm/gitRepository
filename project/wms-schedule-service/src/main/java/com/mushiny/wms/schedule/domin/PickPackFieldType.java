package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/4/28.
 */
@Entity
@Table(name = "OB_PICKPACKFIELDTYPE")
public class PickPackFieldType extends BaseWarehouseAssignedEntity {
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "NUMBER_OF_COLUMNS")
    private int numberOfColumns;

    @Column(name = "NUMBER_OF_ROWS")
    private int numberOfRows;

    @ManyToOne
    @JoinColumn(name = "PICKPACKCELLTYPE_ID", nullable = false)
    private PickPackCellType pickPackCellType;

  /*  @Column(name="FIELD_INDEX")
    private int fieldIndex;*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public PickPackCellType getPickPackCellType() {
        return pickPackCellType;
    }

    public void setPickPackCellType(PickPackCellType pickPackCellType) {
        this.pickPackCellType = pickPackCellType;
    }

}
