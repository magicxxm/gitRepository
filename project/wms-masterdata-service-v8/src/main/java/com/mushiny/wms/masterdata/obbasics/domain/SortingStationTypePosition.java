package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OB_SORTINGSTATIONTYPEPOSITION")
public class SortingStationTypePosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_INDEX")
    private int positionIndex;

    @Column(name = "POSITION_STATE")
    private String positionState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STATIONTYPE_ID", nullable = false)
    private SortingStationType sortingStationType;

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

    public SortingStationType getSortingStationType() {
        return sortingStationType;
    }

    public void setSortingStationType(SortingStationType sortingStationType) {
        this.sortingStationType = sortingStationType;
    }
}
