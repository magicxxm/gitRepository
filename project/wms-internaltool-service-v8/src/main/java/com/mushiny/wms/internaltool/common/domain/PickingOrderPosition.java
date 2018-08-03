package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "OB_PICKINGORDERPOSITION")
public class PickingOrderPosition extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "AMOUNT_PICKED", nullable = false)
    private BigDecimal amountPicked;

    @Column(name = "PICKING_TYPE", nullable = false)
    private int pickingType;

    @Column(name = "STATE", nullable = false)
    private int state;

    @Column(name = "PICKFROMLOCATION_NAME", nullable = false)
    private String pickFromLocationName;

    @Column(name = "PICKFROMUNITLOAD_LABEL", nullable = false)
    private String pickFromUnitLoadLabel;

    @Column(name = "PICKINGORDER_NO", nullable = false)
    private String pickingOrderNo;

    @ManyToOne
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @ManyToOne
    @JoinColumn(name = "LOTPICKED_ID")
    private Lot lot;

    @ManyToOne
    @JoinColumn(name = "PICKFROMSTOCKUNIT_ID")
    private StockUnit stockUnit;

    @ManyToOne
    @JoinColumn(name = "PICKINGORDER_ID")
    private PickingOrder pickingOrder;

    @ManyToOne
    @JoinColumn(name = "CUSTOMERSHIPMENTPOSITION_ID")
    private CustomerShipmentPosition customerShipmentPosition;

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

    public int getPickingType() {
        return pickingType;
    }

    public void setPickingType(int pickingType) {
        this.pickingType = pickingType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPickFromLocationName() {
        return pickFromLocationName;
    }

    public void setPickFromLocationName(String pickFromLocationName) {
        this.pickFromLocationName = pickFromLocationName;
    }

    public CustomerShipmentPosition getCustomerShipmentPosition() {
        return customerShipmentPosition;
    }

    public void setCustomerShipmentPosition(CustomerShipmentPosition customerShipmentPosition) {
        this.customerShipmentPosition = customerShipmentPosition;
    }

    public String getPickFromUnitLoadLabel() {
        return pickFromUnitLoadLabel;
    }

    public void setPickFromUnitLoadLabel(String pickFromUnitLoadLabel) {
        this.pickFromUnitLoadLabel = pickFromUnitLoadLabel;
    }

    public String getPickingOrderNo() {
        return pickingOrderNo;
    }

    public void setPickingOrderNo(String pickingOrderNo) {
        this.pickingOrderNo = pickingOrderNo;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public StockUnit getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnit stockUnit) {
        this.stockUnit = stockUnit;
    }

    public PickingOrder getPickingOrder() {
        return pickingOrder;
    }

    public void setPickingOrder(PickingOrder pickingOrder) {
        this.pickingOrder = pickingOrder;
    }
}
