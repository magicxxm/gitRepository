package com.mushiny.wms.outboundproblem.crud.common.dto;

import com.mushiny.wms.outboundproblem.crud.dto.OBPStationDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPStationTypePositionDTO;

import java.io.Serializable;
import java.util.List;

public class ScanningOBPStationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private OBPStationDTO obpStation;

    private long maxAmount;

    private List<OBPStationTypePositionDTO> obpStationTypePositions;

    public OBPStationDTO getObpStation() {
        return obpStation;
    }

    public void setObpStation(OBPStationDTO obpStation) {
        this.obpStation = obpStation;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(long maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<OBPStationTypePositionDTO> getObpStationTypePositions() {
        return obpStationTypePositions;
    }

    public void setObpStationTypePositions(List<OBPStationTypePositionDTO> obpStationTypePositions) {
        this.obpStationTypePositions = obpStationTypePositions;
    }
}
