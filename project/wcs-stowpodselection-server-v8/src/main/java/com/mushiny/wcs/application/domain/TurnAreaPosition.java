package com.mushiny.wcs.application.domain;

import com.mushiny.wcs.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_TURNAREAPOSITION")
public class TurnAreaPosition extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TURNAREANODETYPE")
    private Integer turnAreaNodeType;

    @Column(name = "XPOSITION")
    private Integer xPosition;

    @Column(name = "YPOSITION")
    private Integer yPosition;

    @Column(name = "ADDRESSCODEID")
    private Integer addressCodeId;

    @ManyToOne
    @JoinColumn(name = "TURNAREA_ID")
    private TurnArea turnArea;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTurnAreaNodeType() {
        return turnAreaNodeType;
    }

    public void setTurnAreaNodeType(Integer turnAreaNodeType) {
        this.turnAreaNodeType = turnAreaNodeType;
    }

    public Integer getxPosition() {
        return xPosition;
    }

    public void setxPosition(Integer xPosition) {
        this.xPosition = xPosition;
    }

    public Integer getyPosition() {
        return yPosition;
    }

    public void setyPosition(Integer yPosition) {
        this.yPosition = yPosition;
    }

    public Integer getAddressCodeId() {
        return addressCodeId;
    }

    public void setAddressCodeId(Integer addressCodeId) {
        this.addressCodeId = addressCodeId;
    }

    public TurnArea getTurnArea() {
        return turnArea;
    }

    public void setTurnArea(TurnArea turnArea) {
        this.turnArea = turnArea;
    }
}
