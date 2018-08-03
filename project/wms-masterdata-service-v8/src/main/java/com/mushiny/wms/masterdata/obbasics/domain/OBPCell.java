package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OBP_OBPCELL", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"})
})
public class OBPCell extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private OBPCellType obpCellType;

    @Column(name = "XPOS", nullable = false)
    private int xPos = 0;

    @Column(name = "ZPOS", nullable = false)
    private int zPos = 0;

    @Column(name = "YPOS", nullable = false)
    private int yPos = 0;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int orderIndex;

    @Column(name = "LABEL_COLOR", nullable = false)
    private String labelColor;

    @Column(name = "STATE")
    private String state;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WALL_ID", nullable = false)
    private OBPWall obpWall;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public OBPCellType getObpCellType() {
        return obpCellType;
    }

    public void setObpCellType(OBPCellType obpCellType) {
        this.obpCellType = obpCellType;
    }

    public OBPWall getObpWall() {
        return obpWall;
    }

    public void setObpWall(OBPWall obpWall) {
        this.obpWall = obpWall;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
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
}
