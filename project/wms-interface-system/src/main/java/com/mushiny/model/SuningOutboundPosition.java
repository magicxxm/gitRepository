package com.mushiny.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 123 on 2018/5/2.
 */
@Entity
@Table(name = "SUNING_ZRFC_AGV_OUTBOUNDPOSITION")
public class SuningOutboundPosition implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "OUT_ORDER_CODE")
    private String orderCode;

    @Column(name = "ITEM")
    private String lineNo;

    @Column(name = "EXTERNALEANCODE")
    private String itemNo;

    @Column(name = "OWNER_CODE")
    private String clientNo;

    @Column(name = "DFAELL")
    private String lot;

    @Column(name = "STOCK_CODE")
    private String stockState;

    @Column(name = "MENGE")
    private String amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
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

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getStockState() {
        return stockState;
    }

    public void setStockState(String stockState) {
        this.stockState = stockState;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
