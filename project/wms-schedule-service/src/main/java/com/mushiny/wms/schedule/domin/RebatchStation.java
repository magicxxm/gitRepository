package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "OB_REBATCHSTATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NAME", "WAREHOUSE_ID"})
})
public class RebatchStation extends BaseWarehouseAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private RebatchStationType rebatchStationType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATION_ID")
    private WorkStation workStation;

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

    public RebatchStationType getRebatchStationType() {
        return rebatchStationType;
    }

    public void setRebatchStationType(RebatchStationType rebatchStationType) {
        this.rebatchStationType = rebatchStationType;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

}
