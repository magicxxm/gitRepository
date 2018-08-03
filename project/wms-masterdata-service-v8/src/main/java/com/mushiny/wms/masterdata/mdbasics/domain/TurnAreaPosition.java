package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_TURNAREAPOSITION")
public class TurnAreaPosition extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TURNAREANODETYPE")
    private int trurnAreaNodeType;

    @Column(name = "XPOSITION")
    private int xPosition;

    @Column(name = "YPOSITION")
    private int yPosition;

    @Column(name = "ADDRESSCODEID")
    private int addressCodeId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TURNAREA_ID", nullable = false)
    private TurnArea trurnArea;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTrurnAreaNodeType() {
        return trurnAreaNodeType;
    }

    public void setTrurnAreaNodeType(int trurnAreaNodeType) {
        this.trurnAreaNodeType = trurnAreaNodeType;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public TurnArea getTrurnArea() {
        return trurnArea;
    }

    public void setTrurnArea(TurnArea trurnArea) {
        this.trurnArea = trurnArea;
    }

    public int getAddressCodeId() {
        return addressCodeId;
    }

    public void setAddressCodeId(int addressCodeId) {
        this.addressCodeId = addressCodeId;
    }
}
