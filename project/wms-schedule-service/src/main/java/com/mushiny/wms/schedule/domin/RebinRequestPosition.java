package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "OB_REBINREQUESTPOSITION")
public class RebinRequestPosition extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT", nullable = false, precision = 17, scale = 4)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "AMOUNT_REBINED", nullable = false, precision = 17, scale = 4)
    private BigDecimal amountRebined = BigDecimal.ZERO;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "REBINFROMSTOCKUNIT_ID")
    private StockUnit rebinFromStockUnit;

    @Column(name = "REBINFROMCONTAINER_NAME", nullable = false)
    private String rebinFromContainerName;

    @Column(name = "REBINFROMUNITLOAD_LABEL", nullable = false)
    private String rebinFromUnitLoadLabel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REBINTOUNITLOAD_ID")
    private RebinUnitLoad rebinToUnitLoad;

    @Column(name = "STATE", nullable = false)
//    @Enumerated(EnumType.STRING)
    private String rebinState;// = RebinRequestPositionState.RAW;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "LOT_ID")
    private Lot lot;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMERSHIPMENTPOSITION_ID")
    private CustomerShipmentPosition customerShipmentPosition;

    @ManyToOne(optional = true)
    @JoinColumn(name = "REBINWALL_ID")
    private RebinWall rebinWall;

    @Column(name = "REBINWALL_INDEX", nullable = false)
    private int rebinWallIndex = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBINREQUEST_ID")
    private RebinRequest rebinRequest;

    @Column(name = "REBINTOCELL_NAME")
    private String rebinToCellName;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountRebined() {
        return amountRebined;
    }

    public void setAmountRebined(BigDecimal amountRebined) {
        this.amountRebined = amountRebined;
    }

    public StockUnit getRebinFromStockUnit() {
        return rebinFromStockUnit;
    }

    public void setRebinFromStockUnit(StockUnit rebinFromStockUnit) {
        this.rebinFromStockUnit = rebinFromStockUnit;
    }

    public String getRebinFromContainerName() {
        return rebinFromContainerName;
    }

    public void setRebinFromContainerName(String rebinFromContainerName) {
        this.rebinFromContainerName = rebinFromContainerName;
    }

    public String getRebinFromUnitLoadLabel() {
        return rebinFromUnitLoadLabel;
    }

    public void setRebinFromUnitLoadLabel(String rebinFromUnitLoadLabel) {
        this.rebinFromUnitLoadLabel = rebinFromUnitLoadLabel;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public RebinUnitLoad getRebinToUnitLoad() {
        return rebinToUnitLoad;
    }

    public void setRebinToUnitLoad(RebinUnitLoad rebinToUnitLoad) {
        this.rebinToUnitLoad = rebinToUnitLoad;
    }

    public String getRebinState() {
        return rebinState;
    }

    public void setRebinState(String rebinState) {
        this.rebinState = rebinState;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public CustomerShipmentPosition getCustomerShipmentPosition() {
        return customerShipmentPosition;
    }

    public void setCustomerShipmentPosition(CustomerShipmentPosition customerShipmentPosition) {
        this.customerShipmentPosition = customerShipmentPosition;
    }

    public RebinWall getRebinWall() {
        return rebinWall;
    }

    public void setRebinWall(RebinWall rebinWall) {
        this.rebinWall = rebinWall;
    }

    public int getRebinWallIndex() {
        return rebinWallIndex;
    }

    public void setRebinWallIndex(int rebinWallIndex) {
        this.rebinWallIndex = rebinWallIndex;
    }

    public RebinRequest getRebinRequest() {
        return rebinRequest;
    }

    public void setRebinRequest(RebinRequest rebinRequest) {
        this.rebinRequest = rebinRequest;
    }

    public String getRebinToCellName() {
        return rebinToCellName;
    }

    public void setRebinToCellName(String rebinToCellName) {
        this.rebinToCellName = rebinToCellName;
    }
}
