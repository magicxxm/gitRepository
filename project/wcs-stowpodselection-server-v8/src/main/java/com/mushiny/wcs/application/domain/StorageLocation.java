package com.mushiny.wcs.application.domain;

import com.mushiny.wcs.common.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "MD_STORAGELOCATION")
public class StorageLocation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "XPOS", nullable = false)
    private int xPos = 0;

    @Column(name = "YPOS", nullable = false)
    private int yPos = 0;

    @Column(name = "ZPOS", nullable = false)
    private int zPos = 0;

    @Column(name = "FIELD")
    private String field;

    @Column(name = "FIELD_INDEX", nullable = false)
    private int fieldIndex = 0;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int orderIndex = 0;

    @Column(name = "ALLOCATION", nullable = false)
    private BigDecimal allocation;

    @Column(name = "ALLOCATION_STATE", nullable = false)
    private int allocationState = 0;

    @Column(name = "STOCKTAKING_DATE")
    private LocalDateTime stocktakingDate;

    @Column(name = "FACE")
    private String face;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "DROPZONE_ID")
    private String dropZoneId;

    @Column(name = "POD_ID")
    private String podId;

    @Column(name = "AREA_ID")
    private String areaId;

    @Column(name = "ZONE_ID")
    private String zoneId;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private StorageLocationType storageLocationType;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
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

    public LocalDateTime getStocktakingDate() {
        return stocktakingDate;
    }

    public void setStocktakingDate(LocalDateTime stocktakingDate) {
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

    public StorageLocationType getStorageLocationType() {
        return storageLocationType;
    }

    public void setStorageLocationType(StorageLocationType storageLocationType) {
        this.storageLocationType = storageLocationType;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getDropZoneId() {
        return dropZoneId;
    }

    public void setDropZoneId(String dropZoneId) {
        this.dropZoneId = dropZoneId;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }
}
