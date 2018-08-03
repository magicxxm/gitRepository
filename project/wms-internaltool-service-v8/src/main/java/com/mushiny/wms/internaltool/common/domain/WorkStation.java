package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name="MD_WORKSTATION")
public class WorkStation extends BaseWarehouseAssignedEntity {

    @Column(name="NAME")
    private String name;

    @Column(name="TYPE_ID")
    private String workStationType;

    @Column(name="OPERATOR_ID")
    private String operator;

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

    public String getStationName() {return stationName;}

    public void setStationName(String stationName) {this.stationName = stationName;}

    public String getWorkStationType() {
        return workStationType;
    }

    public void setWorkStationType(String workStationType) {
        this.workStationType = workStationType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public WorkStation() {
    }
}
