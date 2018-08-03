package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "OB_PROCESSPATHTYPE")
public class ProcessPathType extends BaseWarehouseAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PICK_FLOW", nullable = false)
    private String pickFlow;
    @Column(name = "PICK_WAY", nullable = false)
    private String pickWay;


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

    public String getPickFlow() {
        return pickFlow;
    }

    public void setPickFlow(String pickFlow) {
        this.pickFlow = pickFlow;
    }

    public String getPickWay() {
        return pickWay;
    }

    public void setPickWay(String pickWay) {
        this.pickWay = pickWay;
    }
}
