package com.mushiny.wms.outboundproblem.crud.dto;


import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class StorageLocationPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String storageLocationName;

    private int amountScanedProblem=0;

    private int amountProblem=0;

    private String labelColor;

    private boolean goodsInStorageLocation = false;

    private boolean scaned = false;

    public StorageLocationPositionDTO(){
    }
    public String getStorageLocationName() {
        return storageLocationName;
    }

    public void setStorageLocationName(String storageLocationName) {
        this.storageLocationName = storageLocationName;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public boolean isGoodsInStorageLocation() {
        return goodsInStorageLocation;
    }

    public void setGoodsInStorageLocation(boolean goodsInStorageLocation) {
        this.goodsInStorageLocation = goodsInStorageLocation;
    }

    public boolean isScaned() {
        return scaned;
    }

    public void setScaned(boolean scaned) {
        this.scaned = scaned;
    }

    public int getAmountScanedProblem() {
        return amountScanedProblem;
    }

    public void setAmountScanedProblem(int amountScanedProblem) {
        this.amountScanedProblem = amountScanedProblem;
    }

    public int getAmountProblem() {
        return amountProblem;
    }

    public void setAmountProblem(int amountProblem) {
        this.amountProblem = amountProblem;
    }

}
