package com.mushiny.wms.outboundproblem.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.StorageLocationDTO;
import com.mushiny.wms.outboundproblem.domain.OBPCell;
import com.mushiny.wms.outboundproblem.domain.common.StockUnit;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OBPCellPositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name; //问题格名称

    private int orderIndex;

    private String labelColor;

    //是否占用Unoccupied/Occupied
    private String state;

    //扫描拣货回来之后的车牌，显示车内货对应在哪些格
    private boolean goodsInCell = false;

    //扫描商品判断放在哪个cell格
    //private boolean inThisCell=false;

    private boolean scaned = false;

    private int xPos;

    private int yPos;

    private int zPos;

    private BigDecimal amountScanedProblem = BigDecimal.ZERO;

    private BigDecimal amountProblem = BigDecimal.ZERO;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String wallId;

    private OBPWallDTO obpWall;

    private StorageLocationDTO storageLocation;

    public OBPCellPositionDTO() {
    }

    public OBPCellPositionDTO(OBPCell entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public BigDecimal getAmountScanedProblem() {
        return amountScanedProblem;
    }

    public void setAmountScanedProblem(BigDecimal amountScanedProblem) {
        this.amountScanedProblem = amountScanedProblem;
    }

    public BigDecimal getAmountProblem() {
        return amountProblem;
    }

    public void setAmountProblem(BigDecimal amountProblem) {
        this.amountProblem = amountProblem;
    }

    public StorageLocationDTO getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocationDTO storageLocation) {
        this.storageLocation = storageLocation;
    }

    public boolean isScaned() {
        return scaned;
    }

    public void setScaned(boolean scaned) {
        this.scaned = scaned;
    }

    public boolean isGoodsInCell() {
        return goodsInCell;
    }

    public void setGoodsInCell(boolean goodsInCell) {
        this.goodsInCell = goodsInCell;
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

}
