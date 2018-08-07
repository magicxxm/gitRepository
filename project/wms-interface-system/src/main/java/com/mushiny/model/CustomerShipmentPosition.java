package com.mushiny.model;


import com.mushiny.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by 123 on 2017/5/2.
 */
@Entity
@Table(name = "OB_CUSTOMERSHIPMENTPOSITION")
public class CustomerShipmentPosition extends BaseClientAssignedEntity {

    @Column(name = "LINE_NO")
    private String lineNo;

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

    @Column(name = "LOT_DATE")
    private String endDate;

    @Column(name = "STOCK_STATE")
    private String stockState;

    @ManyToOne
    @JoinColumn(name = "SHIPMENT_ID")
    private CustomerShipment customerShipment;

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStockState() {
        return stockState;
    }

    public void setStockState(String stockState) {
        this.stockState = stockState;
    }

    public CustomerShipment getCustomerShipment() {
        return customerShipment;
    }

    public void setCustomerShipment(CustomerShipment customerShipment) {
        this.customerShipment = customerShipment;
    }
}
