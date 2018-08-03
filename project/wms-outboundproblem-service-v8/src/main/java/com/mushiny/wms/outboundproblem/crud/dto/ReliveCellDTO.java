package com.mushiny.wms.outboundproblem.crud.dto;


import java.io.Serializable;

public class ReliveCellDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String locationContainer;

    private String shipmentNo;

    private String solveKey;

    private String obpStationId;

    private String cellName;

    public ReliveCellDTO() {
    }

    public String getSolveKey() {
        return solveKey;
    }

    public void setSolveKey(String solveKey) {
        this.solveKey = solveKey;
    }

    public String getObpStationId() {
        return obpStationId;
    }

    public void setObpStationId(String obpStationId) {
        this.obpStationId = obpStationId;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public String getLocationContainer() {
        return locationContainer;
    }

    public void setLocationContainer(String locationContainer) {
        this.locationContainer = locationContainer;
    }
}
