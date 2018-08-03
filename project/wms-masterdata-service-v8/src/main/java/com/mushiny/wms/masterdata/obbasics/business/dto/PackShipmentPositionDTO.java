package com.mushiny.wms.masterdata.obbasics.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PackShipmentPositionDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    private String shipmentPositionId;

    private String itemDataId;

    private String packingStationId;

    public String getShipmentPositionId() {
        return shipmentPositionId;
    }

    public void setShipmentPositionId(String shipmentPositionId) {
        this.shipmentPositionId = shipmentPositionId;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public String getPackingStationId() {
        return packingStationId;
    }

    public void setPackingStationId(String packingStationId) {
        this.packingStationId = packingStationId;
    }
}
