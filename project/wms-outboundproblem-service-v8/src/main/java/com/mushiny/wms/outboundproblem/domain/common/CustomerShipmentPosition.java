package com.mushiny.wms.outboundproblem.domain.common;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "OB_CUSTOMERSHIPMENTPOSITION")
public class CustomerShipmentPosition extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "ORDER_INDEX")
    private int orderIndex;

    @Column(name = "POSITION_NO")
    private int positionNo;

    @Column(name = "STATE")
    private int state;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SHIPMENT_ID")
    private CustomerShipment customerShipment;

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

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public CustomerShipment getCustomerShipment() {
        return customerShipment;
    }

    public void setCustomerShipment(CustomerShipment customerShipment) {
        this.customerShipment = customerShipment;
    }
}
