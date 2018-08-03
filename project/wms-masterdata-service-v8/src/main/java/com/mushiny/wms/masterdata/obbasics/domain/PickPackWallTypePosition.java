package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OB_PICKPACKWALLTYPEPOSITION")
public class PickPackWallTypePosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "FIELDTYPE_ID")
    private PickPackFieldType pickPackFieldType;

    @Column(name = "ORDER_INDEX")
    private Integer orderIndex;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WALLTYPE_ID")
    private PickPackWallType pickPackWallType;

    @Column(name="POSITION")
    private Integer position;

    @Column(name="XPOS")
    private Integer xPos;

    @Column(name="YPOS")
    private Integer yPos;

    @Column(name="ZPOS")
    private Integer zPos;

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public PickPackFieldType getPickPackFieldType() {
        return pickPackFieldType;
    }

    public void setPickPackFieldType(PickPackFieldType pickPackFieldType) {
        this.pickPackFieldType = pickPackFieldType;
    }

    public PickPackWallType getPickPackWallType() {
        return pickPackWallType;
    }

    public void setPickPackWallType(PickPackWallType pickPackWallType) {
        this.pickPackWallType = pickPackWallType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getxPos() {
        return xPos;
    }

    public void setxPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getyPos() {
        return yPos;
    }

    public void setyPos(Integer yPos) {
        this.yPos = yPos;
    }

    public Integer getzPos() {
        return zPos;
    }

    public void setzPos(Integer zPos) {
        this.zPos = zPos;
    }
}
