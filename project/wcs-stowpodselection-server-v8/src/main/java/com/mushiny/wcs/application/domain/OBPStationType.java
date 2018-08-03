package com.mushiny.wcs.application.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OBP_OBPSTATIONTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class OBPStationType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OrderBy("positionIndex")
    @OneToMany(mappedBy = "obpStationType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<OBPStationTypePosition> positions = new ArrayList<>();

    public void addPosition(OBPStationTypePosition position) {
        getPositions().add(position);
        position.setObpStationType(this);
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

    public List<OBPStationTypePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<OBPStationTypePosition> positions) {
        this.positions = positions;
    }
}
