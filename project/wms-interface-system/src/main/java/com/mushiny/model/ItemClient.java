package com.mushiny.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by 123 on 2018/4/15.
 */
public class ItemClient implements Serializable{

    private String itemDataId;

    private String stockState;

    private String endDate;

    private String  clientId;

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public String getStockState() {
        return stockState;
    }

    public void setStockState(String stockState) {
        this.stockState = stockState;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
