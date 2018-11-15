package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "RCS_TRIP")
public class RcsTrip extends BaseWarehouseAssignedEntity {

    @Column(name = "TRIP_STATE")
    private String tripState;

    @ManyToOne
    @JoinColumn(name = "WORKSTATION_ID")
    private WorkStation workStationId;

    public String getTripState() {
        return tripState;
    }

    public void setTripState(String tripState) {
        this.tripState = tripState;
    }

    public WorkStation getWorkStationId() {
        return workStationId;
    }

    public void setWorkStationId(WorkStation workStationId) {
        this.workStationId = workStationId;
    }
}
