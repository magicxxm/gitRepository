package com.mushiny.wms.outboundproblem.domain.common;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "MD_STORAGELOCATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class StorageLocation extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "XPOS")
    private int xPos;

    @Column(name = "YPOS")
    private int yPos;

    @Column(name = "ZPOS")
    private int zPos;

    @Column(name = "FIELD")
    private String field;

    @Column(name = "FIELD_INDEX")
    private int fieldIndex;

    @Column(name = "ORDER_INDEX")
    private int orderIndex;

    @Column(name = "ALLOCATION")
    private BigDecimal allocation;

    @Column(name = "ALLOCATION_STATE")
    private int allocationState;

    @Column(name = "STOCKTAKING_DATE")
    private ZonedDateTime stocktakingDate;

    @Column(name = "FACE")
    private String face;

    @Column(name = "COLOR")
    private String color;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private StorageLocationType storageLocationType;

    @ManyToOne
    @JoinColumn(name = "POD_ID")
    private Pod pod;

    @ManyToOne
    @JoinColumn(name = "ZONE_ID")
    private Zone zone;

    @ManyToOne
    @JoinColumn(name = "AREA_ID")
    private Area area;

    @Column(name = "SECTION_ID")
    private String sectionId;

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
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

    public StorageLocationType getStorageLocationType() {
        return storageLocationType;
    }

    public void setStorageLocationType(StorageLocationType storageLocationType) {
        this.storageLocationType = storageLocationType;
    }

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
