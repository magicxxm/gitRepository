package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/4/27.
 */
@Entity
@Table(name = "OB_REBINSTATIONTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NAME", "WAREHOUSE_ID"})
})
public class RebinStationType extends BaseWarehouseAssignedEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "REBINWALLTYPE_ID")
    private RebinWallType reBinWallType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RebinWallType getReBinWallType() {
        return reBinWallType;
    }

    public void setReBinWallType(RebinWallType reBinWallType) {
        this.reBinWallType = reBinWallType;
    }
}
