package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationPosition;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationPositionDTO;

import javax.validation.constraints.NotNull;

public class StockTakingStationPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String positionState;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workStationPositionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stationId;

    private WorkStationPositionDTO workStationPosition;

    private StockTakingStationDTO stockTakingStation;

    public StockTakingStationPositionDTO() {
    }

    public StockTakingStationPositionDTO(StockTakingStationPosition entity) {
        super(entity);
    }


    public String getPositionState() {
        return positionState;
    }

    public void setPositionState(String positionState) {
        this.positionState = positionState;
    }

    public String getWorkStationPositionId() {
        return workStationPositionId;
    }

    public void setWorkStationPositionId(String workStationPositionId) {
        this.workStationPositionId = workStationPositionId;
    }

    public WorkStationPositionDTO getWorkStationPosition() {
        return workStationPosition;
    }

    public void setWorkStationPosition(WorkStationPositionDTO workStationPosition) {
        this.workStationPosition = workStationPosition;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public StockTakingStationDTO getStockTakingStation() {
        return stockTakingStation;
    }

    public void setStockTakingStation(StockTakingStationDTO stockTakingStation) {
        this.stockTakingStation = stockTakingStation;
    }
}
