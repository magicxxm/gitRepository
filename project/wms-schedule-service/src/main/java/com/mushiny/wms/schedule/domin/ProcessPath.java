package com.mushiny.wms.schedule.domin;


import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name="OB_PROCESSPATH")

public class ProcessPath extends BaseWarehouseAssignedEntity {

    @Column(name="NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name="TYPE_ID")
    private ProcessPathType processPathType;

    @Column(name="IS_HOTPICK")
    private boolean isHotPick;

    @ManyToOne
    @JoinColumn(name="REBINWALLTYPE_ID")
    private RebinWallType rebinWallType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHotPick() {
        return isHotPick;
    }

    public void setHotPick(boolean hotPick) {
        isHotPick = hotPick;
    }

    public ProcessPathType getProcessPathType() {
        return processPathType;
    }

    public void setProcessPathType(ProcessPathType processPathType) {
        this.processPathType = processPathType;
    }

    public RebinWallType getRebinWallType() {
        return rebinWallType;
    }

    public void setRebinWallType(RebinWallType rebinWallType) {
        this.rebinWallType = rebinWallType;
    }
}
