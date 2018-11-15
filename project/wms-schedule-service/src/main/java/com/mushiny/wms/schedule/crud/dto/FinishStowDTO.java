package com.mushiny.wms.schedule.crud.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class FinishStowDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * SN
     */
    private String sn;
    /**
     * 有效期
     */
    private String useNotAfter;
    /**
     * itemid
     */
    private String itemId;
    /**
     * storagelocationId
     */
    private String storageLocationId;
    /**
     * amount收货数量
     */
    private BigDecimal amount;
    /**
     * 上架方式(单件，批量)
     */
    private String finishType;

    private String toolName;

    private int finishPositionIndex;

    public FinishStowDTO() {
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUseNotAfter() {
        return useNotAfter;
    }

    public void setUseNotAfter(String useNotAfter) {
        this.useNotAfter = useNotAfter;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(String storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFinishType() {
        return finishType;
    }

    public void setFinishType(String finishType) {
        this.finishType = finishType;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public int getFinishPositionIndex() {
        return finishPositionIndex;
    }

    public void setFinishPositionIndex(int finishPositionIndex) {
        this.finishPositionIndex = finishPositionIndex;
    }
}
