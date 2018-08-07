package com.mushiny.model;

import com.mushiny.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2018/3/27.
 */
@Entity
@Table(name = "RCS_TRIPPOSITION")
public class TripPosition extends BaseEntity {

    @Column(name = "TRIP_ID")
    private String tripId;

    @Column(name = "TRIPPOSITION_STATE")
    private String trippositionState = "Available";

    @Column(name = "POSITION_NO")
    private int positionNo;

    @Column(name = "POD_USING_FACE")
    private String usePodFace;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    @Column(name = "SECTION_ID")
    private String sectionId;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTrippositionState() {
        return trippositionState;
    }

    public void setTrippositionState(String trippositionState) {
        this.trippositionState = trippositionState;
    }

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public String getUsePodFace() {
        return usePodFace;
    }

    public void setUsePodFace(String usePodFace) {
        this.usePodFace = usePodFace;
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
}
