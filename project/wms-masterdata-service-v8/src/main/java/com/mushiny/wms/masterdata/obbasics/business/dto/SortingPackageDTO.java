package com.mushiny.wms.masterdata.obbasics.business.dto;

import java.io.Serializable;

public class SortingPackageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String containerName;

    private String sortCode;

    private String shipmentNo;

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }
}
