package com.mushiny.wcs.application.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mushiny.wcs.common.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RCS_TRIP")
public class Trip extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "TRIP_TYPE", nullable = false)
    private String tripType;

    @Column(name = "TRIP_STATE", nullable = false)
    private String tripState;

    @Column(name = "DRIVE_ID")
    private String driveId;

    @Column(name = "POD_ID")
    private String podId;
    @Column(name = "PODSCANPATH")
    private String podScanpath;

    public String getPodScanpath() {
        return podScanpath;
    }

    public void setPodScanpath(String podScanpath) {
        this.podScanpath = podScanpath;
    }

    @Column(name = "WORKSTATION_ID")
    private String workStationId;

    @Column(name = "CHARGER_ID")
    private String chargerId;
    @Column(name = "END_ADDRESS")
    private Integer endAddress;

    public Integer getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(Integer endAddress) {
        this.endAddress = endAddress;
    }

    @Column(name = "SECTION_ID")
    private String sectionId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    @OneToMany(mappedBy = "trip", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<TripPosition> positions = new ArrayList<>();

    public void addPosition(TripPosition position) {
        getPositions().add(position);
        position.setTrip(this);
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

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

    public String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public String getWorkStationId() {
        return workStationId;
    }

    public void setWorkStationId(String workStationId) {
        this.workStationId = workStationId;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    @JsonBackReference
    public List<TripPosition> getPositions() {
        return positions;
    }

    @JsonBackReference
    public void setPositions(List<TripPosition> positions) {
        this.positions = positions;
    }

}
