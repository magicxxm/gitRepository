package com.mushiny.wms.outboundproblem.crud.dto;

import java.io.Serializable;
import java.math.BigDecimal;

//上架货位记录
public class StowStockunitRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String storageLocationId;

    //问题商品的数量
    private BigDecimal amount = BigDecimal.ZERO;

    //检查完商品数量
    private BigDecimal actualAmount = BigDecimal.ZERO;

    //货位商品总数量
    private BigDecimal totalAmount = BigDecimal.ZERO;

    private String clientId;

    private String clientName;

    private String lotId;

    private String itemDataId;

    private String unexamined;

    public StowStockunitRecordDTO() {

    }

    @Override
    public String toString() {
        return "StowStockunitRecordDTO{" +
                "name='" + name + '\'' +
                ", storageLocationId='" + storageLocationId + '\'' +
                ", amount=" + amount +
                ", actualAmount=" + actualAmount +
                ", totalAmount=" + totalAmount +
                ", clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", lotId='" + lotId + '\'' +
                ", itemDataId='" + itemDataId + '\'' +
                '}';
    }

    public String getUnexamined() {
        return unexamined;
    }

    public void setUnexamined(String unexamined) {
        this.unexamined = unexamined;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(String storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }
}