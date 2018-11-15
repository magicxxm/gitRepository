package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OB_REBINWALLTYPEPOSITION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NAME", "REBINWALLTYPE_ID", "WAREHOUSE_ID"})
})
public class RebinWallTypePosition extends BaseWarehouseAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "XPOS", nullable = false)
    private int XPos = 0;

    @Column(name = "YPOS", nullable = false)
    private int YPos = 0;

    @Column(name = "ZPOS", nullable = false)
    private int ZPos = 0;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int index = 0;

    @JoinColumn(name = "REBINCELLTYPE_ID")
    private String rebinCellType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBINWALLTYPE_ID")
    private RebinWallType rebinWallType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getXPos() {
        return XPos;
    }

    public void setXPos(int XPos) {
        this.XPos = XPos;
    }

    public int getYPos() {
        return YPos;
    }

    public void setYPos(int YPos) {
        this.YPos = YPos;
    }

    public int getZPos() {
        return ZPos;
    }

    public void setZPos(int ZPos) {
        this.ZPos = ZPos;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public RebinWallType getRebinWallType() {
        return rebinWallType;
    }

    public void setRebinWallType(RebinWallType rebinWallType) {
        this.rebinWallType = rebinWallType;
    }

    public String getRebinCellType() {
        return rebinCellType;
    }

    public void setRebinCellType(String rebinCellType) {
        this.rebinCellType = rebinCellType;
    }
}
