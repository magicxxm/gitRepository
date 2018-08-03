package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MD_TURNAREA")
public class TurnArea extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATION_ID")
    private String station;

    @Column(name = "MAP_ID")
    private String map;

    @Column(name = "SECTION_ID")
    private String section;

    @OrderBy("trurnArea")
    @OneToMany(mappedBy = "trurnArea", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<TurnAreaPosition> positions = new ArrayList<>();

    public void addPosition(TurnAreaPosition position) {
        getPositions().add(position);
        position.setTrurnArea(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<TurnAreaPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<TurnAreaPosition> positions) {
        this.positions = positions;
    }
}
