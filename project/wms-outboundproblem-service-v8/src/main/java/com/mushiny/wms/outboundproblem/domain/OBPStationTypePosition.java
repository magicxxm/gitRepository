package com.mushiny.wms.outboundproblem.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OBP_OBPSTATIONTYPEPOSITION")
public class OBPStationTypePosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_INDEX")
    private int positionIndex;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STATIONTYPE_ID")
    private OBPStationType obpStationType;

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public OBPStationType getObpStationType() {
        return obpStationType;
    }

    public void setObpStationType(OBPStationType obpStationType) {
        this.obpStationType = obpStationType;
    }
}
