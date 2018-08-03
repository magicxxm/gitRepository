package com.mushiny.wms.masterdata.ibbasics.domain;


import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IB_STOWSTATIONTYPE")
public class StowStationType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OrderBy("positionIndex")
    @OneToMany(mappedBy = "stowStationType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<StowStationTypePosition> stowStationType = new ArrayList<>();

    public void addPosition(StowStationTypePosition stowStationTypePosition) {
        getStowStationType().add(stowStationTypePosition);
        stowStationTypePosition.setStowStationType(this);
    }

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

    public List<StowStationTypePosition> getStowStationType() {
        return stowStationType;
    }

    public void setStowStationType(List<StowStationTypePosition> stowStationType) {
        this.stowStationType = stowStationType;
    }
}
