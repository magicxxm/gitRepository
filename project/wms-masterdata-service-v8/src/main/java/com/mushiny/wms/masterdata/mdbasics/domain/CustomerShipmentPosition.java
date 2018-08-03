package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Laptop-11 on 2017/12/27.
 */
@Entity
@Table(name = "OB_CUSTOMERSHIPMENTPOSITION")
public class CustomerShipmentPosition extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;
    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "AMOUNT_PICKED")
    private BigDecimal amountPicked = BigDecimal.ZERO;

    @Column(name = "AMOUNT_REBINED")
    private BigDecimal amountRebined = BigDecimal.ZERO;

    @Column(name = "ORDER_INDEX")
    private int orderIndex;

    @Column(name = "POSITION_NO")
    private int positionNo;

    @Column(name = "STATE")
    private int state;

    @Column(name = "ITEMDATA_ID")
    private String itemDataId;

    @Column(name = "SHIPMENT_ID")
    private String customerShipmentId;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountPicked() {
        return amountPicked;
    }

    public void setAmountPicked(BigDecimal amountPicked) {
        this.amountPicked = amountPicked;
    }

    public BigDecimal getAmountRebined() {
        return amountRebined;
    }

    public void setAmountRebined(BigDecimal amountRebined) {
        this.amountRebined = amountRebined;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public String getCustomerShipmentId() {
        return customerShipmentId;
    }

    public void setCustomerShipmentId(String customerShipmentId) {
        this.customerShipmentId = customerShipmentId;
    }
}
