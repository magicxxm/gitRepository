package com.mushiny.wms.outboundproblem.domain.common;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="OB_PICKINGUNITLOAD")
public class PickingUnitLoad extends BaseClientAssignedEntity {

    @ManyToOne
    @JoinColumn(name="PICKINGORDER_ID")
    private PickingOrder pickingOrder;

    @ManyToOne
    @JoinColumn(name="UNITLOAD_ID")
    private UnitLoad unitLoad;

    public PickingOrder getPickingOrder() {
        return pickingOrder;
    }

    public void setPickingOrder(PickingOrder pickingOrder) {
        this.pickingOrder = pickingOrder;
    }

    public UnitLoad getUnitLoad() {
        return unitLoad;
    }

    public void setUnitLoad(UnitLoad unitLoad) {
        this.unitLoad = unitLoad;
    }
}
