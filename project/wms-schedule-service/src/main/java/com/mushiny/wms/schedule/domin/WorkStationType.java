package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="MD_WORKSTATIONTYPE")
public class WorkStationType extends BaseWarehouseAssignedEntity {

    @Column(name="NAME")
    private String name;

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public WorkStationType() {}
}
