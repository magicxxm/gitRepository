package com.mushiny.model;


import com.mushiny.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by 123 on 2017/5/15.
 */
@Entity
@Table(name = "OB_CUSTOMERORDERPOSITION")
public class CustomerOrderPosition extends BaseClientAssignedEntity {

    @Column(name = "LINE_NO")
    private String lineNo;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "AMOUNT_PICKED")
    private BigDecimal amountPicked = BigDecimal.ZERO;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int orderIndex;

    @Column(name = "POSITION_NO", nullable = false)
    private int positionNo;

    @Column(name = "STATE", nullable = false)
    private int state;

    @Column(name = "ITEMDATA_ID", nullable = false)
    private String itemDataId;

    /*@ManyToOne
    @JoinColumn(name = "ITEMDATA_ID", nullable = false)
    private ItemData itemData;*/

    @Column(name = "LOT_DATE")
    private String endDate;

    @Column(name = "STOCK_STATE")
    private String stockState;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private CustomerOrder customerOrder;


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

   /* public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }*/

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getAmountPicked() {
        return amountPicked;
    }

    public void setAmountPicked(BigDecimal amountPicked) {
        this.amountPicked = amountPicked;
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

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }
}
