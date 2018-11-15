package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name="MD_WORKSTATION")
public class WorkStation extends BaseWarehouseAssignedEntity {

    @Column(name="NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name="TYPE_ID")
    private WorkStationType workStationType;

    @ManyToOne
    @JoinColumn(name="OPERATOR_ID")
    private User operator;

    @Column(name="STATION_NAME")
    private String stationName;

    @Column(name = "ISCALLPOD")
    private boolean isCallPod;

    @Column(name = "SECTION_ID")
    private String sectionId;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public boolean isCallPod() {
        return isCallPod;
    }

    public void setCallPod(boolean callPod) {
        isCallPod = callPod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkStationType getWorkStationType() {
        return workStationType;
    }

    public void setWorkStationType(WorkStationType workStationType) {
        this.workStationType = workStationType;
    }

    public User getOperator() {return operator;}

    public void setOperator(User operator) {this.operator = operator;}

    public String getStationName() {return stationName;}

    public void setStationName(String stationName) {this.stationName = stationName;}

    public WorkStation() {
    }
}
