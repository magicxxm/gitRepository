package com.mushiny.model;

import com.mushiny.common.entity.BaseEntity;
import com.mushiny.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "MD_STORAGELOCATIONTYPE")
public class StorageLocationType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "HEIGHT")
    private BigDecimal height;

    @Column(name = "WIDTH")
    private BigDecimal width;

    @Column(name = "DEPTH")
    private BigDecimal depth;

    @Column(name = "VOLUME")
    private BigDecimal volume;

    @Column(name = "LIFTING_CAPACITY")
    private BigDecimal liftingCapacity;

    @Column(name = "MAX_ITEMDATA_AMOUNT")
    private int maxItemDataAmount;

    @Column(name = "INVENTORY_STATE")
    private String inventoryState;

    @Column(name = "STORAGETYPE")
    private String storageType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getDepth() {
        return depth;
    }

    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getLiftingCapacity() {
        return liftingCapacity;
    }

    public void setLiftingCapacity(BigDecimal liftingCapacity) {
        this.liftingCapacity = liftingCapacity;
    }

    public int getMaxItemDataAmount() {
        return maxItemDataAmount;
    }

    public void setMaxItemDataAmount(int maxItemDataAmount) {
        this.maxItemDataAmount = maxItemDataAmount;
    }

    public String getInventoryState() {
        return inventoryState;
    }

    public void setInvertoryState(String inventoryState) {
        this.inventoryState = inventoryState;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public void setInventoryState(String inventoryState) {
        this.inventoryState = inventoryState;
    }
}
