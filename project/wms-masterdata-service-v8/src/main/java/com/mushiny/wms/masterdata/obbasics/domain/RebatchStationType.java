package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_REBATCHSTATIONTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"})
})
public class RebatchStationType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OrderBy("positionIndex")
    @OneToMany(mappedBy = "rebatchStationType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<RebatchStationTypePosition> positions = new ArrayList<>();

    public void addPosition(RebatchStationTypePosition position) {
        getPositions().add(position);
        position.setRebatchStationType(this);
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

    public List<RebatchStationTypePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<RebatchStationTypePosition> positions) {
        this.positions = positions;
    }
}
