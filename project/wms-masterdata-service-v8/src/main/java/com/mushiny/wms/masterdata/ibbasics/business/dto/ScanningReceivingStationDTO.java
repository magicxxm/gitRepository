package com.mushiny.wms.masterdata.ibbasics.business.dto;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationTypePositionDTO;

import java.io.Serializable;
import java.util.List;

public class ScanningReceivingStationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private ReceiveStationDTO receivingStation;

    private long maxAmount;

    private List<ReceiveStationTypePositionDTO> receivingStationTypePositions;

    public List<ReceiveStationTypePositionDTO> getReceivingStationTypePositions() {
        return receivingStationTypePositions;
    }

    public void setReceivingStationTypePositions(List<ReceiveStationTypePositionDTO> receivingStationTypePositions) {
        this.receivingStationTypePositions = receivingStationTypePositions;
    }

    public ReceiveStationDTO getReceivingStation() {
        return receivingStation;
    }

    public void setReceivingStation(ReceiveStationDTO receivingStation) {
        this.receivingStation = receivingStation;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(long maxAmount) {
        this.maxAmount = maxAmount;
    }
}
