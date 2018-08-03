package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocation;
import com.mushiny.wms.masterdata.general.domain.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "IB_GOODSRECEIPTPOSITION")
public class GoodsReceiptPosition extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "ITEMDATA_ID")
    private String itemData;

    @Column(name = "LOT")
    private String lot;

    @Column(name = "RECEIPT_TYPE", nullable = false)
    private String receiptType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RECEIVETOSTOCKUNIT_ID")
    private StockUnit stockUnit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RECEIVESTORAGE_ID")
    private StorageLocation StorageLocation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RECEIVEUNITLOAD_ID")
    private UnitLoad unitLoad;

    @Column(name = "RECEIVESTATION_ID")
    private String stationId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "GOODSRECEIPT_ID")
    private GoodsReceipt goodsReceipt;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getItemData() {
        return itemData;
    }

    public void setItemData(String itemData) {
        this.itemData = itemData;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public GoodsReceipt getGoodsReceipt() {
        return goodsReceipt;
    }

    public void setGoodsReceipt(GoodsReceipt goodsReceipt) {
        this.goodsReceipt = goodsReceipt;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public StockUnit getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(StockUnit stockUnit) {
        this.stockUnit = stockUnit;
    }

    public StorageLocation getStorageLocation() {
        return StorageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        StorageLocation = storageLocation;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public UnitLoad getUnitLoad() {
        return unitLoad;
    }

    public void setUnitLoad(UnitLoad unitLoad) {
        this.unitLoad = unitLoad;
    }
}
