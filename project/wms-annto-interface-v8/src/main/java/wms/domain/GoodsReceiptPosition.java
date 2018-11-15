package wms.domain;

import wms.common.entity.BaseClientAssignedEntity;
import wms.domain.common.StockUnit;
import wms.domain.common.StorageLocation;
import wms.domain.common.UnitLoad;
import wms.domain.common.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "IB_GOODSRECEIPTPOSITION")
public class GoodsReceiptPosition extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "ITEMDATA_ID")
    private String itemData;

    @Column(name = "STATE")
    private String state;

    @Column(name = "LOT")
    private String lot;

    @Column(name = "PRODUCT_DATE")
    private Date productDate;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
