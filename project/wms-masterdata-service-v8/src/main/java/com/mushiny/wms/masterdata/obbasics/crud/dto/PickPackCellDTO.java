package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocationType;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackCell;

import javax.validation.constraints.NotNull;

public class PickPackCellDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    private PickPackCellTypeDTO pickPackCellType;

    @NotNull
    private int xPos = 0;

    @NotNull
    private int yPos = 0;

    @NotNull
    private int zPos = 0;

    private String field;

    private int fieldIndex = 0;

    @NotNull
    private int orderIndex = 0;

    @NotNull
    private String labelColor;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pickPackWallId;

    private PickPackWallDTO pickPackWall;

    private String digitalLabel1Id;

    private DigitalLabelDTO digitalLabel1;

    private String digitalLabel2Id;

    private DigitalLabelDTO digitalLabel2;

    private StorageLocationType storageLocationType;

    private Area area;


    public PickPackCellDTO() {
    }

    public PickPackCellDTO(PickPackCell entity) {
        super(entity);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public PickPackCellTypeDTO getPickPackCellType() {
        return pickPackCellType;
    }

    public void setPickPackCellType(PickPackCellTypeDTO pickPackCellType) {
        this.pickPackCellType = pickPackCellType;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getPickPackWallId() {
        return pickPackWallId;
    }

    public void setPickPackWallId(String pickPackWallId) {
        this.pickPackWallId = pickPackWallId;
    }

    public PickPackWallDTO getPickPackWall() {
        return pickPackWall;
    }

    public void setPickPackWall(PickPackWallDTO pickPackWall) {
        this.pickPackWall = pickPackWall;
    }

    public DigitalLabelDTO getDigitalLabel1() {
        return digitalLabel1;
    }

    public void setDigitalLabel1(DigitalLabelDTO digitalLabel1) {
        this.digitalLabel1 = digitalLabel1;
    }

    public DigitalLabelDTO getDigitalLabel2() {
        return digitalLabel2;
    }

    public void setDigitalLabel2(DigitalLabelDTO digitalLabel2) {
        this.digitalLabel2 = digitalLabel2;
    }

    public String getDigitalLabel1Id() {
        return digitalLabel1Id;
    }

    public void setDigitalLabel1Id(String digitalLabel1Id) {
        this.digitalLabel1Id = digitalLabel1Id;
    }

    public String getDigitalLabel2Id() {
        return digitalLabel2Id;
    }

    public void setDigitalLabel2Id(String digitalLabel2Id) {
        this.digitalLabel2Id = digitalLabel2Id;
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
