package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="RCS_TRIP")
public class RcsTrip extends BaseEntity {

    @Column(name="TRIP_TYPE")
    private String tripType;

    @Column(name="TRIP_STATE")
    private String tripState;

    @Column(name="DRIVE_ID")
    private String driveId;

    @ManyToOne
    @JoinColumn(name = "POD_ID")
    private Pod pod;

    @ManyToOne
    @JoinColumn(name = "SECTION_ID")
    private Section section;

    @ManyToOne
    @JoinColumn(name="WORKSTATION_ID")
    private WorkStation workStation;

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

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }
}
