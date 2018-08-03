package com.mushiny.wms.outboundproblem.domain;

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

    @Column(name = "STATE")
    private String state;

    @Column(name = "LABEL_COLOR")
    private String labelColor;

    @Column(name = "ORDER_INDEX")
    private int orderIndex;

    @Column(name = "XPOS")
    private int xPos;

    @Column(name = "YPOS")
    private int yPos;

    @Column(name = "ZPOS")
    private int zPos;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private OBPCellType obpCellType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WALL_ID")
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
