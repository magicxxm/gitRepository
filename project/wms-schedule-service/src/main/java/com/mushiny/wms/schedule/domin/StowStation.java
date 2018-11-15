package com.mushiny.wms.schedule.domin;


import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name="IB_STOWSTATION")
public class StowStation extends BaseWarehouseAssignedEntity {

    @Column(name="NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name="OPERATOR_ID")
    private User operator;

    @ManyToOne
    @JoinColumn(name="WORKSTATION_ID")
    private WorkStation workStation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }
}
