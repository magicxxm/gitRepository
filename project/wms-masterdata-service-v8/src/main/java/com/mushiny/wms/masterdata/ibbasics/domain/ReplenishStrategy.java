package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "IB_REPLENISHSTRATEGY")
public class ReplenishStrategy extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "SHIPMENT_DAY", nullable = false)
    private BigDecimal shipmentDay;

    @Column(name = "UNITS_SHIPMENT", nullable = false)
    private BigDecimal unitsShipment;

    @Column(name = "REPLENISH_TRIGGER")
    private String replenishTrigger;

    @Column(name = "FUD_STRATEGY", nullable = false)
    private String fudStrategy;

    @Column(name = "REPLENISH_PAD_TIME")
    private String replenishPadTime;

    @Column(name = "RECEIVE_IS_REPLENISH", nullable = false)
    private String receiveIsReplenish;

    @Column(name = "RECEIVE_IS_REPLENISH_CONDITION")
    private String receiveIsReplenishCondition;

    @Column(name = "SKU_MAX_TYPE", nullable = false)
    private String skuMaxType;

    @Column(name = "REPLENISH_COUNT")
    private String replenishCount;


    public String getReceiveIsReplenish() {
        return receiveIsReplenish;
    }

    public void setReceiveIsReplenish(String receiveIsReplenish) {
        this.receiveIsReplenish = receiveIsReplenish;
    }

    public String getReceiveIsReplenishCondition() {
        return receiveIsReplenishCondition;
    }

    public void setReceiveIsReplenishCondition(String receiveIsReplenishCondition) {
        this.receiveIsReplenishCondition = receiveIsReplenishCondition;
    }

    public String getReplenishCount() {
        return replenishCount;
    }

    public void setReplenishCount(String replenishCount) {
        this.replenishCount = replenishCount;
    }

    public BigDecimal getShipmentDay() {
        return shipmentDay;
    }

    public void setShipmentDay(BigDecimal shipmentDay) {
        this.shipmentDay = shipmentDay;
    }

    public BigDecimal getUnitsShipment() {
        return unitsShipment;
    }

    public void setUnitsShipment(BigDecimal unitsShipment) {
        this.unitsShipment = unitsShipment;
    }

    public String getReplenishTrigger() {
        return replenishTrigger;
    }

    public void setReplenishTrigger(String replenishTrigger) {
        this.replenishTrigger = replenishTrigger;
    }

    public String getFudStrategy() {
        return fudStrategy;
    }

    public void setFudStrategy(String fudStrategy) {
        this.fudStrategy = fudStrategy;
    }

    public String getReplenishPadTime() {
        return replenishPadTime;
    }

    public void setReplenishPadTime(String replenishPadTime) {
        this.replenishPadTime = replenishPadTime;
    }


    public String getSkuMaxType() {
        return skuMaxType;
    }

    public void setSkuMaxType(String skuMaxType) {
        this.skuMaxType = skuMaxType;
    }

}
