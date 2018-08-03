package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "INV_MEASURERECORD")
public class MeasureRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "ITEMDATA_ITEMNO")
    private String itemNo;

    @Column(name = "ITEMDATA_SKU")
    private String sku;

    @Column(name = "ITEMDATA_NAME")
    private String itemDateName;

    @Column(name = "FROM_STORAGELOCATION")
    private String fromStorageLocation;

    @Column(name = "FROM_HEIGHT")
    private BigDecimal fromHeight;

    @Column(name = "FROM_WIDTH")
    private BigDecimal fromWidth;

    @Column(name = "FROM_DEPTH")
    private BigDecimal fromDepth;

    @Column(name = "FROM_WEIGHT")
    private BigDecimal fromWeight;

    @Column(name = "TO_HEIGHT")
    private BigDecimal toHeight;

    @Column(name = "TO_WIDTH")
    private BigDecimal toWidth;

    @Column(name = "TO_DEPTH")
    private BigDecimal toDepth;

    @Column(name = "TO_WEIGHT")
    private BigDecimal toWeight;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "WAREHOUSE_ID")
    private Warehouse warehouse;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemDateName() {
        return itemDateName;
    }

    public void setItemDateName(String itemDateName) {
        this.itemDateName = itemDateName;
    }

    public String getFromStorageLocation() {
        return fromStorageLocation;
    }

    public void setFromStorageLocation(String fromStorageLocation) {
        this.fromStorageLocation = fromStorageLocation;
    }

    public BigDecimal getFromHeight() {
        return fromHeight;
    }

    public void setFromHeight(BigDecimal fromHeight) {
        this.fromHeight = fromHeight;
    }

    public BigDecimal getFromWidth() {
        return fromWidth;
    }

    public void setFromWidth(BigDecimal fromWidth) {
        this.fromWidth = fromWidth;
    }

    public BigDecimal getFromDepth() {
        return fromDepth;
    }

    public void setFromDepth(BigDecimal fromDepth) {
        this.fromDepth = fromDepth;
    }

    public BigDecimal getFromWeight() {
        return fromWeight;
    }

    public void setFromWeight(BigDecimal fromWeight) {
        this.fromWeight = fromWeight;
    }

    public BigDecimal getToHeight() {
        return toHeight;
    }

    public void setToHeight(BigDecimal toHeight) {
        this.toHeight = toHeight;
    }

    public BigDecimal getToWidth() {
        return toWidth;
    }

    public void setToWidth(BigDecimal toWidth) {
        this.toWidth = toWidth;
    }

    public BigDecimal getToDepth() {
        return toDepth;
    }

    public void setToDepth(BigDecimal toDepth) {
        this.toDepth = toDepth;
    }

    public BigDecimal getToWeight() {
        return toWeight;
    }

    public void setToWeight(BigDecimal toWeight) {
        this.toWeight = toWeight;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
