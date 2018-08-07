package com.mushiny.model;


import com.mushiny.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by 123 on 2017/8/30.
 */
@Entity
@Table(name = "ICQA_SYSTEM_STOCKTAKINGPOSITION")
public class SystemStocktakingPosition extends BaseClientAssignedEntity {

    @Column(name = "ITEM_NO")
    private String itemNo;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "SKU_NO")
    private String skuNo;

    @Column(name = "STATE")
    private String state = "new";

    @Column(name = "LOCATION_CODE")
    private String locationCode;

    @Column(name = "INVENTORY_STS")
    private String inventorySts;

    @Column(name = "AMOUNT_SYSTEM_WMS")
    private BigDecimal amountSystemWms = BigDecimal.ZERO;

    @Column(name = "AMOUNT_SYSTEM_ANNTO")
    private BigDecimal amountSystemAnnto;

    @Column(name = "AMOUNT_STOCKTAKING")
    private BigDecimal amountStocking = BigDecimal.ZERO;

    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "STARTED")
    private LocalDateTime started;

    @Column(name = "ENDED")
    private LocalDateTime ended;

    @Column(name = "AMOUNT_ADJUST")
    private BigDecimal amountAdjust = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "STOCKTAKING_ID")
    private SystemStocktaking systemStocktaking;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getInventorySts() {
        return inventorySts;
    }

    public void setInventorySts(String inventorySts) {
        this.inventorySts = inventorySts;
    }

    public BigDecimal getAmountSystemWms() {
        return amountSystemWms;
    }

    public void setAmountSystemWms(BigDecimal amountSystemWms) {
        this.amountSystemWms = amountSystemWms;
    }

    public BigDecimal getAmountSystemAnnto() {
        return amountSystemAnnto;
    }

    public void setAmountSystemAnnto(BigDecimal amountSystemAnnto) {
        this.amountSystemAnnto = amountSystemAnnto;
    }

    public BigDecimal getAmountStocking() {
        return amountStocking;
    }

    public void setAmountStocking(BigDecimal amountStocking) {
        this.amountStocking = amountStocking;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getEnded() {
        return ended;
    }

    public void setEnded(LocalDateTime ended) {
        this.ended = ended;
    }

    public BigDecimal getAmountAdjust() {
        return amountAdjust;
    }

    public void setAmountAdjust(BigDecimal amountAdjust) {
        this.amountAdjust = amountAdjust;
    }

    public SystemStocktaking getSystemStocktaking() {
        return systemStocktaking;
    }

    public void setSystemStocktaking(SystemStocktaking systemStocktaking) {
        this.systemStocktaking = systemStocktaking;
    }
}
