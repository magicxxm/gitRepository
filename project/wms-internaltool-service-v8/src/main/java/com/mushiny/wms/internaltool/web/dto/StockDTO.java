package com.mushiny.wms.internaltool.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/11/8.
 */
public class StockDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemNo;

    private String skuNo;

    private String name;

    private String client;

    private BigDecimal amountUse;

    private BigDecimal amountReserve;

    private BigDecimal amountDamage;

    private BigDecimal amountPending;

    private BigDecimal amountTotal;

    private String warehouse;

    private String stockState;

    //所有商品库存总数
    private Long stockAmount;

    public StockDTO() {
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public BigDecimal getAmountUse() {
        return amountUse;
    }

    public void setAmountUse(BigDecimal amountUse) {
        this.amountUse = amountUse;
    }

    public BigDecimal getAmountReserve() {
        return amountReserve;
    }

    public void setAmountReserve(BigDecimal amountReserve) {
        this.amountReserve = amountReserve;
    }

    public BigDecimal getAmountDamage() {
        return amountDamage;
    }

    public void setAmountDamage(BigDecimal amountDamage) {
        this.amountDamage = amountDamage;
    }

    public BigDecimal getAmountPending() {
        return amountPending;
    }

    public void setAmountPending(BigDecimal amountPending) {
        this.amountPending = amountPending;
    }

    public BigDecimal getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(BigDecimal amountTotal) {
        this.amountTotal = amountTotal;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getStockState() {
        return stockState;
    }

    public void setStockState(String stockState) {
        this.stockState = stockState;
    }

    public Long getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(Long stockAmount) {
        this.stockAmount = stockAmount;
    }
}
