package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinCell;

import javax.validation.constraints.NotNull;

public class ReBinCellDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    @NotNull
    private int xPos = 0;

    @NotNull
    private int yPos = 0;

    @NotNull
    private int zPos = 0;

    @NotNull
    private int orderIndex = 0;

    @NotNull
    private String labelColor;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rebinWallId;

    private ReBinCellTypeDTO rebinCellType;

    private ReBinWallDTO rebinWall;

    public ReBinCellDTO() {
    }

    public ReBinCellDTO(ReBinCell entity) {
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

    public String getRebinWallId() {
        return rebinWallId;
    }

    public void setRebinWallId(String rebinWallId) {
        this.rebinWallId = rebinWallId;
    }

    public ReBinCellTypeDTO getRebinCellType() {
        return rebinCellType;
    }

    public void setRebinCellType(ReBinCellTypeDTO rebinCellType) {
        this.rebinCellType = rebinCellType;
    }

    public ReBinWallDTO getRebinWall() {
        return rebinWall;
    }

    public void setRebinWall(ReBinWallDTO rebinWall) {
        this.rebinWall = rebinWall;
    }
}
