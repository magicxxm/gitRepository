package com.mushiny.wms.outboundproblem.crud.dto;


import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CellStorageLocationDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private boolean thisCell=false;

    private boolean scaned=false;

    private boolean isCallPod=false;

    private String itemName;

    private boolean nextPod=false;

    @NotNull
    private String cellName;
    @NotNull
    private int yPos;

    private List<StorageLocationPositionDTO> storageLocationPositions;

    public CellStorageLocationDTO() {
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public List<StorageLocationPositionDTO> getStorageLocationPositions() {
        return storageLocationPositions;
    }

    public void setStorageLocationPositions(List<StorageLocationPositionDTO> storageLocationPositions) {
        this.storageLocationPositions = storageLocationPositions;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public boolean isThisCell() {
        return thisCell;
    }

    public void setThisCell(boolean thisCell) {
        this.thisCell = thisCell;
    }

    public boolean isScaned() {
        return scaned;
    }

    public void setScaned(boolean scaned) {
        this.scaned = scaned;
    }

    public boolean isCallPod() {
        return isCallPod;
    }

    public void setCallPod(boolean callPod) {
        isCallPod = callPod;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isNextPod() {
        return nextPod;
    }

    public void setNextPod(boolean nextPod) {
        this.nextPod = nextPod;
    }

}
