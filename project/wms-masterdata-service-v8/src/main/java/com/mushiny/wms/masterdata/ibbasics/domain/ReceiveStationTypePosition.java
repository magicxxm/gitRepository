package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "IB_RECEIVESTATIONTYPEPOSITION")
public class ReceiveStationTypePosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_INDEX")
    private int positionIndex;

    @Column(name = "POSITION_STATE")
    private String positionState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RECEIVEDESTINATION_ID")
    private ReceiveDestination receivingDestination;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STATIONTYPE_ID")
    private ReceiveStationType receivingStationType;


    public ReceiveDestination getReceivingDestination() {
        return receivingDestination;
    }

    public void setReceivingDestination(ReceiveDestination receivingDestination) {
        this.receivingDestination = receivingDestination;
    }

    public ReceiveStationType getReceivingStationType() {
        return receivingStationType;
    }

    public void setReceivingStationType(ReceiveStationType receivingStationType) {
        this.receivingStationType = receivingStationType;
    }

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
}
