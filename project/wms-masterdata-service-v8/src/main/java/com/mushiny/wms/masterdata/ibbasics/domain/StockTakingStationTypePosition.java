package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "ICQA_STOCKTAKINGSTATIONTYPEPOSITION")
public class StockTakingStationTypePosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_INDEX")
    private int positionIndex;

    @Column(name = "POSITION_STATE")
    private String positionState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STATIONTYPE_ID", nullable = false)
    private StockTakingStationType stockTakingStationType;

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public String getPositionState() {
        return positionState;
    }

    public void setPositionState(String positionState) {
        this.positionState = positionState;
    }

    public StockTakingStationType getStockTakingStationType() {
        return stockTakingStationType;
    }

    public void setStockTakingStationType(StockTakingStationType stockTakingStationType) {
        this.stockTakingStationType = stockTakingStationType;
    }
}
