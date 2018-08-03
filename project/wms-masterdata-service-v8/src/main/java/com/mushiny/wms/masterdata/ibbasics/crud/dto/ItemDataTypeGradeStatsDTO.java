package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ItemDataTypeGradeStats;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class ItemDataTypeGradeStatsDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private BigDecimal shipmentDay;

    @NotNull
    private BigDecimal unitsShipment;

    @NotNull
    private BigDecimal unitsDay;

    @NotNull
    private String skuGrade;

    @NotNull
    private int alterState=0;

    @NotNull
    private String skuAdjustGrade;

    @NotNull
    private LocalDateTime adjustExpireDate;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String itemDataId;

    private ItemDataDTO itemData;

    public ItemDataTypeGradeStatsDTO() {
    }

    public ItemDataTypeGradeStatsDTO(ItemDataTypeGradeStats entity) {
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

    public BigDecimal getUnitsDay() {
        return unitsDay;
    }

    public void setUnitsDay(BigDecimal unitsDay) {
        this.unitsDay = unitsDay;
    }

    public LocalDateTime getAdjustExpireDate() {
        return adjustExpireDate;
    }

    public void setAdjustExpireDate(LocalDateTime adjustExpireDate) {
        this.adjustExpireDate = adjustExpireDate;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public ItemDataDTO getItemData() {
        return itemData;
    }

    public void setItemData(ItemDataDTO itemData) {
        this.itemData = itemData;
    }

    public String getSkuGrade() {
        return skuGrade;
    }

    public void setSkuGrade(String skuGrade) {
        this.skuGrade = skuGrade;
    }

    public String getSkuAdjustGrade() {
        return skuAdjustGrade;
    }

    public void setSkuAdjustGrade(String skuAdjustGrade) {
        this.skuAdjustGrade = skuAdjustGrade;
    }

    public int getAlterState() {
        return alterState;
    }

    public void setAlterState(int alterState) {
        this.alterState = alterState;
    }
}
