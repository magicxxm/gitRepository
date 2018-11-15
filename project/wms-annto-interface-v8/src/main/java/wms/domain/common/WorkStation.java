package wms.domain.common;


import wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/4/26.
 */
@Entity
@Table(name="MD_WORKSTATION")
public class WorkStation extends BaseWarehouseAssignedEntity {
    @Column(name="NAME")
    private String name;

//    @ManyToOne
//    @JoinColumn(name="TYPE_ID")
//    private WorkStationType workStationType;

    @Column(name="PICKPACKWALL_ID")
    private String pickPackWallId;

    @Column(name="FIXED_SCANNER")
    private boolean fixedScanner;

    @Column(name="ISCALLPOD")
    private boolean isCallPod;

    @Column(name = "OPERATOR_ID")
    private String operatorId;

    @Column(name = "STATION_NAME")
    private String stationName;

    @Column(name = "SECTION_ID")
    private String sectionId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public WorkStationType getWorkStationType() {
//        return workStationType;
//    }
//
//    public void setWorkStationType(WorkStationType workStationType) {
//        this.workStationType = workStationType;
//    }

    public String getPickPackWallId() {
        return pickPackWallId;
    }

    public void setPickPackWallId(String pickPackWallId) {
        this.pickPackWallId = pickPackWallId;
    }

    public boolean isFixedScanner() {
        return fixedScanner;
    }

    public void setFixedScanner(boolean fixedScanner) {
        this.fixedScanner = fixedScanner;
    }

    public boolean isCallPod() {
        return isCallPod;
    }

    public void setCallPod(boolean callPod) {
        isCallPod = callPod;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
