package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "WD_NODE")
public class Node extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "XPOSITION")
    private int xPosition;
    @Column(name = "YPOSITION")
    private int yPosition;
    @Column(name = "ADDRESSCODEID")
    private int addressCodeId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "MAP_ID")
    private Map map;

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

    public int getAddressCodeId() {
        return addressCodeId;
    }

    public void setAddressCodeId(int addressCodeId) {
        this.addressCodeId = addressCodeId;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
