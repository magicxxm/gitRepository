package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;

import javax.persistence.*;

@Entity
@Table(name = "ICQA_STOCKTAKINGSTATIONPOSITION")
public class StockTakingStationPosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATIONPOSITION_ID", nullable = false)
    private WorkStationPosition workStationPosition;

    @Column(name = "POSITION_STATE", nullable = false)
    private String positionState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STATION_ID", nullable = false)
    private StockTakingStation stockTakingStation;


    public WorkStationPosition getWorkStationPosition() {
        return workStationPosition;
    }

    public void setWorkStationPosition(WorkStationPosition workStationPosition) {
        this.workStationPosition = workStationPosition;
    }

    public String getPositionState() {
        return positionState;
    }

    public void setPositionState(String positionState) {
        this.positionState = positionState;
    }

    public StockTakingStation getStockTakingStation() {
        return stockTakingStation;
    }

    public void setStockTakingStation(StockTakingStation stockTakingStation) {
        this.stockTakingStation = stockTakingStation;
    }
}
