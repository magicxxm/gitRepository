package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ICQA_STOCKTAKINGSTATIONTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"})
})
public class StockTakingStationType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATIONTYPE", nullable = false)
    private String stationType;

    @OrderBy("positionIndex")
    @OneToMany(mappedBy = "stockTakingStationType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<StockTakingStationTypePosition> positions = new ArrayList<>();

    public void addPosition(StockTakingStationTypePosition position) {
        getPositions().add(position);
        position.setStockTakingStationType(this);
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

    public List<StockTakingStationTypePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<StockTakingStationTypePosition> positions) {
        this.positions = positions;
    }

    public String getStationType() {
        return stationType;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
    }
}
