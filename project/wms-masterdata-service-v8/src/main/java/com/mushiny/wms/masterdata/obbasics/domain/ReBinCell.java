package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "OB_REBINCELL")
public class ReBinCell extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private ReBinCellType type;

    @Column(name = "XPOS", nullable = false)
    private int xPos = 0;

    @Column(name = "YPOS", nullable = false)
    private int yPos = 0;

    @Column(name = "ZPOS", nullable = false)
    private int zPos = 0;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int orderIndex = 0;

    @Column(name = "LABEL_COLOR", nullable = false)
    private String labelColor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBINWALL_ID", nullable = false)
    private ReBinWall reBinWall;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReBinCellType getType() {
        return type;
    }

    public void setType(ReBinCellType type) {
        this.type = type;
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

    public ReBinWall getReBinWall() {
        return reBinWall;
    }

    public void setReBinWall(ReBinWall reBinWall) {
        this.reBinWall = reBinWall;
    }
}
