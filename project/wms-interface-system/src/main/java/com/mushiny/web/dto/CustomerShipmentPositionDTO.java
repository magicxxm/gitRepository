package com.mushiny.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 123 on 2018/2/5.
 */
public class CustomerShipmentPositionDTO implements Serializable {

    private String item;

    private BigDecimal amount;

    private String itemNo;

    private String clientNo;

    private String endDate;//到期日

    private String stockState;//库存状态

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStockState() {
        return stockState;
    }

    public void setStockState(String stockState) {
        this.stockState = stockState;
    }
}
