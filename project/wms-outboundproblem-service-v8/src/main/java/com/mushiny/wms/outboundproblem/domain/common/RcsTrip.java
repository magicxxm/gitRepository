package com.mushiny.wms.outboundproblem.domain.common;

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

    @Column(name = "TRIP_TYPE")
    private String tripType;

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

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
