package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/5/31.
 */
@Entity
@Table(name = "OB_PICKPACKWALLTYPEPOSITION")
public class PickPackWallTypePosition extends BaseWarehouseAssignedEntity {

    @ManyToOne
    @JoinColumn(name = "FIELDTYPE_ID")
    private PickPackFieldType pickPackFieldType;

    @Column(name = "ORDER_INDEX")
    private int orderIndex;

    @ManyToOne
    @JoinColumn(name = "WALLTYPE_ID")
    private PickPackWallType pickPackWallType;

    public PickPackFieldType getPickPackFieldType() {
        return pickPackFieldType;
    }

    public void setPickPackFieldType(PickPackFieldType pickPackFieldType) {
        this.pickPackFieldType = pickPackFieldType;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public PickPackWallType getPickPackWallType() {
        return pickPackWallType;
    }

    public void setPickPackWallType(PickPackWallType pickPackWallType) {
        this.pickPackWallType = pickPackWallType;
    }
}
