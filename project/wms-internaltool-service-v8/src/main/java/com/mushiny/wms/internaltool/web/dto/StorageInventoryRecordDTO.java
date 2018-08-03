package com.mushiny.wms.internaltool.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class StorageInventoryRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String storageLocationName;

    private String itemNo;

    private String sku;

    private BigDecimal amount;

    private String inventoryState;

    private String shipmentNo;

    private String itemUnitName;

    private String itemDataName;

    private String clientName;

    public String getStorageLocationName() {
        return storageLocationName;
    }

    public void setStorageLocationName(String storageLocationName) {
        this.storageLocationName = storageLocationName;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getInventoryState() {
        return inventoryState;
    }

    public void setInventoryState(String inventoryState) {
        this.inventoryState = inventoryState;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public String getItemUnitName() {
        return itemUnitName;
    }

    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName;
    }

    public String getItemDataName() {
        return itemDataName;
    }

    public void setItemDataName(String itemDataName) {
        this.itemDataName = itemDataName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
