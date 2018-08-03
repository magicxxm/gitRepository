package com.mushiny.wms.internaltool.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class InventoryAttributesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sourceId;

    private String destinationId;

    private String itemDataId;

    private String inventoryState;

    private BigDecimal amount;

    private String adjustReason;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public String getInventoryState() {
        return inventoryState;
    }

    public void setInventoryState(String inventoryState) {
        this.inventoryState = inventoryState;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAdjustReason() {
        return adjustReason;
    }

    public void setAdjustReason(String adjustReason) {
        this.adjustReason = adjustReason;
    }
}
