package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OBP_OBPSTATIONTYPEPOSITION")
public class OBPStationTypePosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_INDEX")
    private int positionIndex;

    @Column(name = "POSITION_STATE")
    private String positionState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STATIONTYPE_ID", nullable = false)
    private OBPStationType obpStationType;

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

    public OBPStationType getObpStationType() {
        return obpStationType;
    }

    public void setObpStationType(OBPStationType obpStationType) {
        this.obpStationType = obpStationType;
    }
}
