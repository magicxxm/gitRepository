package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReplenishStrategy;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ReplenishStrategyDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private BigDecimal shipmentDay;
    @NotNull
    private BigDecimal unitsShipment;
    private String replenishTrigger;
    @NotNull
    private String fudStrategy;
    private String replenishPadTime;
    @NotNull
    private String receiveIsReplenish;
    private String receiveIsReplenishCondition;
    @NotNull
    private String replenishCount;
    @NotNull
    private String skuMaxType;

    public ReplenishStrategyDTO() {
    }

    public ReplenishStrategyDTO(ReplenishStrategy entity) {
        super(entity);
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
}
