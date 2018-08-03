package com.mushiny.wms.outboundproblem.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OBP_SHIPMENT_SERIALNO")
public class OBPShipmentSerialNo extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "SHIPMENT_ID")
    private String shipmentId;

    @Column(name = "ITEMDATA_ID")
    private String itemDataId;

    @Column(name = "SERIAL_NO")
    private String serialNo;

    @Column(name = "SCANED")
    private Boolean scaned;

    @Column(name="STATE")
    private String state;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
