package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocationType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "OB_PICKPACKCELL")
public class PickPackCell extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private PickPackCellType pickPackCellType;

    @Column(name = "XPOS", nullable = false)
    private int xPos = 0;

    @Column(name = "YPOS", nullable = false)
    private int yPos = 0;

    @Column(name = "ZPOS", nullable = false)
    private int zPos = 0;

    @Column(name = "FIELD")
    private String field;

    @Column(name = "FIELD_INDEX")
    private int fieldIndex;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int orderIndex = 0;

    @Column(name = "LABEL_COLOR", nullable = false)
    private String labelColor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PICKPACKWALL_ID", nullable = false)
    private PickPackWall pickPackWall;

    @ManyToOne
    @JoinColumn(name = "DIGITALLABEL1_ID")
    private DigitalLabel digitalLabel1;

    @ManyToOne
    @JoinColumn(name = "DIGITALLABEL2_ID")
    private DigitalLabel digitalLabel2;

    @ManyToOne
    @JoinColumn(name = "STORAGELOCATIONTYPE_ID")
    private StorageLocationType storageLocationType;

    @ManyToOne
    @JoinColumn(name = "AREA_ID")
    private Area area;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PickPackCellType getPickPackCellType() {
        return pickPackCellType;
    }

    public void setPickPackCellType(PickPackCellType pickPackCellType) {
        this.pickPackCellType = pickPackCellType;
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

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public PickPackWall getPickPackWall() {
        return pickPackWall;
    }

    public void setPickPackWall(PickPackWall pickPackWall) {
        this.pickPackWall = pickPackWall;
    }

    public DigitalLabel getDigitalLabel1() {
        return digitalLabel1;
    }

    public void setDigitalLabel1(DigitalLabel digitalLabel1) {
        this.digitalLabel1 = digitalLabel1;
    }

    public DigitalLabel getDigitalLabel2() {
        return digitalLabel2;
    }

    public void setDigitalLabel2(DigitalLabel digitalLabel2) {
        this.digitalLabel2 = digitalLabel2;
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
}
