package com.mushiny.wms.schedule.domin;

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
    private BigDecimal reservedAmount = BigDecimal.ZERO;

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

    @Column(name="SERIAL_NO")
    private String serialNo;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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

    @Transient
    public void releaseReservedAmount(BigDecimal amount1){
        // check constraint
        if (amount.compareTo(new BigDecimal(0)) < 0) {
            // throw new Exception();
        }

        BigDecimal reservedAmountNew = this.reservedAmount.subtract(amount);
        if (reservedAmountNew.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("------- LE " + getId() + " : want to release less than reserved items" + amount + " but was " + reservedAmount);
            reservedAmountNew = BigDecimal.ZERO;
        }

        this.reservedAmount = reservedAmountNew;
    }

    @Transient
    public void addReservedAmount(BigDecimal amount) {
        // check constraint
        if (amount.compareTo(new BigDecimal(0)) < 0) {
//            throw new FacadeException("cannot reserve negative amount");
        } else if (this.reservedAmount.add(amount).compareTo(this.amount) > 0) {
//            throw new FacadeException("cannot reserve more items than available (item=" + getItemData().getNumber() + ", on stock=" + this.amount + ", already reserved=" + this.reservedAmount + ", new reservation=" + amount + ")");
        }

        System.out.println("reserve add=" + amount);
        this.reservedAmount = this.reservedAmount.add(amount);
    }

}
