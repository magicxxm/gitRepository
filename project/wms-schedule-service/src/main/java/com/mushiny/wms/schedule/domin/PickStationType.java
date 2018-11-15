package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/4/26.
 */
@Entity
@Table(name = "OB_PICKSTATIONTYPE")
public class PickStationType extends BaseWarehouseAssignedEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PICK_STATION_TYPE")
    private String pickStationType;

    @ManyToOne
    @JoinColumn(name = "PICKPACKWALLTYPE_ID")
    private PickPackWallType pickPackWallType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPickStationType() {
        return pickStationType;
    }

    public void setPickStationType(String pickStationType) {
        this.pickStationType = pickStationType;
    }

    public PickPackWallType getPickPackWallType() {
        return pickPackWallType;
    }

    public void setPickPackWallType(PickPackWallType pickPackWallType) {
        this.pickPackWallType = pickPackWallType;
    }
}
