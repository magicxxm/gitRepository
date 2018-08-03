package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "IB_INVENTORYANALYSIS")
public class InventoryAnalysis extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;
    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;
    @Column(name = "LEVEL")
    private String level;
    @Column(name = "ONPOD_AMOUNT")
    private int onpodAmount;
    @Column(name = "AVAILABLE_AMOUNT")
    private int availableAmount;
    @Column(name = "MAX_DOC")
    private BigDecimal maxDoc;
    @Column(name = "REPLENISH_DOC")
    private BigDecimal replenishDoc;
    @Column(name = "SAFETY_DOC")
    private BigDecimal safetyDoc;
    @Column(name="BUFFERFUD_AMOUNT")
    private BigDecimal bufferFudAmount;

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getOnpodAmount() {
        return onpodAmount;
    }

    public void setOnpodAmount(int onpodAmount) {
        this.onpodAmount = onpodAmount;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }

    public BigDecimal getMaxDoc() {
        return maxDoc;
    }

    public void setMaxDoc(BigDecimal maxDoc) {
        this.maxDoc = maxDoc;
    }

    public BigDecimal getReplenishDoc() {
        return replenishDoc;
    }

    public void setReplenishDoc(BigDecimal replenishDoc) {
        this.replenishDoc = replenishDoc;
    }

    public BigDecimal getSafetyDoc() {
        return safetyDoc;
    }

    public void setSafetyDoc(BigDecimal safetyDoc) {
        this.safetyDoc = safetyDoc;
    }

    public BigDecimal getBufferFudAmount() {
        return bufferFudAmount;
    }

    public void setBufferFudAmount(BigDecimal bufferFudAmount) {
        this.bufferFudAmount = bufferFudAmount;
    }
}
