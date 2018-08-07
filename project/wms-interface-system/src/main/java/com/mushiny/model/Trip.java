package com.mushiny.model;

import com.mushiny.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2018/3/27.
 */
@Entity
@Table(name = "RCS_TRIP")
public class Trip extends BaseEntity {

    @Column(name = "TRIP_TYPE")
    private String tripType;

    @Column(name = "TRIP_STATE")
    private String tripState = "New";

    @Column(name = "DRIVE_ID")
    private String driveId;

    @Column(name = "ACTIVED_BY")
    private String activedBy;

    @Column(name = "POD_ID")
    private String podId;

    @Column(name = "WORKSTATION_ID")
    private String workStationId;

    @Column(name = "CHARGER_ID")
    private String chargerId;

    @Column(name = "END_ADDRESS")
    private String endAddress;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    @Column(name = "SECTION_ID")
    private String sectionId;

    @Column(name = "PODSCANPATH")
    private String podscanpath;

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

    public String getActivedBy() {
        return activedBy;
    }

    public void setActivedBy(String activedBy) {
        this.activedBy = activedBy;
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

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getPodscanpath() {
        return podscanpath;
    }

    public void setPodscanpath(String podscanpath) {
        this.podscanpath = podscanpath;
    }
}
