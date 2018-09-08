package com.mushiny.wms.application.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2018/7/9.
 */
@Entity
@Table(name = "WMS_INV_STOCKUNIT")
public class WmsInvStockunit extends BaseEntity {
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "RESERVED_AMOUNT")
    private BigDecimal reservedAmount;
    @Column(name = "STATE")
    private String state;
    @Column(name = "ITEMDATA_ID")
    private String itemdataId;
    @Column(name = "UNITLOAD_ID")
    private String unitloadId;
    @Column(name = "CLIENT_ID")
    private String clientId;
    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;


    public String getItemdataId() {
        return itemdataId;
    }

    public void setItemdataId(String itemdataId) {
        this.itemdataId = itemdataId;
    }


    public String getUnitloadId() {
        return unitloadId;
    }

    public void setUnitloadId(String unitloadId) {
        this.unitloadId = unitloadId;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }



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
