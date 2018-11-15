package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/5/15.
 */
@Entity
@Table(name = "OB_PACKINGSTATIONTYPEPOSITION")
public class PackingStationTypePosition extends BaseWarehouseAssignedEntity {

    @Column(name = "POSITION_INDEX")
    private int positionIndex;

    @Column(name = "POSITION_STATE")
    private String positionState;

    @ManyToOne
    @JoinColumn(name = "STATIONTYPE_ID")
    private PackingStationType packingStationType;

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

    public PackingStationType getPackingStationType() {
        return packingStationType;
    }

    public void setPackingStationType(PackingStationType packingStationType) {
        this.packingStationType = packingStationType;
    }
}
