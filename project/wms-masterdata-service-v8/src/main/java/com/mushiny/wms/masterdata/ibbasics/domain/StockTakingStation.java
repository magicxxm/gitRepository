package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ICQA_STOCKTAKINGSTATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class StockTakingStation extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OPERATOR_ID")
    private User operatorId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private StockTakingStationType stockTakingStationType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATION_ID")
    private WorkStation workStation;

    @OrderBy("workStationPosition")
    @OneToMany(mappedBy = "stockTakingStation", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<StockTakingStationPosition> positions = new ArrayList<>();

    public void addPosition(StockTakingStationPosition position) {
        getPositions().add(position);
        position.setStockTakingStation(this);
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


    public User getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(User operatorId) {
        this.operatorId = operatorId;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

    public List<StockTakingStationPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<StockTakingStationPosition> positions) {
        this.positions = positions;
    }

    public StockTakingStationType getStockTakingStationType() {
        return stockTakingStationType;
    }

    public void setStockTakingStationType(StockTakingStationType stockTakingStationType) {
        this.stockTakingStationType = stockTakingStationType;
    }
}
