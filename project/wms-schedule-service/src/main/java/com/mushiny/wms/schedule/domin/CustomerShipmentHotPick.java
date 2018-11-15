package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="OB_CUSTOMERSHIPMENT_HOTPICK")
public class CustomerShipmentHotPick extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name="SHIPMENT_ID")
    private String ShipmentId;

    @Column(name="ITEMDATA_ID")
    private String ItemDataId;

    @Column(name="AMOUNT")
    private BigDecimal amount;

    @Column(name="STATE")
    private int state;

    public CustomerShipmentHotPick() {
    }

    public String getShipmentId() {
        return ShipmentId;
    }

    public void setShipmentId(String shipmentId) {
        ShipmentId = shipmentId;
    }

    public String getItemDataId() {
        return ItemDataId;
    }

    public void setItemDataId(String itemDataId) {
        ItemDataId = itemDataId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
