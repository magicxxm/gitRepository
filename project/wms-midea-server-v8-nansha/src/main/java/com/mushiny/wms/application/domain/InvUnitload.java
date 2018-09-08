package com.mushiny.wms.application.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2018/7/7 0007.
 */
@Entity
@Table(name = "WMS_INV_UNITLOAD")
public class InvUnitload extends BaseEntity {
    @Column(name = "POD_INDEX")
    private String podIndex;
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


    public String getPodIndex() {
        return podIndex;
    }

    public void setPodIndex(String podIndex) {
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
