package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;

import javax.persistence.*;

@Entity
@Table(name = "IB_RECEIVESTATIONPOSITION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "ID"
        })
})
public class ReceiveStationPosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_STATE", nullable = false)
    private String positionState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATIONPOSITION_ID", nullable = false)
    private WorkStationPosition workStationPosition;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STATION_ID", nullable = false)
    private ReceiveStation receiveStation;

    public String getPositionState() {
        return positionState;
    }

    public void setPositionState(String positionState) {
        this.positionState = positionState;
    }

    public WorkStationPosition getWorkStationPosition() {
        return workStationPosition;
    }

    public void setWorkStationPosition(WorkStationPosition workStationPosition) {
        this.workStationPosition = workStationPosition;
    }

    public ReceiveStation getReceiveStation() {
        return receiveStation;
    }

    public void setReceiveStation(ReceiveStation receiveStation) {
        this.receiveStation = receiveStation;
    }
}
