package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;

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

//    @Column(name = "STRATEGY_DATE", nullable = false)
//    private LocalDate strategyDate;

    @Column(name = "STATE", nullable = false)
    private String state;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

//    @ManyToOne
//    @JoinColumn(name = "LOT_ID")
//    private Lot lot;

//    @ManyToOne
//    @JoinColumn(name = "STORAGELOCATION_ID")
//    private StorageLocation storageLocation;

//    @ManyToOne
//    @JoinColumn(name = "CONTAINER_ID")
//    private Container container;

    @ManyToOne
    @JoinColumn(name = "UNITLOAD_ID")
    private UnitLoad unitLoad;

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

//    public LocalDate getStrategyDate() {
//        return strategyDate;
//    }
//
//    public void setStrategyDate(LocalDate strategyDate) {
//        this.strategyDate = strategyDate;
//    }
//
//    public Lot getLot() {
//        return lot;
//    }
//
//    public void setLot(Lot lot) {
//        this.lot = lot;
//    }



//    public StorageLocation getStorageLocation() {
//        return storageLocation;
//    }
//
//    public void setStorageLocation(StorageLocation storageLocation) {
//        this.storageLocation = storageLocation;
//    }

//    public Container getContainer() {
//        return container;
//    }
//
//    public void setContainer(Container container) {
//        this.container = container;
//    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UnitLoad getUnitLoad() {
        return unitLoad;
    }

    public void setUnitLoad(UnitLoad unitLoad) {
        this.unitLoad = unitLoad;
    }
}
