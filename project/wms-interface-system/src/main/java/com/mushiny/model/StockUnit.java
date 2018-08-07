package com.mushiny.model;


import com.mushiny.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "INV_STOCKUNIT")
public class StockUnit extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @Column(name = "AMOUNT", nullable = false, precision = 17, scale = 4)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "RESERVED_AMOUNT", precision = 17, scale = 4)
    private BigDecimal reservedAmount = BigDecimal.ZERO;

    @ManyToOne(optional = false)
    @JoinColumn(name = "UNITLOAD_ID")
    private UnitLoad unitLoad;

    @ManyToOne(optional = true)
    @JoinColumn(name = "LOT_ID")
    private Lot lot;

    @Column(name = "BATCH_NO")
    private String batchOrder;

    @Column(name = "SERIAL_NO")
    private String serialNumber;

    @Column(name = "STATE")
    private String state;

    @Transient
    public BigDecimal getAvailableAmount() {
        return amount.subtract(reservedAmount);
    }

    @Transient
    public void addReservedAmount(BigDecimal amount) {
        this.reservedAmount = this.reservedAmount.add(amount);
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

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

    public UnitLoad getUnitLoad() {
        return unitLoad;
    }

    public void setUnitLoad(UnitLoad unitLoad) {
        this.unitLoad = unitLoad;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public String getBatchOrder() {
        return batchOrder;
    }

    public void setBatchOrder(String batchOrder) {
        this.batchOrder = batchOrder;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
