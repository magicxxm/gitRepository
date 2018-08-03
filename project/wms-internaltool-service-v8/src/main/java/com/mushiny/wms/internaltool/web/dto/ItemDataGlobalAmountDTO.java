package com.mushiny.wms.internaltool.web.dto;

import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.ItemDataGlobal;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemDataGlobalAmountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal amount = BigDecimal.ZERO;

//    private ItemDataGlobal itemDataGlobal;

    private ItemData itemData;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

//    public ItemDataGlobal getItemDataGlobal() {
//        return itemDataGlobal;
//    }
//
//    public void setItemDataGlobal(ItemDataGlobal itemDataGlobal) {
//        this.itemDataGlobal = itemDataGlobal;
//    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }
}
