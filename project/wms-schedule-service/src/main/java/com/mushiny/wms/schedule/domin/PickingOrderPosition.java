package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/5/2.
 */
@Entity
@Table(name = "OB_PICKINGORDERPOSITION")
public class PickingOrderPosition extends BaseClientAssignedEntity {

    public final static int PICKING_TYPE_DEFAULT = 0;
    public final static int PICKING_TYPE_PICK = 1;
    public final static int PICKING_TYPE_COMPLETE = 2;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "AMOUNT_PICKED")
    private BigDecimal amountPicked = BigDecimal.ZERO;

    @Column(name = "PICKING_TYPE")
    private int pickingType = PICKING_TYPE_DEFAULT;

    @Column(name = "STATE")
    private int state;

    @ManyToOne
    @JoinColumn(name = "CUSTOMERSHIPMENTPOSITION_ID")
    private CustomerShipmentPosition customerShipmentPosition;

    @ManyToOne
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @ManyToOne
    @JoinColumn(name = "LOTPICKED_ID")
    private Lot lotPicked;

    @Column(name = "PICKFROMLOCATION_NAME")
    private String pickFromLocationName;

    @Column(name = "PICKFROMFACE_NAME")
    private String pickFromFaceName;

    @Column(name = "PICKFROMUNITLOAD_LABEL")
    private String pickFromUnitLoadLabel;

    @Column(name = "PICKINGORDER_NO")
    private String pickingOrderNo;

    @ManyToOne
    @JoinColumn(name = "PICKFROMSTOCKUNIT_ID")
    private StockUnit pickFromStockUnit;

    @ManyToOne
    @JoinColumn(name = "PICKTOUNITLOAD_ID")
    private PickingUnitLoad pickToUnitLoad;

    @ManyToOne
    @JoinColumn(name = "PICKINGORDER_ID")
    private PickingOrder pickingOrder;

    @ManyToOne
    @JoinColumn(name = "STRATEGY_ID")
    private OrderStrategy orderStrategy;

    @ManyToOne
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @Column(name = "PICK_INDEX")
    private int pickIndex = 0;

    @ManyToOne
    @JoinColumn(name = "POD_ID")
    private Pod pod;

    @ManyToOne
    @JoinColumn(name = "PICKSTATION_ID")
    private PickStation pickStation;

    @ManyToOne
    @JoinColumn(name = "PICKPACKCELL_ID")
    private PickPackCell pickPackCell;

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

    public CustomerShipmentPosition getCustomerShipmentPosition() {
        return customerShipmentPosition;
    }

    public void setCustomerShipmentPosition(CustomerShipmentPosition customerShipmentPosition) {
        this.customerShipmentPosition = customerShipmentPosition;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public Lot getLotPicked() {
        return lotPicked;
    }

    public void setLotPicked(Lot lotPicked) {
        this.lotPicked = lotPicked;
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

    public String getPickingOrderNo() {
        return pickingOrderNo;
    }

    public void setPickingOrderNo(String pickingOrderNo) {
        this.pickingOrderNo = pickingOrderNo;
    }

    public StockUnit getPickFromStockUnit() {
        return pickFromStockUnit;
    }

    public void setPickFromStockUnit(StockUnit pickFromStockUnit) {
        this.pickFromStockUnit = pickFromStockUnit;
    }

    public PickingUnitLoad getPickToUnitLoad() {
        return pickToUnitLoad;
    }

    public void setPickToUnitLoad(PickingUnitLoad pickToUnitLoad) {
        this.pickToUnitLoad = pickToUnitLoad;
    }

    public PickingOrder getPickingOrder() {
        return pickingOrder;
    }

    public void setPickingOrder(PickingOrder pickingOrder) {
        this.pickingOrder = pickingOrder;
    }

    public OrderStrategy getOrderStrategy() {
        return orderStrategy;
    }

    public void setOrderStrategy(OrderStrategy orderStrategy) {
        this.orderStrategy = orderStrategy;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public int getPickIndex() {
        return pickIndex;
    }

    public void setPickIndex(int pickIndex) {
        this.pickIndex = pickIndex;
    }

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    public PickStation getPickStation() {
        return pickStation;
    }

    public void setPickStation(PickStation pickStation) {
        this.pickStation = pickStation;
    }

    public PickPackCell getPickPackCell() {
        return pickPackCell;
    }

    public void setPickPackCell(PickPackCell pickPackCell) {
        this.pickPackCell = pickPackCell;
    }

    public String getPickFromFaceName() {
        return pickFromFaceName;
    }

    public void setPickFromFaceName(String pickFromFaceName) {
        this.pickFromFaceName = pickFromFaceName;
    }
}
