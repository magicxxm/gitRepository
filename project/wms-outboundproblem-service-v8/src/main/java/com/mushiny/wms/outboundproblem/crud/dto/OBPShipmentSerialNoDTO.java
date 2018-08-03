package com.mushiny.wms.outboundproblem.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.outboundproblem.domain.OBPShipmentSerialNo;

public class OBPShipmentSerialNoDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String shipmentId;

    private String itemDataId;

    private String serialNo;

    private Boolean scaned;

    public OBPShipmentSerialNoDTO() {
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Boolean getScaned() {
        return scaned;
    }

    public void setScaned(Boolean scaned) {
        this.scaned = scaned;
    }
}
