package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_SORTINGSTATIONTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"})
})
public class SortingStationType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OrderBy("positionIndex")
    @OneToMany(mappedBy = "sortingStationType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<SortingStationTypePosition> positions = new ArrayList<>();

    public void addPosition(SortingStationTypePosition position) {
        getPositions().add(position);
        position.setSortingStationType(this);
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

    public List<SortingStationTypePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<SortingStationTypePosition> positions) {
        this.positions = positions;
    }
}
