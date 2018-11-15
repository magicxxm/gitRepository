package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/4/26.
 */
@Entity
@Table(name = "OB_PICKSTATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NAME", "WAREHOUSE_ID"})
})
public class PickStation extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private PickStationType pickStationType;

    @ManyToOne
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @ManyToOne
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

    public PickStationType getPickStationType() {
        return pickStationType;
    }

    public void setPickStationType(PickStationType pickStationType) {
        this.pickStationType = pickStationType;
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
