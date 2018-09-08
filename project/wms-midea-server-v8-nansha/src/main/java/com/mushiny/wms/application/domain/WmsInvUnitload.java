package com.mushiny.wms.application.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2018/7/9.
 */
@Entity
@Table(name = "WMS_INV_UNITLOAD")
public class WmsInvUnitload   extends BaseEntity {
    @Column(name = "POD_INDEX")
    private Integer podIndex;
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @Column(name = "WEIGHT_CALCULATED")

    private BigDecimal weightCalculated;
    @Column(name = "WEIGHT_MEASURE")
    private BigDecimal weightMeasure;
    @Column(name = "STATION_NAME")
    private String stationName;
    @Column(name = "INBOUND_INSTRUCT_ID")
    private String inboundInstructId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getPodIndex() {
        return podIndex;
    }

    public void setPodIndex(Integer podIndex) {
        this.podIndex = podIndex;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }



    public BigDecimal getWeightCalculated() {
        return weightCalculated;
    }

    public void setWeightCalculated(BigDecimal weightCalculated) {
        this.weightCalculated = weightCalculated;
    }



    public BigDecimal getWeightMeasure() {
        return weightMeasure;
    }

    public void setWeightMeasure(BigDecimal weightMeasure) {
        this.weightMeasure = weightMeasure;
    }



    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }



    public String getInboundInstructId() {
        return inboundInstructId;
    }

    public void setInboundInstructId(String inboundInstructId) {
        this.inboundInstructId = inboundInstructId;
    }


}
