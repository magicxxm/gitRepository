package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_PICKSTATIONTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"})
})
public class PickStationType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "IS_PRINT_FHD",nullable = false)
    private String isPrintFHD;
    @Column(name = "IS_PRINT_ZXD",nullable = false)
    private String isPrintZXD;
    @Column(name = "IS_PRINT_QD",nullable = false)
    private String isPrintQD;
    @ManyToOne
    @JoinColumn(name = "PICKPACKWALLTYPE_ID")
    private PickPackWallType pickPackWallType;
    @Column(name = "PICK_STATION_TYPE")
    private String pickStationType;
    @OrderBy("positionIndex")
    @OneToMany(mappedBy = "pickStationType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PickStationTypePosition> positions = new ArrayList<>();

    public void addPosition(PickStationTypePosition position) {
        getPositions().add(position);
        position.setPickStationType(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPickStationType() {
        return pickStationType;
    }

    public void setPickStationType(String pickStationType) {
        this.pickStationType = pickStationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PickPackWallType getPickPackWallType() {
        return pickPackWallType;
    }

    public void setPickPackWallType(PickPackWallType pickPackWallType) {
        this.pickPackWallType = pickPackWallType;
    }

    public List<PickStationTypePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PickStationTypePosition> positions) {
        this.positions = positions;
    }

    public String getIsPrintFHD() {
        return isPrintFHD;
    }

    public void setIsPrintFHD(String isPrintFHD) {
        this.isPrintFHD = isPrintFHD;
    }

    public String getIsPrintZXD() {
        return isPrintZXD;
    }

    public void setIsPrintZXD(String isPrintZXD) {
        this.isPrintZXD = isPrintZXD;
    }

    public String getIsPrintQD() {
        return isPrintQD;
    }

    public void setIsPrintQD(String isPrintQD) {
        this.isPrintQD = isPrintQD;
    }
}
