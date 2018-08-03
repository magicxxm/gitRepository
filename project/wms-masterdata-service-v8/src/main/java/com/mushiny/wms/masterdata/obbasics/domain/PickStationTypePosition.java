package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OB_PICKSTATIONTYPEPOSITION")
public class PickStationTypePosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_INDEX")
    private int positionIndex;

    @Column(name = "POSITION_STATE")
    private String positionState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PICKSTATIONTYPE_ID", nullable = false)
    private PickStationType pickStationType;

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public String getPositionState() {
        return positionState;
    }

    public void setPositionState(String positionState) {
        this.positionState = positionState;
    }

    public PickStationType getPickStationType() {
        return pickStationType;
    }

    public void setPickStationType(PickStationType pickStationType) {
        this.pickStationType = pickStationType;
    }
}
