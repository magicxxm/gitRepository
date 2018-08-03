package com.mushiny.wms.masterdata.obbasics.domain;


import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.ibbasics.domain.StockUnit;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.obbasics.constants.State;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "OB_PICKINGORDERPOSITION")
public class PickingPosition extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    public final static int PICKING_TYPE_DEFAULT = 0;
    public final static int PICKING_TYPE_PICK = 1;
    public final static int PICKING_TYPE_COMPLETE = 2;

    @ManyToOne(optional = true)
    @JoinColumn(name = "PICKINGORDER_ID")
    private PickingOrder pickingOrder;

    @Column(name = "PICKINGORDER_NO", nullable = false)
    private String pickingOrderNumber;

    @Column(name = "AMOUNT", nullable = false, precision = 17, scale = 4)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "AMOUNT_PICKED", nullable = false, precision = 17, scale = 4)
    private BigDecimal amountPicked = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PICKFROMSTOCKUNIT_ID")
    private StockUnit pickFromStockUnit;

    @Column(name = "PICKFROMLOCATION_NAME", nullable = false)
    private String pickFromLocationName;

    @Column(name = "PICKFROMUNITLOAD_LABEL", nullable = false)
    private String pickFromUnitLoadLabel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "PICKTOUNITLOAD_ID")
//    private PickingUnitLoad pickToUnitLoad;

    @Column(name = "STATE", nullable = false)
    private int state = State.RAW;

    @Column(name = "PICKING_TYPE", nullable = false)
    private int pickingType = PICKING_TYPE_DEFAULT;

    @ManyToOne
    @JoinColumn(name = "STRATEGY_ID")
    private OrderStrategy strategy;

    @ManyToOne
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "LOTPICKED_ID")
//    private Lot lotPicked;

    @Column(name = "PICK_INDEX", nullable = false)
    private int pickIndex = 0;

//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "CUSTOMERSHIPMENTPOSITION_ID")
//    private CustomerShipmentPosition customerShipmentPosition;

    public PickingOrder getPickingOrder() {
        return pickingOrder;
    }

    public void setPickingOrder(PickingOrder pickingOrder) {
        this.pickingOrder = pickingOrder;
    }

    public String getPickingOrderNumber() {
        return pickingOrderNumber;
    }

    public void setPickingOrderNumber(String pickingOrderNumber) {
        this.pickingOrderNumber = pickingOrderNumber;
    }

//    public BigDecimal getAmount() {
//        if( getItemData() != null ) {
//            return amount.setScale(getItemData().getScale());
//        }
//        return amount;
//    }
//
//    public void setAmount(BigDecimal amount) {
//        this.amount = amount;
//    }

//    public BigDecimal getAmountPicked() {
//        if( getItemData() != null ) {
//            return amountPicked.setScale(getItemData().getScale());
//        }
//        return amountPicked;
//    }

    public void setAmountPicked(BigDecimal amountPicked) {
        this.amountPicked = amountPicked;
    }

    public StockUnit getPickFromStockUnit() {
        return pickFromStockUnit;
    }

    public void setPickFromStockUnit(StockUnit pickFromStockUnit) {
        this.pickFromStockUnit = pickFromStockUnit;
    }

    public String getPickFromLocationName() {
        return pickFromLocationName;
    }

    public void setPickFromLocationName(String pickFromLocationName) {
        this.pickFromLocationName = pickFromLocationName;
    }

    public String getPickFromUnitLoadLabel() {
        return pickFromUnitLoadLabel;
    }

    public void setPickFromUnitLoadLabel(String pickFromUnitLoadLabel) {
        this.pickFromUnitLoadLabel = pickFromUnitLoadLabel;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

//    public PickingUnitLoad getPickToUnitLoad() {
//        return pickToUnitLoad;
//    }
//
//    public void setPickToUnitLoad(PickingUnitLoad pickToUnitLoad) {
//        this.pickToUnitLoad = pickToUnitLoad;
//    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPickingType() {
        return pickingType;
    }

    public void setPickingType(int pickingType) {
        this.pickingType = pickingType;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public OrderStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(OrderStrategy strategy) {
        this.strategy = strategy;
    }

//    public Lot getLotPicked() {
//        return lotPicked;
//    }
//
//    public void setLotPicked(Lot lotPicked) {
//        this.lotPicked = lotPicked;
//    }

    public int getPickIndex() {
        return pickIndex;
    }

    public void setPickIndex(int pickIndex) {
        this.pickIndex = pickIndex;
    }

//    public CustomerShipmentPosition getCustomerShipmentPosition() {
//        return customerShipmentPosition;
//    }
//
//    public void setCustomerShipmentPosition(CustomerShipmentPosition customerShipmentPosition) {
//        this.customerShipmentPosition = customerShipmentPosition;
//    }

//    @Transient
//    public String getUnit() {
//        return itemData == null ? null : itemData.getHandlingUnit().getName();
//    }

    @PrePersist
    @PreUpdate
    private void setRedundantValues() {
        if (pickingOrder == null) {
            pickingOrderNumber = null;
        } else {
            pickingOrderNumber = pickingOrder.getNumber();
        }
    }
}
