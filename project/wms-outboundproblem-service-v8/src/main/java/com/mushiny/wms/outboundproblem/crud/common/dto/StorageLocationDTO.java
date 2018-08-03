package com.mushiny.wms.outboundproblem.crud.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class StorageLocationDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String name;

    @NotNull
    private int xPos = 0;

    @NotNull
    private int yPos = 0;

    @NotNull
    private int zPos = 0;

    private String field;

    @NotNull
    private int fieldIndex = 0;

    @NotNull
    private int orderIndex = 0;

    private String scanCode;

    @NotNull
    private BigDecimal allocation;

    @NotNull
    private int allocationState = 0;

    private ZonedDateTime stocktakingDate;

    private String face;

    private String color;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String zoneId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dropZoneId;

    public StorageLocationDTO() {
    }

    public StorageLocationDTO(StorageLocation entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getzPos() {
        return zPos;
    }

    public void setzPos(int zPos) {
        this.zPos = zPos;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public BigDecimal getAllocation() {
        return allocation;
    }

    public void setAllocation(BigDecimal allocation) {
        this.allocation = allocation;
    }

    public int getAllocationState() {
        return allocationState;
    }

    public void setAllocationState(int allocationState) {
        this.allocationState = allocationState;
    }

    public ZonedDateTime getStocktakingDate() {
        return stocktakingDate;
    }

    public void setStocktakingDate(ZonedDateTime stocktakingDate) {
        this.stocktakingDate = stocktakingDate;
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getDropZoneId() {
        return dropZoneId;
    }

    public void setDropZoneId(String dropZoneId) {
        this.dropZoneId = dropZoneId;
    }
}
