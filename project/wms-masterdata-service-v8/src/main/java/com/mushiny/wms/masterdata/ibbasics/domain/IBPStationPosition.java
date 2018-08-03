package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;

import javax.persistence.*;

@Entity
@Table(name = "IBP_IBPSTATIONPOSITION")
public class IBPStationPosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATIONPOSITION_ID", nullable = false)
    private WorkStationPosition workStationPosition;

    @Column(name = "POSITION_STATE", nullable = false)
    private String positionState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STATION_ID", nullable = false)
    private IBPStation ibpStation;


    public WorkStationPosition getWorkStationPosition() {
        return workStationPosition;
    }

    public void setWorkStationPosition(WorkStationPosition workStationPosition) {
        this.workStationPosition = workStationPosition;
    }

    public String getPositionState() {
        return positionState;
    }

    public void setPositionState(String positionState) {
        this.positionState = positionState;
    }

    public IBPStation getIbpStation() {
        return ibpStation;
    }

    public void setIbpStation(IBPStation ibpStation) {
        this.ibpStation = ibpStation;
    }
}
