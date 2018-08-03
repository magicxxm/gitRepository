package com.mushiny.wcs.application.domain;

import com.mushiny.wcs.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "RCS_TRIPPOSITION")
public class TripPosition extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_NO", nullable = false)
    private int positionNo;

    @Column(name = "TRIPPOSITION_STATE")
    private String tripState;

    @Column(name = "POD_USING_FACE")
    private String podUsingFace;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    @Column(name = "SECTION_ID")
    private String sectionId;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    @ManyToOne
    @JoinColumn(name = "TRIP_ID")
    private Trip trip;

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public String getTripState() {
        return tripState;
    }

    public void setTripState(String tripState) {
        this.tripState = tripState;
    }


    public String getPodUsingFace() {
        return podUsingFace;
    }

    public void setPodUsingFace(String podUsingFace) {
        this.podUsingFace = podUsingFace;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
