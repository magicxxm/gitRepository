package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OB_REBINWALLTYPEPOSITION")
public class ReBinWallTypePosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "XPOS", nullable = false)
    private int xPos;

    @Column(name = "YPOS", nullable = false)
    private int yPos;

    @Column(name = "ZPOS", nullable = false)
    private int zPos;

    @Column(name = "ORDER_INDEX")
    private Integer orderIndex;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBINCELLTYPE_ID")
    private ReBinCellType reBinCellType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBINWALLTYPE_ID")
    private ReBinWallType reBinWallType;

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

    public ReBinCellType getReBinCellType() {
        return reBinCellType;
    }

    public void setReBinCellType(ReBinCellType reBinCellType) {
        this.reBinCellType = reBinCellType;
    }

    public ReBinWallType getReBinWallType() {
        return reBinWallType;
    }

    public void setReBinWallType(ReBinWallType reBinWallType) {
        this.reBinWallType = reBinWallType;
    }
}
