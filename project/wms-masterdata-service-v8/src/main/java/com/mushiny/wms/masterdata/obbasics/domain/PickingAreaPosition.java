package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.Zone;

import javax.persistence.*;

@Entity
@Table(name = "OB_PICKINGAREAPOSITION")
public class PickingAreaPosition extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_NO", nullable = false)
    private String positionNo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PICKINGAREA_ID")
    private PickingArea pickingArea;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ZONE_ID")
    private Zone zone;

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public PickingArea getPickingArea() {
        return pickingArea;
    }

    public void setPickingArea(PickingArea pickingArea) {
        this.pickingArea = pickingArea;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }
}
