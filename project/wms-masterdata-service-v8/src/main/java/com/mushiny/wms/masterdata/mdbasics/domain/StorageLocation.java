package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.masterdata.ibbasics.domain.UnitLoad;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MD_STORAGELOCATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class StorageLocation extends BaseClientAssignedEntity {
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private StorageLocationType storageLocationType;

    @ManyToOne
    @JoinColumn(name = "AREA_ID")
    private Area area;

    @ManyToOne
    @JoinColumn(name = "ZONE_ID")
    private Zone zone;

    @ManyToOne
    @JoinColumn(name = "DROPZONE_ID")
    private DropZone dropZone;

    @ManyToOne
    @JoinColumn(name = "POD_ID")
    private Pod pod;

    @ManyToOne
    @JoinColumn(name = "BAY_ID")
    private Bay bay;

    @ManyToOne
    @JoinColumn(name = "SECTION_ID")
    private Section section;

    @OneToMany(mappedBy = "storageLocation")
    private List<UnitLoad> unitLoad;

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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public DropZone getDropZone() {
        return dropZone;
    }

    public void setDropZone(DropZone dropZone) {
        this.dropZone = dropZone;
    }

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public List<UnitLoad> getUnitLoad() {
        return unitLoad;
    }

    public void setUnitLoad(List<UnitLoad> unitLoad) {
        this.unitLoad = unitLoad;
    }

    public Bay getBay() {
        return bay;
    }

    public void setBay(Bay bay) {
        this.bay = bay;
    }
}
