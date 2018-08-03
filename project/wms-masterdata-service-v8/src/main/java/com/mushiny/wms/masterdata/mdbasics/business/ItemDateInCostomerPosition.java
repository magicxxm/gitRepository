package com.mushiny.wms.masterdata.mdbasics.business;

import java.math.BigDecimal;

/**
 * Created by Laptop-11 on 2017/12/28.
 */
public class ItemDateInCostomerPosition {
    private String itemId;
    private BigDecimal itemNumber;
    private int itemN;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(BigDecimal itemNumber) {
        this.itemNumber = itemNumber;
    }

    public int getItemN() {
        return itemN;
    }

    public void setItemN(int itemN) {
        this.itemN = itemN;
    }
}
