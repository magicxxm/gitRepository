package com.mushiny.wms.internaltool.web.dto;

import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.Lot;
import com.mushiny.wms.internaltool.common.domain.StockUnit;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemDataAmountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal amount = BigDecimal.ZERO;

    private Lot lot;

    private String inventoryAttributes;

    private ItemData itemData;

    public ItemDataAmountDTO() {

    }

    public ItemDataAmountDTO(StockUnit stockUnit) {
        this.lot = stockUnit.getLot();
        this.inventoryAttributes = stockUnit.getState();
        this.itemData = stockUnit.getItemData();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public String getInventoryAttributes() {
        return inventoryAttributes;
    }

    public void setInventoryAttributes(String inventoryAttributes) {
        this.inventoryAttributes = inventoryAttributes;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }
}
