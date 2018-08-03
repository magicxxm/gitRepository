package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IBP_IBPSTATIONTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"})
})
public class IBPStationType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OrderBy("positionIndex")
    @OneToMany(mappedBy = "ibpStationType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<IBPStationTypePosition> positions = new ArrayList<>();

    public void addPosition(IBPStationTypePosition position) {
        getPositions().add(position);
        position.setIbpStationType(this);
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

    public List<IBPStationTypePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<IBPStationTypePosition> positions) {
        this.positions = positions;
    }
}
