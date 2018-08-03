package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWallTypePosition;

import javax.validation.constraints.NotNull;

public class ReBinWallTypePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String name;

    @NotNull
    private int xPos;

    @NotNull
    private int yPos;

    @NotNull
    private int zPos;

    @NotNull
    private Integer orderIndex;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rebinCellTypeId;

    private ReBinCellTypeDTO rebinCellType;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rebinWallTypeId;

    private ReBinWallTypeDTO rebinWallType;

    public ReBinWallTypePositionDTO() {
    }

    public ReBinWallTypePositionDTO(ReBinWallTypePosition entity) {
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

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getRebinCellTypeId() {
        return rebinCellTypeId;
    }

    public void setRebinCellTypeId(String rebinCellTypeId) {
        this.rebinCellTypeId = rebinCellTypeId;
    }

    public ReBinCellTypeDTO getRebinCellType() {
        return rebinCellType;
    }

    public void setRebinCellType(ReBinCellTypeDTO rebinCellType) {
        this.rebinCellType = rebinCellType;
    }

    public ReBinWallTypeDTO getRebinWallType() {
        return rebinWallType;
    }

    public void setRebinWallType(ReBinWallTypeDTO rebinWallType) {
        this.rebinWallType = rebinWallType;
    }

    public String getRebinWallTypeId() {
        return rebinWallTypeId;
    }

    public void setRebinWallTypeId(String rebinWallTypeId) {
        this.rebinWallTypeId = rebinWallTypeId;
    }
}
