package com.mushiny.wms.report.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "INV_STOCKUNIT")
public class StockUnit extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "RESERVED_AMOUNT")
    private BigDecimal reservedAmount;

    @Column(name = "STATE", nullable = false)
    private String state;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @ManyToOne
    @JoinColumn(name = "LOT_ID")
    private Lot lot;

    @ManyToOne
    @JoinColumn(name = "UNITLOAD_ID")
    private UnitLoad unitLoad;

//    @Column(name = "BATCH")
//    private String batch;
//
//    @Column(name = "LOT_NUMBER")
//    private String lotNumber;

//    public String getBatch() {
//        return batch;
//    }
//
//    public void setBatch(String batch) {
//        this.batch = batch;
//    }
//
//    public String getLotNumber() {
//        return lotNumber;
//    }
//
//    public void setLotNumber(String lotNumber) {
//        this.lotNumber = lotNumber;
//    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public UnitLoad getUnitLoad() {
        return unitLoad;
    }

    public void setUnitLoad(UnitLoad unitLoad) {
        this.unitLoad = unitLoad;
    }
}
