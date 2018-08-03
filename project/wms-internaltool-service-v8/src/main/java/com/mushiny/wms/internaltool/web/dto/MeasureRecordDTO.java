package com.mushiny.wms.internaltool.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MeasureRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime createDate;

    private String createBy;

    private String itemNo;

    private String sku;

    private String itemName;

    private String fromStorageLocation;

    private BigDecimal amount;

    private BigDecimal fromHeight;

    private BigDecimal fromWidth;

    private BigDecimal fromDepth;

    private BigDecimal fromWeight;

    private BigDecimal toHeight;

    private BigDecimal toWidth;

    private BigDecimal toDepth;

    private BigDecimal toWeight;

    private String clientName;

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getFromStorageLocation() {
        return fromStorageLocation;
    }

    public void setFromStorageLocation(String fromStorageLocation) {
        this.fromStorageLocation = fromStorageLocation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
