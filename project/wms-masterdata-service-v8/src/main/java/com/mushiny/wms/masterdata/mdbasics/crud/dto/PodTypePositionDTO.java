package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.PodTypePosition;

import javax.validation.constraints.NotNull;

public class PodTypePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private int positionNo = 0;

    @NotNull
    private int level = 0;

    @NotNull
    private int numberOfColumns = 0;

    private String face;

    private String color;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String storageLocationTypeId;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dropZoneId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String podTypeId;

    private StorageLocationTypeDTO storageLocationType;

    private DropZoneDTO dropZone;

    private PodTypeDTO podType;

    public PodTypePositionDTO() {
    }

    public PodTypePositionDTO(PodTypePosition entity) {
        super(entity);
    }

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public String getStorageLocationTypeId() {
        return storageLocationTypeId;
    }

    public void setStorageLocationTypeId(String storageLocationTypeId) {
        this.storageLocationTypeId = storageLocationTypeId;
    }

    public String getDropZoneId() {
        return dropZoneId;
    }

    public void setDropZoneId(String dropZoneId) {
        this.dropZoneId = dropZoneId;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public StorageLocationTypeDTO getStorageLocationType() {
        return storageLocationType;
    }

    public void setStorageLocationType(StorageLocationTypeDTO storageLocationType) {
        this.storageLocationType = storageLocationType;
    }

    public DropZoneDTO getDropZone() {
        return dropZone;
    }

    public void setDropZone(DropZoneDTO dropZone) {
        this.dropZone = dropZone;
    }

    public PodTypeDTO getPodType() {
        return podType;
    }

    public void setPodType(PodTypeDTO podType) {
        this.podType = podType;
    }

    public String getPodTypeId() {
        return podTypeId;
    }

    public void setPodTypeId(String podTypeId) {
        this.podTypeId = podTypeId;
    }
}
