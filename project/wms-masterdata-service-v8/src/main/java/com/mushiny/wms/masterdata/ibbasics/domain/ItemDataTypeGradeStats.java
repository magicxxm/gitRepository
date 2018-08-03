package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "IB_ITEMDATETYPEGRADESTATS")
public class ItemDataTypeGradeStats extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "SKU_GRADE", nullable = false)
    private String skuGrade;

    @Column(name = "ADJ_SKU_GRADE", nullable = false)
    private String skuAdjustGrade;

    @Column(name = "SHIPMENT_DAY", nullable = false)
    private BigDecimal shipmentDay;

    @Column(name = "ALTER_STATE", nullable = false)
    private int alterState=0;

    @Column(name = "UNITS_SHIPMENT", nullable = false)
    private BigDecimal unitsShipment;

    @Column(name = "UNITS_DAY", nullable = false)
    private BigDecimal unitsDay;

    @Column(name = "ADJUST_EXPIRE_DATE", nullable = false)
    private LocalDateTime adjustExpireDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

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

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
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
