package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPCell;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OBPCellDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    private OBPCellTypeDTO obpCellType;

    @NotNull
    private int xPos = 0;

    @NotNull
    private int zPos = 0;

    @NotNull
    private int yPos = 0;

    @NotNull
    private int orderIndex = 0;

    @NotNull
    private String labelColor;

    private String state;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String wallId;

    private OBPWallDTO obpWall;

    public OBPCellDTO() {
    }

    public OBPCellDTO(OBPCell entity) {
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

    public OBPCellTypeDTO getObpCellType() {
        return obpCellType;
    }

    public void setObpCellType(OBPCellTypeDTO obpCellType) {
        this.obpCellType = obpCellType;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getzPos() {
        return zPos;
    }

    public void setzPos(int zPos) {
        this.zPos = zPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
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

    public String getWallId() {
        return wallId;
    }

    public void setWallId(String wallId) {
        this.wallId = wallId;
    }

    public OBPWallDTO getObpWall() {
        return obpWall;
    }

    public void setObpWall(OBPWallDTO obpWall) {
        this.obpWall = obpWall;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
