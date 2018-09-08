package com.mushiny.wms.application.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/7/7 0007.
 */
@Entity
@Table(name = "WMS_INV_STOCKUNIT" )
public class InvStockunit extends BaseEntity {
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "RESERVED_AMOUNT")
    private BigDecimal reservedAmount;
    @Column(name = "STATE")
    private String state;




    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
