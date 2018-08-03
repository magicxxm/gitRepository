package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationTypePosition;

public class StockTakingStationTypePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private int positionIndex;

    private String positionState;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stationTypeId;

    private StockTakingStationTypeDTO stockTakingStationType;

    public StockTakingStationTypePositionDTO() {
    }

    public StockTakingStationTypePositionDTO(StockTakingStationTypePosition entity) {
        super(entity);
    }


    public String getPositionState() {
        return positionState;
    }

    public void setPositionState(String positionState) {
        this.positionState = positionState;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public String getStationTypeId() {
        return stationTypeId;
    }

    public void setStationTypeId(String stationTypeId) {
        this.stationTypeId = stationTypeId;
    }

    public StockTakingStationTypeDTO getStockTakingStationType() {
        return stockTakingStationType;
    }

    public void setStockTakingStationType(StockTakingStationTypeDTO stockTakingStationType) {
        this.stockTakingStationType = stockTakingStationType;
    }
}
