package com.mushiny.wms.outboundproblem.crud.dto;

import com.mushiny.wms.outboundproblem.business.dto.SolveShipmentPositionDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrintShipmentPositionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String shipmentNo;

    List<SolveShipmentPositionDTO> solveShipmentPositions=new ArrayList<>();

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public List<SolveShipmentPositionDTO> getSolveShipmentPositions() {
        return solveShipmentPositions;
    }

    public void setSolveShipmentPositions(List<SolveShipmentPositionDTO> solveShipmentPositions) {
        this.solveShipmentPositions = solveShipmentPositions;
    }
}
